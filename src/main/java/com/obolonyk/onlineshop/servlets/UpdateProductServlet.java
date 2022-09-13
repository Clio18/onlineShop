package com.obolonyk.onlineshop.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Setter
public class UpdateProductServlet extends HttpServlet {
    private ProductService productService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product product = productService.getProductById(id);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("product", product);
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("templates/updateProduct.html", paramMap);
        resp.getWriter().write(page);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            double price = Double.parseDouble(req.getParameter("price"));
            LocalDateTime date = LocalDateTime.now();
            Product product = Product.builder()
                    .id(id)
                    .creationDate(date)
                    .price(price)
                    .description(description)
                    .name(name)
                    .build();
            productService.update(product);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
