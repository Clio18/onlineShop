package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.servlets.*;
import com.obolonyk.onlineshop.utils.DataSourceCreator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = DataSourceCreator.getDataSource();
        //flyway
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        List<String> sessionList = new ArrayList<>();
        //TODO: add logs
        //config dao
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);

        //config servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);
        productsServlet.setSessionList(sessionList);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        removeProductServlet.setProductService(productService);

        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);

        SearchProductsServlet searchProductsServlet = new SearchProductsServlet();
        searchProductsServlet.setProductService(productService);

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSessionList(sessionList);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/products/add");
        servletContextHandler.addServlet(new ServletHolder(removeProductServlet), "/products/delete");
        servletContextHandler.addServlet(new ServletHolder(updateProductServlet), "/products/update");
        servletContextHandler.addServlet(new ServletHolder(searchProductsServlet), "/products/search");
        servletContextHandler.addServlet(new ServletHolder(loginServlet), "/login");

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
