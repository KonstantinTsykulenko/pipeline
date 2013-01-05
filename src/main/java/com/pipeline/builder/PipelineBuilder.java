package com.pipeline.builder;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.PipelineRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class PipelineBuilder {
    private static final int DEFAULT_NODE_CAPACITY = 20;
    private List<Node> nodes = new ArrayList<Node>(DEFAULT_NODE_CAPACITY);

    public PipelineRuntime buildPipeline() {
;       Pipeline pipeline = new Pipeline();
        pipeline.setNodes(nodes);
        PipelineRuntime pipelineRuntime = new PipelineRuntime(pipeline);
        return pipelineRuntime;
    }

    public PipelineBuilder node(Object actionObject) {
        Node node = new Node();
        node.setAction(actionObject);
        nodes.add(node);
        return this;
    }
}
