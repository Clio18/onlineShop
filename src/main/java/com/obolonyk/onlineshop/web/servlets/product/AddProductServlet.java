package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.context.SingletonContextWrapper;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.templator.TemplateFactory;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddProductServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = SingletonContextWrapper.getContext();
    private static final TemplateFactory pageGenerator = PageGenerator.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("addProduct.html");
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
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.save(product);
        resp.sendRedirect("/products");
    }
}
