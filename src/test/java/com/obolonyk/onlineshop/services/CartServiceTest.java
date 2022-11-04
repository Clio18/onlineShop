package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("GetTotalProductCount Method And Check TotalProductCount")
    void testGetTotalProductCount() {
        int totalProductCount = cartService.getTotalProductsCount(cart);
        assertEquals(3, totalProductCount);
    }

    @Test
    @DisplayName("GetTotalProductCount Method And Check TotalProductCount On Empty Cart")
    void testGetTotalProductCountOnEmptyCart() {
        int totalProductCount = cartService.getTotalProductsCount(new ArrayList<>());
        assertEquals(0, totalProductCount);
    }

    @Test
    @DisplayName("GetTotalPrice Method And Check TotalPrice")
    void testGetTotalPrice() {
        double totalPrice = cartService.getTotalProductsPrice(cart);
        assertEquals(30.0, totalPrice);
    }

    @Test
    @DisplayName("GetTotalPrice Method And Check TotalPrice On Empty Cart")
    void testGetTotalPriceOnEmptyCart() {
        double totalPrice = cartService.getTotalProductsPrice(new ArrayList<>());
        assertEquals(0.0, totalPrice);
    }

    @Test
    @DisplayName("DecreasingByOneCart And Check Size Of Cart And Total Products Count Before And After")
    void testDecreasingByOneCart() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductsCount(cart);

        cartService.decreasingByOneCart(cart, 1L);

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductsCount(cart);

        assertEquals(sizeBefore - 1, sizeAfter);
        assertEquals(totalProductCountBefore - 1, totalProductCountAfter);
    }

    @Test
    @DisplayName("IncreasingByOneCart And Check Size Of Cart And Total Products Count Before And After")
    void testIncreasingByOneCart() {
        int totalProductCountBefore = cartService.getTotalProductsCount(cart);
        cartService.increasingByOneInCart(cart, 1L);
        int totalProductCountAfter = cartService.getTotalProductsCount(cart);
        assertEquals(totalProductCountBefore + 1, totalProductCountAfter);
    }

    @Test
    @DisplayName("DeleteChosenProductFromCart And Check Size Of Cart And Total Products Count Before And After")
    void testDeleteChosenProductFromCart() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductsCount(cart);
        cartService.deleteChosenProductFromCart(cart, 1L);
        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductsCount(cart);
        assertEquals(sizeBefore - 1, sizeAfter);
        assertEquals(totalProductCountBefore - 1, totalProductCountAfter);
    }

    @Test
    @DisplayName("AddChosenProductToCart An Existing Product And Check Size Of Cart And Total Products Count Before And After")
    void testAddChosenProductToCartExistingProduct() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductsCount(cart);
        Product product = Product.builder()
                .id(1)
                .description("Good")
                .price(10.0)
                .name("Bear")
                .creationDate(LocalDateTime.now())
                .build();
        cartService.addChosenProductToCart(product, cart);

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductsCount(cart);

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(totalProductCountBefore + 1, totalProductCountAfter);
    }

    @Test
    @DisplayName("AddChosenProductToCart A New Product And Check Size Of Cart And Total Products Count Before And After")
    void testAddToCartNewProduct() {
        int sizeBefore = cart.size();
        int totalProductCountBefore = cartService.getTotalProductsCount(cart);
        Product product = Product.builder()
                .id(1)
                .description("Good")
                .price(10.0)
                .name("Bear 3")
                .creationDate(LocalDateTime.now())
                .build();
        cartService.addChosenProductToCart(product, cart);

        int sizeAfter = cart.size();
        int totalProductCountAfter = cartService.getTotalProductsCount(cart);

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(totalProductCountBefore + 1, totalProductCountAfter);
    }

}
