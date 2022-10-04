package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.context.SingletonContextWrapper;
import com.obolonyk.onlineshop.web.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateProductServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = SingletonContextWrapper.getContext();
    private static final PageGenerator pageGenerator = PageGenerator.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductService productService = applicationContext.getBean(ProductService.class);
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("product", product);
            String page = pageGenerator.getPage("templates/updateProduct.html", paramMap);
            resp.getWriter().write(page);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        Product product = Product.builder()
                .id(id)
                .price(price)
                .description(description)
                .name(name)
                .build();
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.update(product);
        resp.sendRedirect("/products");
    }
}
