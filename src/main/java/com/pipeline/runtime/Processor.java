package com.pipeline.runtime;

import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public interface Processor {
    Map<String, Object> run(Map<String, Object> contextMap);
}
