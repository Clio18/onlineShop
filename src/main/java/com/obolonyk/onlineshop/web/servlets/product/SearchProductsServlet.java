package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
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
    private ProductService productService = ServiceLocator.getService(ProductService.class);
    private PageGenerator pageGenerator = ServiceLocator.getService(PageGenerator.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("search");
        List<Product> bySearch = productService.getBySearch(parameter);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("products", bySearch);
        String page = pageGenerator.getPage("templates/products.html", paramMap);
        resp.getWriter().write(page);
    }
}
