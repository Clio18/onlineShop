package com.obolonyk.onlineshop.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class SearchProductsServlet extends HttpServlet {
    private ProductService productService;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("search");
        List<Product> bySearch = productService.getBySearch(parameter);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("products", bySearch);
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("templates/products.html", paramMap);
        resp.getWriter().write(page);
    }
}
