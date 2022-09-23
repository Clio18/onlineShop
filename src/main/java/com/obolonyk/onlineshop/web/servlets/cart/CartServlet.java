package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartServlet extends HttpServlet {
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private static final CartService cartService = ServiceLocator.getService(CartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();
        double totalPrice = cartService.getTotalPrice(orders);
        paramMap.put("orders", orders);
        paramMap.put("totalPrice", totalPrice);
        String page = pageGenerator.getPage("templates/cart.html", paramMap);
        resp.getWriter().write(page);
    }
}
