package com.pipeline.runtime;

import com.pipeline.annotation.ContextAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/15/13
 */
public class ActionArgumentBinder {
    public List<ActionArgumentBinding> createActionArgumentBindings(Method action) {
        Annotation[][] parameterAnnotations = action.getParameterAnnotations();
        Class<?>[] parameterTypes = action.getParameterTypes();
        List<ActionArgumentBinding> result =
                new ArrayList<ActionArgumentBinding>(parameterTypes.length);

        int index = 0;
        for (Class<?> argumentType : parameterTypes) {
            String argumentName = null;
            for (Annotation annotation : parameterAnnotations[index]) {
                if (annotation.annotationType().equals(ContextAttribute.class)) {
                    argumentName = ((ContextAttribute) annotation).value();
                }
            }
            result.add(new ActionArgumentBinding(argumentType, argumentName));
            ++index;
        }

        return result;
    }
}
