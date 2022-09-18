package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Session;

import java.util.List;

public class CartService {
    public int getTotalProductCount(Session session) {
        int count = 0;
        if (session != null) {
            List<Order> cart = session.getCart();
            for (Order order : cart) {
                count = count + order.getQuantity();
            }
        }
        return count;
    }

    public double getTotalPrice(List<Order> orders) {
        double totalPrice = 0;
        for (Order order : orders) {
            totalPrice = totalPrice + (order.getQuantity()*order.getProduct().getPrice());
        }
        return totalPrice;
    }
}
