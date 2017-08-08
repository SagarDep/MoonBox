package net.qiujuer.jumper;


import android.support.annotation.Nullable;

import net.qiujuer.genius.kit.handler.Result;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.kit.handler.runable.Func;
import net.qiujuer.jumper.annotation.JumpType;
import net.qiujuer.jumper.annotation.JumpUiThread;
import net.qiujuer.jumper.annotation.JumpWorkerThread;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
class JumperDelegate<T> implements InvocationHandler {
    private static final String TAG = JumperDelegate.class.getSimpleName();
    private final T target;
    private final List<Reference<Result>> mResultReferences = new LinkedList<>();
    private boolean mDone = false;

    JumperDelegate(T target) {
        this.target = target;
    }

    void clear() {
        mDone = true;
        for (Reference<Result> resultReference : mResultReferences) {
            Result result = resultReference.get();
            if (result != null) {
                result.cancel();
            }
        }
    }

    @Override
    public Object invoke(Object o, final Method method, final Object[] objects) throws Throwable {
        Log.p(TAG, "invoke wrap method:" + method.getName() + " and params:" + Arrays.toString(objects));

        // Case Ui annotation
        JumpUiThread uiAnnotation = method.getAnnotation(JumpUiThread.class);
        if (uiAnnotation != null) {
            if (mDone) {
                //throw new JumperException(String.format("Call method(%s-%s) error, because jumper handler is cancel.", method.getName(), Arrays.toString(objects)));
                return buildReturnByType(method.getReturnType());
            }
            try {
                return invokeUiJump(uiAnnotation, method, objects);
            } catch (JumperException e) {
                e.printStackTrace();
            }
        }

        // Case Worker annotation
        JumpWorkerThread workerAnnotation = method.getAnnotation(JumpWorkerThread.class);
        if (workerAnnotation != null) {
            if (mDone) {
                //throw new JumperException(String.format("Call method(%s-%s) error, because jumper handler is cancel.", method.getName(), Arrays.toString(objects)));
                return buildReturnByType(method.getReturnType());
            }
            try {
                return invokeWorkerJump(workerAnnotation, method, objects);
            } catch (JumperException e) {
                e.printStackTrace();
            }
        }

        // The default
        return method.invoke(target, objects);
    }

    private Object invokeUiJump(JumpUiThread annotation, final Method method, final Object[] objects) throws JumperException {
        final JumpType jumpType = annotation.value();
        final Type returnType = method.getGenericReturnType();
        Result result = null;
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
                result = Run.onUiAsync(new Action() {
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
                    result = Run.onUiAsync(new Action() {
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

        putResult(result);
        return buildReturnByType(method.getGenericReturnType());
    }

    @SuppressWarnings("unused")
    private Object invokeWorkerJump(JumpWorkerThread annotation, final Method method, final Object[] objects) throws JumperException {
        // Support asynchronous operation only
        Result result = Run.onBackground(new Action() {
            @Override
            public void call() {
                callMethod(method, objects);
            }
        });
        putResult(result);
        return buildReturnByType(method.getGenericReturnType());
    }

    private void checkDone(Method method, Object[] objects) throws JumperException {
        // This handler is done, we can't do any request
        if (mDone) {
            throw new JumperException(String.format("Call method(%s-%s) error, because jumper handler is cancel.", method.getName(), Arrays.toString(objects)));
            //return buildReturnByType(method.getReturnType());
        }
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

    @SuppressWarnings("unchecked")
    private void putResult(@Nullable Result result) {
        if (result == null)
            return;
        mResultReferences.add(new SoftReference(result));
    }

    private static Object buildReturnByType(Type returnType) {
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
