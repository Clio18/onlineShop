package com.obolonyk.onlineshop.servlets;

import com.obolonyk.onlineshop.services.ProductService;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Setter
public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;

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
