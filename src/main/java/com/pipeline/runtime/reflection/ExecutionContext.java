package com.pipeline.runtime.reflection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/3/13
 */
public class ExecutionContext {
    private static final Logger logger = Logger.getLogger(ExecutionContext.class);

    private Map<String, Object> contextMap = new HashMap<String, Object>();

    public <T> T getObject(Class<T> clazz) {
        return getObject(clazz.getName(), clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String name, Class<T> clazz) {
        Object object = contextMap.get(name);
        if (object != null) {
            if (clazz.isAssignableFrom(object.getClass())) {
                return (T) object;
            } else {
                throw new IllegalStateException(String.format(
                        "Object with name %s is expected to be of class %s but is %s",
                        name,
                        clazz,
                        object.getClass()));
            }
        }

        throw new IllegalStateException(String.format(
                "No object of class %s with name %s found in execution context",
                clazz,
                name));
    }

    public void putObject(Object object) {
        putObject(object.getClass().getName(), object);
    }

    public void putObject(String name, Object object) {
        if (logger.isEnabledFor(Level.DEBUG)) {
            Object oldObject = contextMap.get(name);
            if (oldObject != null) {
                logger.debug(String.format("Overwriting an object with key '%s' in the execution context",
                        name));
            }
        }
        contextMap.put(name, object);
    }

    public void putAll(Map<String, Object> contextMap) {
        if (contextMap == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
            putObject(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }
}
