package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private static final ProductService productService = ServiceLocator.getService(ProductService.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.remove(id);
        resp.sendRedirect("/products");
    }
}
