package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Setter
public class UpdatePlusCartServlet extends HttpServlet {
private CartService cartService = ServiceLocator.getService(CartService.class);
private static final String  PLUS = "plus";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, PLUS);
        resp.sendRedirect("/products/cart");
    }
}
