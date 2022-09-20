package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;

import java.util.List;

public class CartService {
    public int getTotalProductCount(List<Order> cart) {
        int count = 0;
        for (Order order : cart) {
            count = count + order.getQuantity();
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

    public void addToCart(Product product, List<Order> cart) {
        for (Order order : cart) {
            if (order.getProduct().getName().equals(product.getName())) {
                int quantity = order.getQuantity() + 1;
                double total = quantity * product.getPrice();
                order.setQuantity(quantity);
                order.setTotal(total);
                return;
            }
        }
        Order order = Order.builder()
                .product(product)
                .quantity(1)
                .total(product.getPrice())
                .build();
        cart.add(order);
    }
}
