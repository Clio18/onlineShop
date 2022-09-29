package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UpdateMinusCartServlet extends HttpServlet {
    private static final CartService cartService = ServiceLocator.getService(CartService.class);
    private static final String MINUS = "minus";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, MINUS);
        resp.sendRedirect("/products/cart");
    }
}
