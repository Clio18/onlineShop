package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.ArrayList;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws Exception {
        List<String> sessionList = new ArrayList<>();
        //config dao
        JdbcProductDao jdbcProductDao = new JdbcProductDao();

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);

        //config servlets
        ProductsServlet productsServlet = new ProductsServlet(sessionList);
        productsServlet.setProductService(productService);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        removeProductServlet.setProductService(productService);

        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);

        LoginServlet loginServlet = new LoginServlet(sessionList);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/products/add");
        servletContextHandler.addServlet(new ServletHolder(removeProductServlet), "/products/delete");
        servletContextHandler.addServlet(new ServletHolder(updateProductServlet), "/products/update");
        servletContextHandler.addServlet(new ServletHolder(loginServlet), "/login");

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
