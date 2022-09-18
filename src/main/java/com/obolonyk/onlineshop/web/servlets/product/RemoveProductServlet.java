package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.services.ProductService;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class RemoveProductServlet extends HttpServlet {
    private ProductService productService;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            int id = Integer.parseInt(req.getParameter("id"));
            productService.remove(id);
            resp.sendRedirect("/products");
    }
}
