package com.pipeline.runtime.reflection;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.ExecutionContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/2/13
 */
public class PipelineRuntime {
    private List<NodeRuntime> nodeRuntimeList = new LinkedList<NodeRuntime>();

    public PipelineRuntime(Pipeline pipeline) {
        for (Node node : pipeline.getNodes()) {
            nodeRuntimeList.add(new NodeRuntime(node.getAction()));
        }
    }

    public Map<String, Object> run(Object... params) {
        ExecutionContext executionContext = new ExecutionContext();
        if (params != null) {
            for (Object param : params) {
                executionContext.putObject(param);
            }
        }
        return run(executionContext);
    }

    public Map<String, Object> run(Map<String, Object> contextMap) {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.putAll(contextMap);
        return run(executionContext);
    }

    public Map<String, Object> run(ExecutionContext executionContext) {
        if (executionContext == null)
            throw new IllegalArgumentException("Execution context can not be null");

        for (NodeRuntime nodeRuntime : nodeRuntimeList) {
            nodeRuntime.run(executionContext);
        }
        return executionContext.getContextMap();
    }
}
