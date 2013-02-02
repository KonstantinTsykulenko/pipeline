package com.pipeline.example3;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.annotation.HandlerMethod;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class Logger {
    @HandlerMethod
    public void log(@ContextAttribute("factor") Integer value) {
        System.out.println(value);
    }
}
