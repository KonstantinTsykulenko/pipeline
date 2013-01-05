package com.pipeline.exception;

/**
 * @author Konstantin Tsykulenko
 * @since 1/4/13
 */
public class IllegalExecutionContextArgumentException extends RuntimeException {
    public IllegalExecutionContextArgumentException(String message) {
        super(message);
    }
}
