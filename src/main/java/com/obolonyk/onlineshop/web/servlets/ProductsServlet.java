package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class ProductsServlet extends HttpServlet {
    private ProductService productService;
    private PageGenerator pageGenerator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            Map<String, Object> paramMap = new HashMap<>();
            List<Product> products = productService.getAllProducts();
            paramMap.put("products", products);
            String page = pageGenerator.getPage("templates/products.html", paramMap);
            resp.getWriter().write(page);
    }
}
