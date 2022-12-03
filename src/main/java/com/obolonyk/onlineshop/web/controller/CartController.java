package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.service.CartService;
import com.obolonyk.onlineshop.security.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(path = "/product/cart")
    protected String addToCartPost(@RequestParam Integer productId,
                                   HttpServletRequest req) {


        Session session = (Session) req.getAttribute("session");
        List<Order> cart = cartService.addChosenProductToCart(productId, session.getCart());
        session.setCart(cart);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/cart")
    protected String getCartGet(HttpServletRequest req, ModelMap model) {

        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();

        //we need this check if we want to see our cart just after login in
        // because the orders will be formed after add to cart
        if (orders == null) {
            //TODO: ??
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
