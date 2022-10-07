package com.obolonyk.onlineshop.web.context;

import com.obolonyk.ioc.context.impl.GenericApplicationContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext("context.xml");
        new SingletonContextWrapper(genericApplicationContext);
    }
}
