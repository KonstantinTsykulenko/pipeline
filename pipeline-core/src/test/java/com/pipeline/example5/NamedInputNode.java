package com.pipeline.example5;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.annotation.HandlerMethod;

/**
 * @author KonstantinTsykulenko
 * @since 1/18/13
 */
public class NamedInputNode {
    @HandlerMethod
    public void logDifferentInput(@ContextAttribute("a") String a, @ContextAttribute("b") String b) {
        System.out.println("a is " + a + " and b is " + b);
    }
}
