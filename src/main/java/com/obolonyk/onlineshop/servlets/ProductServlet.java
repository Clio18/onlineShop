package com.obolonyk.onlineshop.servlets;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);
        List<Product> products = productService.getAllProducts();
        paramMap.put("products", products);
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("products.html", paramMap);
        resp.getWriter().write(page);
    }
}
