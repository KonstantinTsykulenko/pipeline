package com.pipeline.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Konstantin Tsykulenko
 * @since 1/1/13
 */
public class PipelineNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("pipeline", new PipelineBeanDefinitionParser());
    }
}
