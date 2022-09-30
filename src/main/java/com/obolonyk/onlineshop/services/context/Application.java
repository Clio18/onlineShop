package com.obolonyk.onlineshop.services.context;

import com.obolonyk.ioc.context.impl.GenericApplicationContext;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Application implements ServletContextListener {
    GenericApplicationContext genericApplicationContext = new GenericApplicationContext("context.xml");
    Context context = new Context(genericApplicationContext);
}
