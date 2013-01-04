package com.pipeline;

import com.pipeline.annotation.HandlerMethod;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KonstantinTsykulenko
 * @since 1/2/13
 */
public class PipelineRuntime {
    private List<NodeRuntime> nodeRuntimeList = new LinkedList<NodeRuntime>();

    public PipelineRuntime(Pipeline pipeline) {
        for (Node node : pipeline.getNodes()) {
            for (Method method : node.getAction().getClass().getMethods()) {
                if (AnnotationUtils.findAnnotation(method, HandlerMethod.class) != null) {
                    nodeRuntimeList.add(new NodeRuntime(method, node.getAction()));
                }
            }
        }
    }

    public void run(Object... params) {
        ExecutionContext executionContext = new ExecutionContext();
        for (Object param : params) {
            executionContext.putObject(param);
        }
        run(executionContext);
    }

    public void run(ExecutionContext executionContext) {
        for(NodeRuntime nodeRuntime : nodeRuntimeList) {
            try {
                List<Object> invocationParams = getInvocationParams(executionContext, nodeRuntime.getArguments());
                Object result = nodeRuntime.action.invoke(nodeRuntime.actionObject, invocationParams.toArray());
                executionContext.putObject(result);
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        }
    }

    private List<Object> getInvocationParams(ExecutionContext executionContext, List<Class<?>> arguments) {
        List<Object> invocationParams = new ArrayList<Object>(arguments.size());
        for (Class<?> argumentClass : arguments) {
            invocationParams.add(executionContext.getObject(argumentClass));
        }
        return invocationParams;
    }

    private static class NodeRuntime {
        private Method action;
        private Object actionObject;

        private NodeRuntime(Method action, Object actionObject) {
            this.action = action;
            this.actionObject = actionObject;
        }

        public List<Class<?>> getArguments() {
            return Arrays.asList(action.getParameterTypes());
        }
    }
}
