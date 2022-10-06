package com.obolonyk.onlineshop.web;

import com.obolonyk.onlineshop.utils.PropertiesReader;
import com.obolonyk.templator.ClassPathTemplateFactory;
import com.obolonyk.templator.TemplateFactory;

import java.util.Properties;


public class PageGenerator {
    private static TemplateFactory pageGenerator;

    public static TemplateFactory instance() {
        PropertiesReader propertiesReader = new PropertiesReader();
        Properties properties = propertiesReader.getProperties();
        String pathToResources = properties.getProperty("pathToResources");
        if (pageGenerator == null)
            pageGenerator = new ClassPathTemplateFactory(pathToResources);
        return pageGenerator;
    }
}