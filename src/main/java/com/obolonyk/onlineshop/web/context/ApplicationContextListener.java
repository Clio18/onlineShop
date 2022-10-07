package com.obolonyk.onlineshop.web.context;

import com.obolonyk.ioc.context.impl.GenericApplicationContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext("context.xml");
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("applicationContext", genericApplicationContext);
    }
}
