package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveProductServlet extends HttpServlet {
    private static final ProductService productService = ServiceLocator.getService(ProductService.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        productService.remove(id);
        resp.sendRedirect("/products");
    }
}
