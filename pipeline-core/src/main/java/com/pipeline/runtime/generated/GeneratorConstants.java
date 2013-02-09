package com.pipeline.runtime.generated;

import com.pipeline.runtime.ExecutionContext;
import com.pipeline.runtime.Processor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
class GeneratorConstants {

    static final Method EXECUTION_CONTEXT_PUT_ALL;

    static final Method EXECUTION_CONTEXT_PUT;

    static final Method EXECUTION_CONTEXT_GET;

    static final Method PROCESSOR_RUN;

    static final String PUT_ALL = "putAll";

    static final String PUT = "putObject";

    static final String GET = "getObject";

    static final String CONTEXT_FIELD = "executionContext";

    static final String RUN_METHOD_NAME = "run";

    static {
        try {
            EXECUTION_CONTEXT_PUT_ALL = ExecutionContext.class.getMethod(PUT_ALL, Map.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        try {
            EXECUTION_CONTEXT_PUT = ExecutionContext.class.getMethod(PUT, Object.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        try {
            EXECUTION_CONTEXT_GET = ExecutionContext.class.getMethod(GET, String.class, Class.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        try {
            PROCESSOR_RUN = Processor.class.getMethod(GeneratorConstants.RUN_METHOD_NAME, Map.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    private GeneratorConstants() {
    }
}
