package com.obolonyk.onlineshop.servlets;

import com.obolonyk.onlineshop.services.ProductService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            productService.remove(id);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
