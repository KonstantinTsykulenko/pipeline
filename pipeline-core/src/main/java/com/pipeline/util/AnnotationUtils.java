package com.pipeline.util;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.annotation.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Konstantin Tsykulenko
 * @since 1/15/13
 */
public class AnnotationUtils {
    private AnnotationUtils() { }

    public static <A extends Annotation> Method getAnnotatedMethod(Class<?> clazz, Class<A> annotationToFind) {
        for (Method method : clazz.getMethods()) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation.annotationType().equals(annotationToFind)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationToFind) {
        return method.getAnnotation(annotationToFind);
    }

    public static ContextAttribute getContextAttribute(Method method) {
        return getAnnotation(method, ContextAttribute.class);
    }

    public static Method getHanlderMethod(Class<?> clazz) {
        return getAnnotatedMethod(clazz, HandlerMethod.class);
    }

    public static Method getHanlderMethod(Object actionObject) {
        return getAnnotatedMethod(actionObject.getClass(), HandlerMethod.class);
    }
}
