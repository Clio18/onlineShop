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

@Setter
public class AddProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        String page = pageGenerator.getPage("templates/addProduct.html", null);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
       try{ String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        LocalDateTime date = LocalDateTime.now();
        Product product = Product.builder()
                .name(name)
                .price(price)
                .creationDate(date)
                .build();
        productService.save(product);
        resp.sendRedirect("/products");
       } catch (Exception e){
           throw new RuntimeException(e);
       }
    }
}
