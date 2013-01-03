package com.pipeline.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author KonstantinTsykulenko
 * @since 1/1/13
 */
public class PipelineNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("pipeline", new PipelineBeanDefinitionParser());
    }
}
