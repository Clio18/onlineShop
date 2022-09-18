package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.dao.jdbc.JdbcProductDao;
import com.obolonyk.onlineshop.dao.jdbc.JdbcUserDao;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import com.obolonyk.onlineshop.utils.PropertiesReader;
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
import java.util.EnumSet;
import java.util.Properties;

public class Starter {
    public static void main(String[] args) throws Exception {
        //TODO: web.xml
        Properties props = PropertiesReader.getProperties();
        int duration = Integer.parseInt(props.getProperty("durationInSeconds"));
        DataSource dataSource = DataSourceCreator.getDataSource(props);

        PageGenerator pageGenerator = PageGenerator.instance();

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
        securityService.setUserService(userService);
        securityService.setDurationInSeconds(duration);

        //config servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);
        productsServlet.setPageGenerator(pageGenerator);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);
        addProductServlet.setPageGenerator(pageGenerator);

        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        removeProductServlet.setProductService(productService);

        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        updateProductServlet.setProductService(productService);
        updateProductServlet.setPageGenerator(pageGenerator);

        SearchProductsServlet searchProductsServlet = new SearchProductsServlet();
        searchProductsServlet.setProductService(productService);
        searchProductsServlet.setPageGenerator(pageGenerator);

        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);
        loginServlet.setDurationInSeconds(duration);

        RegistrationServlet registrationServlet = new RegistrationServlet();
        registrationServlet.setUserService(userService);
        registrationServlet.setSecurityService(securityService);
        registrationServlet.setPageGenerator(pageGenerator);

        LogOutServlet logOutServlet = new LogOutServlet();
        logOutServlet.setSecurityService(securityService);

        AddToCartServlet addToCartServlet = new AddToCartServlet();
        addToCartServlet.setProductService(productService);

        CartServlet cartServlet = new CartServlet();
        cartServlet.setPageGenerator(pageGenerator);

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
        servletContextHandler.addServlet(new ServletHolder(logOutServlet), "/logout");
        servletContextHandler.addServlet(new ServletHolder(addToCartServlet), "/product/cart");
        servletContextHandler.addServlet(new ServletHolder(cartServlet), "/products/cart");

        servletContextHandler.addFilter(new FilterHolder(securityFilter), "/*", EnumSet.of(DispatcherType.REQUEST));

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
