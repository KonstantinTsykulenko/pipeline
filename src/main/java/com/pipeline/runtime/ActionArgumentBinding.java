package com.pipeline.runtime;

/**
* @author Konstantin Tsykulenko
* @since 1/15/13
*/
public class ActionArgumentBinding {
    private final Class<?> argumentType;
    private final String argumentName;

    public ActionArgumentBinding(Class<?> argumentType, String argumentName) {
        this.argumentType = argumentType;
        if (argumentName == null) {
            this.argumentName = argumentType.getName();
            return;
        }
        this.argumentName = argumentName;
    }

    public Class<?> getArgumentType() {
        return argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }
}
