package org.landy.springxmlextension.context.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class EHINamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}
