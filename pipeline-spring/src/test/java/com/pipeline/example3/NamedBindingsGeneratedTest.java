package com.pipeline.example3;

import org.junit.Test;

import java.util.HashMap;

/**
 * @author KonstantinTsykulenko
 * @since 1/15/13
 */
public class NamedBindingsGeneratedTest {
    @Test
    public void test() {
        ProcessorImpl processor = new ProcessorImpl();
        processor.factorProcessor = new FactorProcessor();
        processor.logger = new Logger();
        HashMap<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(Integer.class.getName(), 5);
        processor.run(contextMap);
    }
}
