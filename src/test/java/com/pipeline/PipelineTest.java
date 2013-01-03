package com.pipeline;

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
@ContextConfiguration("classpath:test-config.xml")
public class PipelineTest {
    @Autowired
    private PipelineRuntime pipelineRuntime;

    @Test
    public void test() {
        pipelineRuntime.run(null);
    }
}
