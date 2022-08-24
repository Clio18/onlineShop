package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.servlets.ProductServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Starter {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8085);
        ServletHolder servletHolder = new ServletHolder(new ProductServlet());
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(servletHolder, "/products");
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
