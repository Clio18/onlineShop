package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.context.Context;
import com.obolonyk.onlineshop.web.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = Context.getContext();
    private static final PageGenerator pageGenerator = PageGenerator.instance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        CartService cartService = applicationContext.getBean(CartService.class);
        int count = cartService.getTotalProductCount(cart);
        paramMap.put("count", count);

        ProductService productService = applicationContext.getBean(ProductService.class);
        List<Product> products = productService.getAllProducts();
        paramMap.put("products", products);
        String page = pageGenerator.getPage("templates/products.html", paramMap);

        resp.getWriter().write(page);
    }
}
