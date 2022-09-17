package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.ProductService;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Setter
public class AddProductToCartServlet extends HttpServlet {
    private ProductService productService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.getProductById(id);
            if (product != null) {
                Session session = (Session) req.getAttribute("session");
                List<Order> cart = session.getCart();
                for (Order order : cart) {
                    if (order.getProduct().getName().equals(product.getName())){
                        int quantity = order.getQuantity() + 1;
                        double total = quantity * product.getPrice();
                        order.setQuantity(quantity);
                        order.setTotal(total);
                        resp.sendRedirect("/products");
                        return;
                    }
                }
                Order order = Order.builder()
                        .product(product)
                        .quantity(1)
                        .total(product.getPrice())
                        .build();
                cart.add(order);
                session.setCart(cart);
            }
            resp.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
