package com.pipeline.runtime;

import com.pipeline.Node;
import com.pipeline.Pipeline;
import com.pipeline.annotation.HandlerMethod;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
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
        for (NodeRuntime nodeRuntime : nodeRuntimeList) {
            nodeRuntime.run(executionContext);
        }
    }
}
