package com.pipeline.runtime;

import com.pipeline.exception.IllegalExecutionContextArgumentException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/3/13
 */
public class ExecutionContext {
    private static final Logger logger = Logger.getLogger(ExecutionContext.class);

    private Map<Class, Map<String, Object>> contextMap = new HashMap<Class, Map<String, Object>>();

    public <T> T getObject(Class<T> clazz) {
        return getObject(clazz.getName(), clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String name, Class<T> clazz) {
        Map<String, Object> objectMap = contextMap.get(clazz);
        if (objectMap != null) {
            Object object = objectMap.get(name);
            if (object != null) {
                return (T) object;
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
        Map<String, Object> nameMap = contextMap.get(object.getClass());
        if (nameMap == null) {
            nameMap = new HashMap<String, Object>();
            contextMap.put(object.getClass(), nameMap);
        }
        else if (logger.isEnabledFor(Level.DEBUG)) {
            Object oldObject = nameMap.get(name);
            if (oldObject != null) {
                logger.debug(String.format("Overwriting an object with key '%s' in the execution context",
                        name));
            }
        }
        nameMap.put(name, object);
    }
}
