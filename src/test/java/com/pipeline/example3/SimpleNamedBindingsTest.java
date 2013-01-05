package com.pipeline.example3;

import com.pipeline.runtime.PipelineRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-example3.xml")
public class SimpleNamedBindingsTest {
    @Autowired
    private PipelineRuntime pipelineRuntime;

    @Test
    public void test() {
        pipelineRuntime.run(5);
    }
}
