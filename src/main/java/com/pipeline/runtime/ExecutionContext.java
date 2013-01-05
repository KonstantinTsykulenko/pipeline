package com.pipeline.runtime;

import com.pipeline.exception.IllegalExecutionContextArgumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/3/13
 */
public class ExecutionContext {
    private Map<Class, Map<String, Object>> contextMap = new HashMap<Class, Map<String, Object>>();

    public <T> T getObject(Class<T> clazz) {
        Map<String, Object> objectMap = contextMap.get(clazz);
        if (objectMap.size() == 1) {
            return (T) objectMap.values().iterator().next();
        }
        throw new IllegalStateException(String.format(
                "Execution context should contain exactly one object of class %s to bind invocation arguments by class, but contains %d",
                clazz,
                objectMap.size()));
    }

    public void putObject(Object object) {
        Map<String, Object> nameMap = contextMap.get(object.getClass());
        if (nameMap == null) {
            nameMap = new HashMap<String, Object>();
            nameMap.put(object.getClass().getName(), object);
            contextMap.put(object.getClass(), nameMap);

        }
        else {
            throw new IllegalExecutionContextArgumentException(
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
