package com.pipeline.example3;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.annotation.HandlerMethod;

/**
 * @author Konstantin Tsykulenko
 * @since 1/5/13
 */
public class FactorProcessor {
    @HandlerMethod
    @ContextAttribute("factor")
    public Integer factor(Integer value) {
        if (value < 1)
            throw new IllegalArgumentException();
        if (value == 1)
            return 1;
        return value * factor(value - 1);
    }
}
