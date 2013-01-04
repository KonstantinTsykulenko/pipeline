package com.pipeline;

import com.pipeline.exception.IllegalExecutionContextArgument;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KonstantinTsykulenko
 * @since 1/3/13
 */
public class ExecutionContext {
    private Map<Class, Map<String, Object>> contextMap = new HashMap<Class, Map<String, Object>>();

    public <T> T getObject(Class<T> clazz) {
        Map<String, Object> objectMap = contextMap.get(clazz);
        if (objectMap.size() == 1) {
            return (T) objectMap.values().iterator().next();
        }
        throw new RuntimeException();
    }

    public void putObject(Object object) {
        Map<String, Object> nameMap = contextMap.get(object.getClass());
        if (nameMap == null) {
            nameMap = new HashMap<String, Object>();
            nameMap.put(object.getClass().getName(), object);
            contextMap.put(object.getClass(), nameMap);

        }
        else {
            throw new IllegalExecutionContextArgument(
                    "Object of the same class is put into execution context without disambiguation identifier");
        }
    }

    public void putObject(String id, Object object) {
        Map<String, Object> nameMap = contextMap.get(object.getClass());
        if (nameMap == null) {
            nameMap = new HashMap<String, Object>();
        }
        nameMap.put(id, object);
        contextMap.put(object.getClass(), nameMap);
    }
}
