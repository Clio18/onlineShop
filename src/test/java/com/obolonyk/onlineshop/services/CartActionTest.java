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

class CartActionTest {
    private List<Order> cart;
    private Order order1;
    private Order order2;

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

        order1 = Order.builder()
                .quantity(1)
                .total(10.0)
                .product(product1)
                .build();

        order2 = Order.builder()
                .quantity(2)
                .total(20.0)
                .product(product2)
                .build();

        cart = new ArrayList<>();
        cart.add(order1);
        cart.add(order2);
    }

    @Test
    @DisplayName("Delete In LowerCase")
    void testDeleteLowerCase() {
        CartAction delete = CartAction.of("delete");
        assertNotNull(delete);
        assertEquals("DELETE", delete.name());
    }

    @Test
    @DisplayName("Delete In UpperCase")
    void testDeleteUpperCase() {
        CartAction delete = CartAction.of("DELETE");
        assertNotNull(delete);
        assertEquals("DELETE", delete.name());
    }

    @Test
    @DisplayName("Delete With Case Ignore")
    void testDeleteCaseIgnore() {
        CartAction deleteLower = CartAction.of("delete");
        CartAction deleteUpper = CartAction.of("DELETE");
        assertEquals(deleteUpper, deleteLower);
    }

    @Test
    @DisplayName("Delete With Incorrect Command And Exception Throws")
    void testDeleteIncorrectExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.of("del");
        });
        assertEquals("Invalid action type", exception.getMessage());
    }

    @Test
    @DisplayName("AddToCart In LowerCase")
    void testAddToCartLowerCase() {
        CartAction addToCart = CartAction.of("add_to_cart");
        assertNotNull(addToCart);
        assertEquals("ADD_TO_CART", addToCart.name());
    }

    @Test
    @DisplayName("AddToCart In UpperCase")
    void testAddToCartUpperCase() {
        CartAction addToCart = CartAction.of("ADD_TO_CART");
        assertNotNull(addToCart);
        assertEquals("ADD_TO_CART", addToCart.name());
    }

    @Test
    @DisplayName("AddToCart With CaseIgnore")
    void testAddToCartCaseIgnore() {
        CartAction addToCartLower = CartAction.of("add_to_cart");
        CartAction addToCartUpper = CartAction.of("ADD_TO_CART");
        assertEquals(addToCartUpper, addToCartLower);
    }

    @Test
    @DisplayName("AddToCart With Incorrect Command And Exception Throws")
    void testAddToCartIncorrectExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.of("add");
        });
        assertEquals("Invalid action type", exception.getMessage());
    }

    @Test
    @DisplayName("RemoveFromCart in LowerCase")
    void testRemoveFromCartLowerCase() {
        CartAction removeFromCart = CartAction.of("remove_from_cart");
        assertNotNull(removeFromCart);
        assertEquals("REMOVE_FROM_CART", removeFromCart.name());
    }

    @Test
    @DisplayName("RemoveFromCart In UpperCase")
    void testRemoveFromCartUpperCase() {
        CartAction removeFromCart = CartAction.of("REMOVE_FROM_CART");
        assertNotNull(removeFromCart);
        assertEquals("REMOVE_FROM_CART", removeFromCart.name());
    }

    @Test
    @DisplayName("RemoveFromCart With CaseIgnore")
    void testRemoveFromCartCaseIgnore() {
        CartAction removeFromCartLower = CartAction.of("remove_from_cart");
        CartAction removeFromCartUpper = CartAction.of("REMOVE_FROM_CART");
        assertEquals(removeFromCartUpper, removeFromCartLower);
    }

    @Test
    @DisplayName("RemoveFromCart With Incorrect Command And Exception Throws")
    void testRemoveFromCartIncorrectExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.of("remove");
        });
        assertEquals("Invalid action type", exception.getMessage());
    }

    @Test
    @DisplayName("Delete Perform Method With Valid Id")
    void testDeletePerformValidId() {
        int before = cart.size();
        CartAction.DELETE.perform(cart, 1);
        int after = cart.size();
        assertEquals(before - 1, after);
    }

    @Test
    @DisplayName("Delete Perform Method With InValid Id")
    void testDeletePerformInValidId() {
        int invalidId = 100;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.DELETE.perform(cart, invalidId);
        });
        assertEquals("No order found by provided id " + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("AddToCart Perform Method With Valid Id")
    void testAddToCartPerformValidId() {
        int quantityBefore = order1.getQuantity();
        double totalBefore = order1.getTotal();

        CartAction.ADD_TO_CART.perform(cart, 1);

        int quantityAfter = order1.getQuantity();
        double totalAfter = order1.getTotal();

        assertEquals(quantityBefore + 1, quantityAfter);
        assertEquals(totalBefore + order1.getProduct().getPrice(), totalAfter);
    }

    @Test
    @DisplayName("AddToCart Perform Method With InValid Id")
    void testAddToCartPerformInValidId() {
        int invalidId = 100;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.ADD_TO_CART.perform(cart, invalidId);
        });
        assertEquals("No order found by provided id " + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("RemoveFromCart Perform Method With Valid Id And Same Product More Than One")
    void testRemoveFromCartPerformValidIdAndSameProductMoreThanOne() {
        int quantityBefore = order2.getQuantity();
        double totalBefore = order2.getTotal();

        CartAction.REMOVE_FROM_CART.perform(cart, 2);

        int quantityAfter = order2.getQuantity();
        double totalAfter = order2.getTotal();

        assertEquals(quantityBefore - 1, quantityAfter);
        assertEquals(totalBefore - order2.getProduct().getPrice(), totalAfter);
    }

    @Test
    @DisplayName("RemoveFromCart Perform Method With Valid Id And Product Is One")
    void testRemoveFromCartPerformValidIdAndProductIsOne() {
        int before = cart.size();
        CartAction.REMOVE_FROM_CART.perform(cart, 1);
        int after = cart.size();
        assertEquals(before - 1, after);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.REMOVE_FROM_CART.perform(cart, 1);
        });
        assertEquals("No order found by provided id " + 1, exception.getMessage());
    }

    @Test
    @DisplayName("RemoveFromCart Perform Method With InValid Id")
    void testRemoveFromCartPerformInValidId() {
        int invalidId = 100;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.REMOVE_FROM_CART.perform(cart, invalidId);
        });
        assertEquals("No order found by provided id " + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("FindOrderInCartById Method With Valid Id")
    void testFindOrderInCartByIdWithValidId(){
        Order orderInCartById = CartAction.findOrderInCartById(cart, 1);
        assertEquals(order1, orderInCartById);
    }

    @Test
    @DisplayName("FindOrderInCartById Method With InValid Id")
    void testFindOrderInCartByIdWithInValidId(){
        int invalidId = 100;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CartAction.findOrderInCartById(cart, invalidId);
        });
        assertEquals("No order found by provided id " + invalidId, exception.getMessage());
    }
}