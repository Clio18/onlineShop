package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Session;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UpdateMinusCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        for (Order order : cart) {
            if (order.getProduct().getId()==id){
                int quantity = order.getQuantity();
                int newQuantity = quantity-1;
                if (newQuantity!=0) {
                    double total = order.getTotal();
                    double newTotal = total - order.getProduct().getPrice();

                    order.setQuantity(newQuantity);
                    order.setTotal(newTotal);
                } else {
                    cart.remove(order);
                }
                break;
            }
        }
        resp.sendRedirect("/products/cart");
    }
}
