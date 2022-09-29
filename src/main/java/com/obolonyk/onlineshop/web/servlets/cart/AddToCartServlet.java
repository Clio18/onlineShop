package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AddToCartServlet extends HttpServlet {
    private static final ProductService productService = ServiceLocator.getService(ProductService.class);
    private static final CartService cartService = ServiceLocator.getService(CartService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            Session session = (Session) req.getAttribute("session");
            List<Order> cart = session.getCart();
            Product product = optionalProduct.get();
            cartService.addToCart(product, cart);
        }
        resp.sendRedirect("/products");
    }
}
