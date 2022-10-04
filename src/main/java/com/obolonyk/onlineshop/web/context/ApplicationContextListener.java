package com.obolonyk.onlineshop.web.context;

import com.obolonyk.ioc.context.impl.GenericApplicationContext;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    GenericApplicationContext genericApplicationContext = new GenericApplicationContext("context.xml");
    SingletonContextWrapper singletonContextWrapper = new SingletonContextWrapper(genericApplicationContext);
}
