package com.obolonyk.onlineshop.web.servlets.product;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.context.SingletonContextWrapper;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.templator.TemplateFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchProductsServlet extends HttpServlet {
    private static final ApplicationContext applicationContext = SingletonContextWrapper.getContext();
    private static final TemplateFactory pageGenerator = PageGenerator.instance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter("search");
        ProductService productService = applicationContext.getBean(ProductService.class);
        List<Product> bySearch = productService.getBySearch(parameter);
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();

        CartService cartService = applicationContext.getBean(CartService.class);
        int count = cartService.getTotalProductCount(cart);
        paramMap.put("count", count);
        paramMap.put("products", bySearch);
        String page = pageGenerator.getPage("products.html", paramMap);

        resp.getWriter().write(page);
    }
}
