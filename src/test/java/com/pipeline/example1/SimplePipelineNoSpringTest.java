package com.pipeline.example1;

import com.pipeline.builder.PipelineBuilder;
import org.junit.Test;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class SimplePipelineNoSpringTest {
    @Test
    public void test() {
        new PipelineBuilder().
                node(new Action1()).
                node(new Action2()).
                node(new Action3()).
                buildPipeline().run((Object[]) null);
    }
}
