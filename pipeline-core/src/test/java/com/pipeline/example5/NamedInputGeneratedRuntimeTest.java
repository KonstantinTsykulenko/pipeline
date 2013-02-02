package com.pipeline.example5;

import com.pipeline.builder.PipelineBuilder;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.Processor;
import com.pipeline.runtime.generated.ProcessorGenerator;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
public class NamedInputGeneratedRuntimeTest {
    @Test
    public void test() {
        Pipeline pipeline = new PipelineBuilder().node(new NamedInputNode()).buildPipeline();
        Processor processor = new ProcessorGenerator().getProcessor(pipeline);
        HashMap<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put("a", "alpha");
        contextMap.put("b", "beta");
        processor.run(contextMap);
    }
}
