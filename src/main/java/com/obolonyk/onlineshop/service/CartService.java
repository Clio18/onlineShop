package com.obolonyk.onlineshop.service;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CartService {
    private ProductService productService;

    @Autowired
    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public int getTotalProductsCount(List<Order> cart) {
        int count = 0;
        if (cart != null) {
            for (Order order : cart) {
                count = count + order.getQuantity();
            }
        }
        return count;
    }

    public double getTotalProductsPrice(List<Order> orders) {
        double totalPrice = 0;
        if (orders == null) {
            return totalPrice;
        }
        for (Order order : orders) {
            totalPrice = totalPrice + (order.getQuantity() * order.getProduct().getPrice());
        }
        return totalPrice;
    }

    public List<Order> addChosenProductToCart(Integer productId, List<Order> cart) {
        Optional<Product> optionalProduct = productService.getById(productId);
        Product product = optionalProduct.orElseThrow(() -> new RuntimeException("Producr not found"));
        Order order = Order.builder()
                .product(product)
                .quantity(1)
                .total(product.getPrice())
                .build();

        if (cart == null) {
            cart = new CopyOnWriteArrayList<>();
            cart.add(order);
        } else {
            for (Order ord : cart) {
                if (ord.getProduct().getName().equals(product.getName())) {
                    int quantity = ord.getQuantity() + 1;
                    double total = quantity * product.getPrice();
                    ord.setQuantity(quantity);
                    ord.setTotal(total);
                    return cart;
                }
            }
        }
        cart.add(order);
        return cart;
    }

    public void decreasingByOneCart(List<Order> cart, Long id) {
        CartAction.REMOVE_FROM_CART.perform(cart, id);
    }

    public void increasingByOneInCart(List<Order> cart, Long id) {
        CartAction.ADD_TO_CART.perform(cart, id);
    }

    public void deleteChosenProductFromCart(List<Order> cart, Long id) {
        CartAction.DELETE.perform(cart, id);
    }
}
