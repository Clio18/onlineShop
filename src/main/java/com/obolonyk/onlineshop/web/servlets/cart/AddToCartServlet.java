package com.obolonyk.onlineshop.web.servlets.cart;

import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Setter
public class AddToCartServlet extends HttpServlet {
    private ProductService productService = ServiceLocator.getService(ProductService.class);
    private CartService cartService = ServiceLocator.getService(CartService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Optional<Product> optionalProduct = productService.getProductById(id);
            if (optionalProduct.isPresent()) {
                Session session = (Session) req.getAttribute("session");
                List<Order> cart = session.getCart();
                Product product = optionalProduct.get();
                cartService.addToCart(product, cart);
            }
            resp.sendRedirect("/products");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
