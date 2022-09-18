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
            totalPrice = totalPrice + (order.getQuantity() * order.getProduct().getPrice());
        }
        return totalPrice;
    }

    public void update(List<Order> cart, long id, String action) {
        for (Order order : cart) {
            if (order.getProduct().getId() == id) {
                int quantity = order.getQuantity();
                double total = order.getTotal();
                int newQuantity = 0;
                double newTotal = 0;

                if (action.equals("minus")) {
                    newQuantity = quantity - 1;
                    if (newQuantity == 0) {
                        cart.remove(order);
                        return;
                    }
                    newTotal = total - order.getProduct().getPrice();
                } else if (action.equals("plus")) {
                    newQuantity = quantity + 1;
                    newTotal = total + order.getProduct().getPrice();
                }

                order.setQuantity(newQuantity);
                order.setTotal(newTotal);

                if (action.equals("delete")) {
                    cart.remove(order);
                }
                break;
            }
        }
    }
}
