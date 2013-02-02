package com.pipeline.example2;

import com.pipeline.annotation.HandlerMethod;

/**
 * @author v-ktsukulenko
 * @since 1/4/13
 */
public class StringToIntTransformer {
    @HandlerMethod
    public Integer transformStringToInt(String s) {
        return Integer.valueOf(s);
    }
}
