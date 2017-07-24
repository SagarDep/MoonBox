package net.qiujuer.jumper;


import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.kit.handler.runable.Func;
import net.qiujuer.jumper.annotation.JumpType;
import net.qiujuer.jumper.annotation.JumpUiThread;
import net.qiujuer.jumper.annotation.JumpWorkerThread;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
class JumperDelegate<T> implements InvocationHandler {
    private static final String TAG = JumperDelegate.class.getSimpleName();
    private T target;

    JumperDelegate(T target) {
        this.target = target;
    }

    void clear() {
        // TODO clear delay action
    }

    @Override
    public Object invoke(Object o, final Method method, final Object[] objects) throws Throwable {
        Log.p(TAG, "invoke wrap method:" + method.getName() + " and params:" + Arrays.toString(objects));

        // Case Ui annotation
        JumpUiThread uiAnnotation = method.getAnnotation(JumpUiThread.class);
        if (uiAnnotation != null) {
            return invokeUiJump(uiAnnotation, method, objects);
        }

        // Case Worker annotation
        JumpWorkerThread workerAnnotation = method.getAnnotation(JumpWorkerThread.class);
        if (workerAnnotation != null) {
            return invokeWorkerJump(workerAnnotation, method, objects);
        }

        // The default
        return method.invoke(target, objects);
    }

    private Object invokeUiJump(JumpUiThread annotation, final Method method, final Object[] objects) {
        final JumpType jumpType = annotation.value();
        final Type returnType = method.getGenericReturnType();
        switch (jumpType) {
            case AWAIT: {
                if (returnType == void.class) {
                    Run.onUiSync(new Action() {
                        @Override
                        public void call() {
                            callMethod(method, objects);
                        }
                    });
                } else {
                    // Need return, blocking it
                    return Run.onUiSync(new Func<Object>() {
                        @Override
                        public Object call() {
                            return callMethod(method, objects);
                        }
                    });
                }
                break;
            }
            case ASYNC: {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        callMethod(method, objects);
                    }
                });
                break;
            }
            case AUTO:
            default:
                if (returnType == void.class) {
                    // If con't need return param
                    // We should use non blocking operations
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {
                            callMethod(method, objects);
                        }
                    });
                } else {
                    // Need return, blocking it
                    return Run.onUiSync(new Func<Object>() {
                        @Override
                        public Object call() {
                            return callMethod(method, objects);
                        }
                    });
                }
                break;
        }

        return buildReturnByType(method.getGenericReturnType());
    }

    private Object invokeWorkerJump(JumpWorkerThread annotation, final Method method, final Object[] objects) {
        // Support asynchronous operation only
        Run.onBackground(new Action() {
            @Override
            public void call() {
                callMethod(method, objects);
            }
        });
        return buildReturnByType(method.getGenericReturnType());
    }

    private Object callMethod(final Method method, final Object[] objects) {
        try {
            return method.invoke(target, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object buildReturnByType(Type returnType) {
        Log.p(TAG, "buildReturnByType wrap:" + returnType);

        if (returnType == int.class
                || returnType == Integer.class
                || returnType == float.class
                || returnType == Float.class
                || returnType == double.class
                || returnType == Double.class
                || returnType == byte.class
                || returnType == Byte.class
                || returnType == short.class
                || returnType == Short.class
                || returnType == long.class
                || returnType == Long.class
                || returnType == char.class
                || returnType == Character.class) {
            return 0;
        }

        if (returnType == boolean.class
                || returnType == Boolean.class) {
            return false;
        }

        return null;
    }
}
