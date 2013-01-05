package com.pipeline.runtime;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;

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
            nodeRuntimeList.add(new NodeRuntime(node.getAction()));
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
