package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class DeleteCartServlet extends HttpServlet {
    private static final String DELETE = "delete";
    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        CartService cartService = applicationContext.getBean(CartService.class);
        cartService.update(cart, id, DELETE);
        resp.sendRedirect("/products/cart");
    }
}
