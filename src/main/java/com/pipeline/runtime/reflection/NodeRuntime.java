package com.pipeline.runtime.reflection;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.annotation.HandlerMethod;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
class NodeRuntime {
    private Method action;
    private Object actionObject;

    private List<ActionArgumentBinding> actionArgumentBindings;
    private String actionResultName;

    NodeRuntime(Object actionObject) {
        for (Method method : actionObject.getClass().getMethods()) {
            if (AnnotationUtils.findAnnotation(method, HandlerMethod.class) != null) {
                this.action = method;
                ContextAttribute contextAttribute =
                        AnnotationUtils.findAnnotation(method, ContextAttribute.class);
                if (contextAttribute != null) {
                    actionResultName = contextAttribute.value();
                }
            }
        }

        if (this.action == null) {
            throw new IllegalStateException("Could not find a handler method for node of type "
                    + actionObject.getClass());
        }

        this.actionObject = actionObject;
        this.actionArgumentBindings = createActionArgumentBindings(action);
    }

    public void run(ExecutionContext executionContext) {
        Object[] invocationParams = getInvocationParams(executionContext, actionArgumentBindings);
        Object actionResult = null;
        try {
            actionResult = action.invoke(actionObject, invocationParams);
        } catch (Exception e) {
            throw new IllegalStateException("Illegal pipeline node runtime configuration", e);
        }
        if (actionResult != null) {
            if (actionResultName == null) {
                executionContext.putObject(actionResult);
            } else {
                executionContext.putObject(actionResultName, actionResult);
            }
        }
    }

    private List<ActionArgumentBinding> createActionArgumentBindings(Method action) {
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
        }
        return result;
    }

    private Object[] getInvocationParams(ExecutionContext executionContext, List<ActionArgumentBinding> arguments) {
        Object[] invocationParams = new Object[arguments.size()];
        int index = 0;
        for (ActionArgumentBinding argumentBinding : arguments) {
            if (!argumentBinding.isNamedArgument()) {
                invocationParams[index] = executionContext.getObject(argumentBinding.argumentType);
            }
            else {
                invocationParams[index] = executionContext.getObject(argumentBinding.argumentName,
                        argumentBinding.argumentType);
            }
        }
        return invocationParams;
    }

    private static class ActionArgumentBinding {
        private Class<?> argumentType;
        private String argumentName;

        private ActionArgumentBinding(Class<?> argumentType, String argumentName) {
            this.argumentType = argumentType;
            this.argumentName = argumentName;
        }

        private boolean isNamedArgument() {
            return argumentName != null;
        }
    }
}
