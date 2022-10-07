package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddToCartServlet extends HttpServlet {
    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        ProductService productService = applicationContext.getBean(ProductService.class);
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            Session session = (Session) req.getAttribute("session");
            List<Order> cart = session.getCart();
            if (cart==null){
                cart = new CopyOnWriteArrayList<>();
                session.setCart(cart);
            }
            Product product = optionalProduct.get();
            CartService cartService = applicationContext.getBean(CartService.class);
            cartService.addToCart(product, cart);
        }
        resp.sendRedirect("/products");
    }
}
