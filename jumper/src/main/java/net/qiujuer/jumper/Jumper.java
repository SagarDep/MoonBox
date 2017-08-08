package net.qiujuer.jumper;

import android.support.annotation.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * This is thread interpreter
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public final class Jumper {

    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target) {
        JumperDelegate<T> handler = new JumperDelegate<>(target);
        Class<T> clx = (Class<T>) target.getClass();
        return (T) Proxy.newProxyInstance(clx.getClassLoader(), clx.getInterfaces(), handler);
    }

    public static <T> void dispose(T targetWrapper) {
        if (targetWrapper == null)
            return;
        if (Proxy.isProxyClass(targetWrapper.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(targetWrapper);
            if (handler instanceof JumperDelegate) {
                JumperDelegate delegate = (JumperDelegate) handler;
                delegate.clear();
            }
        }
    }
}
