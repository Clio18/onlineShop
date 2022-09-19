package com.obolonyk.onlineshop;

import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.web.security.filters.AdminFilter;
import com.obolonyk.onlineshop.web.security.filters.UserFilter;
import com.obolonyk.onlineshop.web.servlets.auth.LogOutServlet;
import com.obolonyk.onlineshop.web.servlets.auth.LoginServlet;
import com.obolonyk.onlineshop.web.servlets.auth.RegistrationServlet;
import com.obolonyk.onlineshop.web.servlets.cart.*;
import com.obolonyk.onlineshop.web.servlets.product.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Starter {
    public static void main(String[] args) throws Exception {
        //TODO: web.xml
        Flyway flyway = ServiceLocator.getService(Flyway.class);
        flyway.migrate();

        //config servlets
        ProductsServlet productsServlet = new ProductsServlet();
        AddProductServlet addProductServlet = new AddProductServlet();
        RemoveProductServlet removeProductServlet = new RemoveProductServlet();
        UpdateProductServlet updateProductServlet = new UpdateProductServlet();
        SearchProductsServlet searchProductsServlet = new SearchProductsServlet();
        LoginServlet loginServlet = new LoginServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();
        LogOutServlet logOutServlet = new LogOutServlet();
        AddToCartServlet addToCartServlet = new AddToCartServlet();
        CartServlet cartServlet = new CartServlet();
        UpdatePlusCartServlet updatePlusCartServlet = new UpdatePlusCartServlet();
        UpdateMinusCartServlet updateMinusCartServlet = new UpdateMinusCartServlet();
        DeleteCartServlet deleteCartServlet = new DeleteCartServlet();

        //config filters
        AdminFilter adminFilter = new AdminFilter();
        UserFilter userFilter = new UserFilter();

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
        servletContextHandler.addServlet(new ServletHolder(updatePlusCartServlet), "/products/cart/update/plus");
        servletContextHandler.addServlet(new ServletHolder(updateMinusCartServlet), "/products/cart/update/minus");
        servletContextHandler.addServlet(new ServletHolder(deleteCartServlet), "/products/cart/delete");

        servletContextHandler.addFilter(new FilterHolder(userFilter), "/products", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(userFilter), "/products/search", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(userFilter), "/logout", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(userFilter), "/products/cart/*", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(userFilter), "/product/cart", EnumSet.of(DispatcherType.REQUEST));

        servletContextHandler.addFilter(new FilterHolder(adminFilter), "/products/delete", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(adminFilter), "/products/update", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(adminFilter), "/products/add", EnumSet.of(DispatcherType.REQUEST));

        //config server
        Server server = new Server(8085);
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
