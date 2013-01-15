package com.pipeline.runtime;

/**
* @author Konstantin Tsykulenko
* @since 1/15/13
*/
public class ActionArgumentBinding {
    private Class<?> argumentType;
    private String argumentName;

    public ActionArgumentBinding(Class<?> argumentType, String argumentName) {
        this.argumentType = argumentType;
        this.argumentName = argumentName;
    }

    public boolean isNamedArgument() {
        return getArgumentName() != null;
    }

    public Class<?> getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(Class<?> argumentType) {
        this.argumentType = argumentType;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }
}
