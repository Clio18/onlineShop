package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {
    private List<Order> cart;

    private CartService cartService = new CartService();

    @BeforeEach
    void init() {
       Product product1 = Product.builder()
                .id(1)
                .description("Good")
                .price(10.0)
                .name("Bear")
                .creationDate(LocalDateTime.now())
                .build();

        Product product2 = Product.builder()
                .id(2)
                .description("Good 2")
                .price(10.0)
                .name("Bear 2")
                .creationDate(LocalDateTime.now())
                .build();

        Order order1 = Order.builder()
                .quantity(1)
                .total(10.0)
                .product(product1)
                .build();

        Order order2 = Order.builder()
                .quantity(2)
                .total(20.0)
                .product(product2)
                .build();

        cart = new ArrayList<>();
        cart.add(order1);
        cart.add(order2);
    }

    @Test
    void testGetTotalProductCount() {
        int totalProductCount = cartService.getTotalProductCount(cart);
        assertEquals(3, totalProductCount);
    }

    @Test
    void testGetTotalProductCountOnEmptyCart() {
        int totalProductCount = cartService.getTotalProductCount(new ArrayList<>());
        assertEquals(0, totalProductCount);
    }

    @Test
    void testGetTotalPrice() {
        double totalPrice = cartService.getTotalPrice(cart);
        assertEquals(30.0, totalPrice);
    }

    @Test
    void testGetTotalPriceOnEmptyCart() {
        double totalPrice = cartService.getTotalPrice(new ArrayList<>());
        assertEquals(0.0, totalPrice);
    }

    @Test
    void testUpdateMinusOne() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductCount(cart);

        cartService.update(cart, 1, "minus");

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductCount(cart);

        assertEquals(sizeBefore-1, sizeAfter);
        assertEquals(totalProductCountBefore-1, totalProductCountAfter);
    }

    @Test
    void testUpdatePlusOne() {
        int totalProductCountBefore = cartService.getTotalProductCount(cart);
        cartService.update(cart, 1, "plus");
        int totalProductCountAfter = cartService.getTotalProductCount(cart);
        assertEquals(totalProductCountBefore+1, totalProductCountAfter);
    }

    @Test
    void testUpdateDeleteOne() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductCount(cart);

        cartService.update(cart, 1, "delete");

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductCount(cart);

        assertEquals(sizeBefore-1, sizeAfter);
        assertEquals(totalProductCountBefore-1, totalProductCountAfter);
    }

    @Test
    void testAddToCartExistingProduct(){
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductCount(cart);
        Product product = Product.builder()
                .id(1)
                .description("Good")
                .price(10.0)
                .name("Bear")
                .creationDate(LocalDateTime.now())
                .build();
        cartService.addToCart(product, cart);

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductCount(cart);

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(totalProductCountBefore+1, totalProductCountAfter);
    }

    @Test
    void testAddToCartNewProduct(){
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductCount(cart);
        Product product = Product.builder()
                .id(1)
                .description("Good")
                .price(10.0)
                .name("Bear 3")
                .creationDate(LocalDateTime.now())
                .build();
        cartService.addToCart(product, cart);

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductCount(cart);

        assertEquals(sizeBefore+1, sizeAfter);
        assertEquals(totalProductCountBefore+1, totalProductCountAfter);
    }

}
