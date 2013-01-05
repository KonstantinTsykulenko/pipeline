package com.pipeline.runtime;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
* @author Konstantin Tsykulenko
* @since 1/5/13
*/
class NodeRuntime {
    private Method action;
    private Object actionObject;
    private List<Class<?>> invocationArgumentTypes;

    NodeRuntime(Method action, Object actionObject) {
        this.action = action;
        this.actionObject = actionObject;
        this.invocationArgumentTypes = getArgumentTypes();
    }

    public void run(ExecutionContext executionContext) {
        Object[] invocationParams = getInvocationParams(executionContext,invocationArgumentTypes);
        Object actionResult = null;
        try {
            actionResult = action.invoke(actionObject, invocationParams);
        } catch (Exception e) {
            throw new IllegalStateException("Illegal pipeline node runtime configuration", e);
        }
        if (actionResult != null) {
            executionContext.putObject(actionResult);
        }
    }

    private Object[] getInvocationParams(ExecutionContext executionContext, List<Class<?>> arguments) {
        Object[] invocationParams = new Object[arguments.size()];
        int index = 0;
        for (Class<?> argumentClass : arguments) {
            invocationParams[index] = executionContext.getObject(argumentClass);
        }
        return invocationParams;
    }

    private List<Class<?>> getArgumentTypes() {
        return Arrays.asList(action.getParameterTypes());
    }
}
