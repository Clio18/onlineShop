package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductServlet extends HttpServlet {
    private static final ProductService productService = ServiceLocator.getService(ProductService.class);
    private static final PageGenerator pageGenerator = PageGenerator.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("templates/addProduct.html", null);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
        productService.save(product);
        resp.sendRedirect("/products");
    }
}
