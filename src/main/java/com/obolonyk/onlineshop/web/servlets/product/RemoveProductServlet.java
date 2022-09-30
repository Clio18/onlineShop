package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.context.Context;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private ApplicationContext applicationContext = Context.getContext();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductService productService = (ProductService) applicationContext.getBean("productService");
        productService.remove(id);
        resp.sendRedirect("/products");
    }
}
