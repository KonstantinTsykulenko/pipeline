package com.pipeline.example4;

import com.pipeline.builder.PipelineBuilder;
import com.pipeline.definition.Pipeline;
import com.pipeline.example1.Action1;
import com.pipeline.example1.Action2;
import com.pipeline.example1.Action3;
import com.pipeline.runtime.Processor;
import com.pipeline.runtime.generated.ProcessorGenerator;
import org.junit.Test;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class GeneratedRuntimeTest {
    @Test
    public void test() {
        Pipeline pipeline = new PipelineBuilder().
                node(new Action1()).
                node(new Action2()).
                node(new Action3()).buildPipeline();
        ProcessorGenerator generator = new ProcessorGenerator();
        Processor processor = generator.getProcessor(pipeline);
        processor.run(null);
    }
}
