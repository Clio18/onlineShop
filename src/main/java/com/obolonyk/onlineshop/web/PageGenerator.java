package com.obolonyk.onlineshop.web;

import com.obolonyk.templator.ClassPathTemplateFactory;
import com.obolonyk.templator.TemplateFactory;

public class PageGenerator {
    private static TemplateFactory pageGenerator;

    public static TemplateFactory instance() {
        if (pageGenerator == null)
            pageGenerator = new ClassPathTemplateFactory("templates");
        return pageGenerator;
    }
}