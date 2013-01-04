package com.pipeline.example2;

import com.pipeline.ExecutionContext;
import com.pipeline.PipelineRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Konstantin Tsykulenko
 * @since 1/4/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config2.xml")
public class ListOfCatsTest {
    @Autowired
    private PipelineRuntime pipelineRuntime;

    @Test
    public void test() {
        pipelineRuntime.run("3");
    }
}
