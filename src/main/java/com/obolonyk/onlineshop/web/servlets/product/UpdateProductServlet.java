package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.templator.TemplateFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateProductServlet extends HttpServlet {
    private static final TemplateFactory pageGenerator = PageGenerator.instance();
    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductService productService = applicationContext.getBean(ProductService.class);
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("product", product);
            String page = pageGenerator.getPage("updateProduct.html", paramMap);
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
