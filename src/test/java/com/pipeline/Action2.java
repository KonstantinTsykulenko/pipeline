package com.pipeline;

import com.pipeline.annotation.HandlerMethod;

/**
 * @author KonstantinTsykulenko
 * @since 1/2/13
 */
public class Action2 {
    @HandlerMethod
    public void process() {
        System.out.println("action2");
    }
}
