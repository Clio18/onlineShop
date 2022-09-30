package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.context.Context;
import com.obolonyk.onlineshop.web.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartServlet extends HttpServlet {
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private ApplicationContext applicationContext = Context.getContext();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();
        CartService cartService = (CartService) applicationContext.getBean("cartService");
        double totalPrice = cartService.getTotalPrice(orders);
        paramMap.put("orders", orders);
        paramMap.put("totalPrice", totalPrice);
        String page = pageGenerator.getPage("templates/cart.html", paramMap);
        resp.getWriter().write(page);
    }
}
