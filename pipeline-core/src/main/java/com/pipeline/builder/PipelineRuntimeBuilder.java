package com.pipeline.builder;

import com.pipeline.runtime.reflection.PipelineRuntime;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class PipelineRuntimeBuilder {
    private PipelineBuilder pipelineBuilder = new PipelineBuilder();

    public PipelineRuntime buildPipeline() {
        PipelineRuntime pipelineRuntime = new PipelineRuntime(pipelineBuilder.buildPipeline());
        return pipelineRuntime;
    }

    public PipelineRuntimeBuilder node(Object actionObject) {
        pipelineBuilder.node(actionObject);
        return this;
    }
}
