package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.security.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class CartController {
    private static final String DELETE = "delete";
    private static final String MINUS = "minus";
    private static final String PLUS = "plus";

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @PostMapping(path = "/product/cart")
    protected String addToCartPost(@RequestParam Integer id,
                                   HttpServletRequest req) {

        Optional<Product> optionalProduct = productService.getById(id);

        if (optionalProduct.isPresent()) {
            Session session = (Session) req.getAttribute("session");
            List<Order> cart = session.getCart();
            if (cart == null) {
                cart = new CopyOnWriteArrayList<>();
                session.setCart(cart);
            }
            Product product = optionalProduct.get();
            cartService.addChosenProductToCart(product, cart);
        }
        return "redirect:/products";
    }

    @GetMapping(path = "/products/cart")
    protected String getCartGet(HttpServletRequest req, ModelMap model) {

        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();

        //we need this check if we want to see our cart just after login in
        // because the orders will be formed after add to cart
        if (orders == null) {
            orders = new ArrayList<>(1);
        }

        double totalPrice = cartService.getTotalProductsPrice(orders);
        model.addAttribute("orders", orders);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping(path = "/products/cart/delete")
    protected String deleteFromCartPost(@RequestParam Long id,
                                        HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.deleteChosenProductFromCart(cart, id);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/minus")
    protected String updateCartMinusPost(@RequestParam Long id,
                                         HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.decreasingByOneCart(cart, id);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/plus")
    protected String updateCartPlusPost(@RequestParam Long id,
                                        HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.increasingByOneInCart(cart, id);
        return "redirect:/products/cart";
    }
}
