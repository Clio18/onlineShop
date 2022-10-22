package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.security.entity.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(path = "/product/cart", method = RequestMethod.POST)
    protected String addToCartPost(@RequestParam Integer id,
                                   HttpServletRequest req) {

        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            Session session = (Session) req.getAttribute("session");
            List<Order> cart = session.getCart();
            if (cart == null) {
                cart = new CopyOnWriteArrayList<>();
                session.setCart(cart);
            }
            Product product = optionalProduct.get();
            cartService.addToCart(product, cart);
        }
        return "redirect:/products";
    }

    @RequestMapping(path = "/products/cart", method = RequestMethod.GET)
    protected String getCartGet(HttpServletRequest req, ModelMap model) {

        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();

        //we need this check if we want to see our cart just after login in
        // because the orders will be formed after add to cart
        if (orders == null) {
            orders = new ArrayList<>(1);
        }

        double totalPrice = cartService.getTotalPrice(orders);
        model.addAttribute("orders", orders);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @RequestMapping(path = "/products/cart/delete", method = RequestMethod.POST)
    protected String deleteFromCartPost(@RequestParam Long id,
                                        HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, DELETE);
        return "redirect:/products/cart";
    }

    @RequestMapping(path = "/products/cart/update/minus", method = RequestMethod.POST)
    protected String updateCartMinusPost(@RequestParam Long id,
                                         HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, MINUS);
        return "redirect:/products/cart";
    }

    @RequestMapping(path = "/products/cart/update/plus", method = RequestMethod.POST)
    protected String updateCartPlusPost(@RequestParam Long id,
                                        HttpServletRequest req) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, PLUS);
        return "redirect:/products/cart";
    }
}
