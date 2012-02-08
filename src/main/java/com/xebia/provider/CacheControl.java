package com.xebia.provider;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD, TYPE })
public @interface CacheControl {
    int maxAge() default -1;
    boolean noCache() default false;
    boolean noStore() default false;
    boolean noTransform() default false;
    boolean mustRevalidate() default false;
    boolean proxyRevalidate() default false;
}