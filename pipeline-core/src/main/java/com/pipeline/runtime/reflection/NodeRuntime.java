package com.pipeline.runtime.reflection;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.runtime.ActionArgumentBinder;
import com.pipeline.runtime.ActionArgumentBinding;
import com.pipeline.runtime.ExecutionContext;
import com.pipeline.util.AnnotationUtils;

import java.lang.reflect.Method;
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
        Method method = AnnotationUtils.getHanlderMethod(actionObject);
        action = method;

        ContextAttribute contextAttribute = AnnotationUtils.getContextAttribute(method);

        if (contextAttribute != null) {
            actionResultName = contextAttribute.value();
        }

        if (this.action == null) {
            throw new IllegalStateException("Could not find a handler method for node of type "
                    + actionObject.getClass());
        }

        this.actionObject = actionObject;
        this.actionArgumentBindings = getActionArgumentBinder().createActionArgumentBindings(action);
    }

    private ActionArgumentBinder getActionArgumentBinder() {
        return new ActionArgumentBinder();
    }

    public void run(ExecutionContext executionContext) {
        Object[] invocationParams = getInvocationParams(executionContext, actionArgumentBindings);
        Object actionResult;
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

    private Object[] getInvocationParams(ExecutionContext executionContext, List<ActionArgumentBinding> arguments) {
        Object[] invocationParams = new Object[arguments.size()];
        int index = 0;
        for (ActionArgumentBinding argumentBinding : arguments) {
                invocationParams[index] = executionContext.getObject(argumentBinding.getArgumentName(),
                        argumentBinding.getArgumentType());
        }
        return invocationParams;
    }

}
