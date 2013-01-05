package com.pipeline.spring;

import com.pipeline.Pipeline;
import com.pipeline.runtime.PipelineRuntime;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/2/13
 */
public class PipelineBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String NODE_ELEMENT = "node";
    private static final String NODES_PROPERTY = "nodes";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        AbstractBeanDefinition pipelineBean = parsePipeline(element);

        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(PipelineRuntime.class);
        factory.addConstructorArgValue(pipelineBean);
        AbstractBeanDefinition pipelineRuntimeBean = factory.getBeanDefinition();

        parserContext.getRegistry().registerBeanDefinition(element.getAttribute(ID_ATTRIBUTE), pipelineRuntimeBean);
        return pipelineRuntimeBean;
    }

    private AbstractBeanDefinition parsePipeline(Element element) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(Pipeline.class);

        List<Element> nodes = DomUtils.getChildElementsByTagName(element, NODE_ELEMENT);

        List<AbstractBeanDefinition> nodeDefinitions = parseNodes(nodes);

        factory.addPropertyValue(NODES_PROPERTY, nodeDefinitions);

        return factory.getBeanDefinition();
    }

    private List<AbstractBeanDefinition> parseNodes(List<Element> nodes) {
        List<AbstractBeanDefinition> result = new ManagedList<AbstractBeanDefinition>();
        NodeBeanDefinitionParser nodeBeanDefinitionParser = new NodeBeanDefinitionParser();
        for (Element node : nodes) {
            result.add(nodeBeanDefinitionParser.parseNode(node));
        }
        return result;
    }
}
