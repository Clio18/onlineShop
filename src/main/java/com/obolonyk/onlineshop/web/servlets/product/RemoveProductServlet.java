package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.context.Context;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = Context.getContext();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.remove(id);
        resp.sendRedirect("/products");
    }
}
