package com.xebia.provider;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.annotation.Annotation;
import java.util.List;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class FilterFactory implements ResourceFilterFactory {

    @Override
    public List<ResourceFilter> create(AbstractMethod method) {
        List<ResourceFilter> filters = newArrayList();

        if (isAnnotationPresent(method, CacheControl.class)) {
            javax.ws.rs.core.CacheControl cacheControl = getCacheHeaders(method.getAnnotations(), method.getResource().getAnnotations());
            filters.add(new CacheControlFilter(cacheControl));
        }

        return filters;
    }

    private boolean isAnnotationPresent(AbstractMethod method, Class<? extends Annotation> clazz) {
        return method.isAnnotationPresent(clazz) || method.getResource().isAnnotationPresent(clazz);
    }

    private javax.ws.rs.core.CacheControl getCacheHeaders(Annotation[] methodAnnotations, Annotation[] classAnnotations) {
        CacheControl methodAnnotation = findCacheControl(methodAnnotations);
        if (methodAnnotation != null)
            return adapt(methodAnnotation);

        CacheControl classAnnotation = findCacheControl(classAnnotations);
        return adapt(classAnnotation);
    }

    private CacheControl findCacheControl(Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (annotation instanceof CacheControl)
                return (CacheControl) annotation;
        return null;
    }

    private javax.ws.rs.core.CacheControl adapt(CacheControl annotation) {
        Preconditions.checkArgument(annotation != null, "this factory must only add a %s on %s annotated classes", CacheControlFilter.class.getSimpleName(),
                CacheControl.class.getSimpleName());

        javax.ws.rs.core.CacheControl header = new javax.ws.rs.core.CacheControl();
        header.setMaxAge(annotation.maxAge());
        header.setMustRevalidate(annotation.mustRevalidate());
        header.setNoCache(annotation.noCache());
        header.setNoStore(annotation.noStore());
        header.setNoTransform(annotation.noTransform());
        header.setProxyRevalidate(annotation.proxyRevalidate());
        return header;
    }
}
