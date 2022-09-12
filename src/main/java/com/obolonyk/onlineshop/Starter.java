package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.servlets.ProductsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Starter {
    public static void main(String[] args) throws Exception {
        //config dao
        JdbcProductDao jdbcProductDao = new JdbcProductDao();

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);

        //config servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);

        ServletHolder servletHolder = new ServletHolder(productsServlet);
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(servletHolder, "/products");

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
