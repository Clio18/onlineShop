package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.dao.jdbc.JdbcProductDao;
import com.obolonyk.onlineshop.dao.jdbc.JdbcUserDao;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.security.SecurityFilter;
import com.obolonyk.onlineshop.web.servlets.*;
import com.obolonyk.onlineshop.utils.DataSourceCreator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = DataSourceCreator.getDataSource();
        List<String> sessionList = Collections.synchronizedList(new ArrayList<>());

        //flyway
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        //config dao
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);

        UserService userService = new UserService();
        userService.setJdbcUserDao(jdbcUserDao);

        SecurityService securityService = new SecurityService();
        securityService.setSessionList(sessionList);
        securityService.setUserService(userService);


        //config servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        removeProductServlet.setProductService(productService);

        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);

        SearchProductsServlet searchProductsServlet = new SearchProductsServlet();
        searchProductsServlet.setProductService(productService);

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);

        RegistrationServlet registrationServlet = new RegistrationServlet();
        registrationServlet.setUserService(userService);
        registrationServlet.setSecurityService(securityService);

        //config filters
        SecurityFilter securityFilter = new SecurityFilter();
        securityFilter.setSecurityService(securityService);

        //config context
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/products/add");
        servletContextHandler.addServlet(new ServletHolder(removeProductServlet), "/products/delete");
        servletContextHandler.addServlet(new ServletHolder(updateProductServlet), "/products/update");
        servletContextHandler.addServlet(new ServletHolder(searchProductsServlet), "/products/search");
        servletContextHandler.addServlet(new ServletHolder(loginServlet), "/login");
        servletContextHandler.addServlet(new ServletHolder(registrationServlet), "/registration");

        servletContextHandler.addFilter(new FilterHolder(securityFilter), "/products/*", EnumSet.of(DispatcherType.REQUEST));

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
