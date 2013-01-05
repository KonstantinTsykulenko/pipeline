package com.pipeline.example1;

import com.pipeline.runtime.ExecutionContext;
import com.pipeline.runtime.PipelineRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author KonstantinTsykulenko
 * @since 1/2/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-example1.xml")
public class SimplePipelineTest {
    @Autowired
    private PipelineRuntime pipelineRuntime;

    @Test
    public void test() {
        pipelineRuntime.run((ExecutionContext) null);
    }
}
