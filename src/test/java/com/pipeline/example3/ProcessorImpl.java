package com.pipeline.example3;

import com.pipeline.runtime.Processor;
import com.pipeline.runtime.ExecutionContext;

import java.util.Map;

/**
 * @author KonstantinTsykulenko
 * @since 1/15/13
 */
public class ProcessorImpl implements Processor {

    public FactorProcessor factorProcessor;

    public  Logger logger;

    public ExecutionContext executionContext;

    public ProcessorImpl() {
        executionContext = new ExecutionContext();
    }

    @Override
    public Map<String, Object> run(Map<String, Object> contextMap) {
        executionContext.putAll(contextMap);
        executionContext.putObject("factor", factorProcessor.factor(executionContext.getObject(Integer.class)));
        logger.log(executionContext.getObject("factor", Integer.class));
        return executionContext.getContextMap();
    }
}
