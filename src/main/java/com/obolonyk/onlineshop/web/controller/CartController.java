package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.templator.TemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class CartController {
    private static final TemplateFactory pageGenerator = PageGenerator.instance();
    private static final String DELETE = "delete";
    private static final String MINUS = "minus";
    private static final String PLUS = "plus";


    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @RequestMapping(path = "/product/cart", method = RequestMethod.POST)
    protected void addToCartPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            Session session = (Session) req.getAttribute("session");
            List<Order> cart = session.getCart();
            if (cart==null){
                cart = new CopyOnWriteArrayList<>();
                session.setCart(cart);
            }
            Product product = optionalProduct.get();
            cartService.addToCart(product, cart);
        }
        resp.sendRedirect("/products");
    }

    @RequestMapping(path = "/products/cart", method = RequestMethod.GET)
    protected void getCartGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> orders = session.getCart();
        double totalPrice = cartService.getTotalPrice(orders);
        paramMap.put("orders", orders);
        paramMap.put("totalPrice", totalPrice);
        String page = pageGenerator.getPage("cart.html", paramMap);
        resp.getWriter().write(page);
    }

    @RequestMapping(path = "/products/cart/delete", method = RequestMethod.POST)
    protected void deleteFromCartPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, DELETE);
        resp.sendRedirect("/products/cart");
    }

    @RequestMapping(path = "/products/cart/update/minus", method = RequestMethod.POST)
    protected void updateCartMinusPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, MINUS);
        resp.sendRedirect("/products/cart");
    }

    @RequestMapping(path = "/products/cart/update/plus", method = RequestMethod.POST)
    protected void updateCartPlusPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        cartService.update(cart, id, PLUS);
        resp.sendRedirect("/products/cart");
    }
}
