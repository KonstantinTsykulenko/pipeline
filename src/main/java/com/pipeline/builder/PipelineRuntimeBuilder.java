package com.pipeline.builder;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.reflection.PipelineRuntime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class PipelineRuntimeBuilder {
    private PipelineBuilder pipelineBuilder = new PipelineBuilder();
    private static final int DEFAULT_NODE_CAPACITY = 20;
    private List<Node> nodes = new ArrayList<Node>(DEFAULT_NODE_CAPACITY);

    public PipelineRuntime buildPipeline() {
        PipelineRuntime pipelineRuntime = new PipelineRuntime(pipelineBuilder.buildPipeline());
        return pipelineRuntime;
    }

    public PipelineRuntimeBuilder node(Object actionObject) {
        pipelineBuilder.node(actionObject);
        return this;
    }
}
