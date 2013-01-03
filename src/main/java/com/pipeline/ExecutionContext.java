package com.pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KonstantinTsykulenko
 * @since 1/3/13
 */
public class ExecutionContext {
    private Map<Class, Object> contextMap = new HashMap<Class, Object>();

    public <T> T getObjectByType(Class<T> clazz) {
        return (T) contextMap.get(clazz);
    }

    public void putObject(Object object) {
        contextMap.put(object.getClass(), object);
    }
}
