package com.pipeline;

import com.pipeline.annotation.HandlerMethod;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KonstantinTsykulenko
 * @since 1/2/13
 */
public class PipelineRuntime {
    private List<Executor> executorList = new LinkedList<Executor>();

    public PipelineRuntime(Pipeline pipeline) {
        for (Node node : pipeline.getNodes()) {
            for (Method method : node.getAction().getClass().getMethods()) {
                if (AnnotationUtils.findAnnotation(method, HandlerMethod.class) != null) {
                    executorList.add(new Executor(method, node.getAction()));
                }
            }
        }
    }

    public void run(ExecutionContext executionContext) {
        for(Executor executor : executorList) {
            try {
                executor.action.invoke(executor.actionObject);
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        }
    }

    private static class Executor {
        private Method action;
        private Object actionObject;

        private Executor(Method action, Object actionObject) {
            this.action = action;
            this.actionObject = actionObject;
        }
    }
}
