package com.pipeline.builder;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.reflection.PipelineRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class PipelineBuilder {
    private static final int DEFAULT_NODE_CAPACITY = 20;
    private List<Node> nodes = new ArrayList<Node>(DEFAULT_NODE_CAPACITY);

    public Pipeline buildPipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.setNodes(nodes);
        return pipeline;
    }

    public PipelineBuilder node(Object actionObject) {
        Node node = new Node();
        node.setAction(actionObject);
        nodes.add(node);
        return this;
    }
}
