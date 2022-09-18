package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class CartServlet extends HttpServlet {
    private PageGenerator pageGenerator;
    private CartService cartService;

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