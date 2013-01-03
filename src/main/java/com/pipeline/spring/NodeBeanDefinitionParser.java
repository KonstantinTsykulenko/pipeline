package com.pipeline.spring;

import com.pipeline.Node;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @author KonstantinTsykulenko
 * @since 1/2/13
 */
public class NodeBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String ACTION_REF_ATTRIBUTE = "action-ref";
    private static final String ACTION_PROPERTY = "action";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        AbstractBeanDefinition processBean = parseNode(element);
        return processBean;
    }

    AbstractBeanDefinition parseNode(Element element) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(Node.class);
        String actionRef = element.getAttribute(ACTION_REF_ATTRIBUTE);
        if (StringUtils.hasText(actionRef)) {
            RuntimeBeanReference reference = new RuntimeBeanReference(actionRef);
            factory.addPropertyValue(ACTION_PROPERTY, reference);
        }
        return factory.getBeanDefinition();
    }
}
