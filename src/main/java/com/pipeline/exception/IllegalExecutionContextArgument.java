package com.pipeline.exception;

/**
 * @author Konstantin Tsykulenko
 * @since 1/4/13
 */
public class IllegalExecutionContextArgument extends RuntimeException {
    public IllegalExecutionContextArgument(String message) {
        super(message);
    }
}
