package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.context.Context;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class UpdatePlusCartServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = Context.getContext();
    private static final String PLUS = "plus";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        CartService cartService = applicationContext.getBean(CartService.class);
        cartService.update(cart, id, PLUS);
        resp.sendRedirect("/products/cart");
    }
}
