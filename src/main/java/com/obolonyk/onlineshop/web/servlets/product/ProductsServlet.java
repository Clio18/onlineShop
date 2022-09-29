package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.web.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsServlet extends HttpServlet {
    private static final ProductService productService = ServiceLocator.getService(ProductService.class);
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private static final CartService cartService = ServiceLocator.getService(CartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        int count = cartService.getTotalProductCount(cart);
        paramMap.put("count", count);
        List<Product> products = productService.getAllProducts();
        paramMap.put("products", products);
        String page = pageGenerator.getPage("templates/products.html", paramMap);
        resp.getWriter().write(page);
    }
}
