package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
public class UpdateProductServlet extends HttpServlet {
    private ProductService productService = ServiceLocator.getService(ProductService.class);
    private PageGenerator pageGenerator = PageGenerator.instance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Product> productOptional = productService.getProductById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("product", product);
            String page = pageGenerator.getPage("templates/updateProduct.html", paramMap);
            resp.getWriter().write(page);
        } else {
            throw new RemoteException("Product not found");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
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
            productService.update(product);
            resp.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
