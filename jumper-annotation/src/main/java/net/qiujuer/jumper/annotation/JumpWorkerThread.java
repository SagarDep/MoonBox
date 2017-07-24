package net.qiujuer.jumper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface JumpWorkerThread {
}