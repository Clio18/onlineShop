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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProductController {
    private static final TemplateFactory pageGenerator = PageGenerator.instance();

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;


    @RequestMapping(path = "/products", method = RequestMethod.GET)
    protected void getProduct(HttpServletRequest req,
                              HttpServletResponse resp) throws IOException {

        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        int count = cartService.getTotalProductCount(cart);
        paramMap.put("count", count);

        List<Product> products = productService.getAllProducts();
        paramMap.put("products", products);
        String page = pageGenerator.getPage("products.html", paramMap);
        resp.getWriter().write(page);
    }

    @RequestMapping(path = "/products/add", method = RequestMethod.GET)
    protected void addProductGet(HttpServletResponse resp) throws IOException {

        String page = pageGenerator.getPage("addProduct.html");
        resp.getWriter().write(page);
    }

    @RequestMapping(path = "/products/add", method = RequestMethod.POST)
    protected String addProductPost(@RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam Double price) {

        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
        productService.save(product);
        return "redirect:/products";
    }

    @RequestMapping(path = "/products/update", method = RequestMethod.GET)
    protected void updateProductGet(@RequestParam Integer id,
                                    HttpServletResponse resp) throws IOException {

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("product", product);
            String page = pageGenerator.getPage("updateProduct.html", paramMap);
            resp.getWriter().write(page);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @RequestMapping(path = "/products/update", method = RequestMethod.POST)
    protected String updateProductPost(@RequestParam Integer id,
                          @RequestParam String name,
                          @RequestParam String description,
                          @RequestParam Double price) {

        Product product = Product.builder()
                .id(id)
                .price(price)
                .description(description)
                .name(name)
                .build();
        productService.update(product);
        return "redirect:/products";
    }

    @RequestMapping(path = "/products/search", method = RequestMethod.POST)
    protected void searchProductPost( @RequestParam String search,
                                      HttpServletRequest req,
                                      HttpServletResponse resp) throws IOException {

        List<Product> bySearch = productService.getBySearch(search);
        Map<String, Object> paramMap = new HashMap<>();
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();

        int count = cartService.getTotalProductCount(cart);
        paramMap.put("count", count);
        paramMap.put("products", bySearch);
        String page = pageGenerator.getPage("products.html", paramMap);

        resp.getWriter().write(page);
    }

    @RequestMapping(path = "/products/delete", method = RequestMethod.POST)
    protected String deleteProductPost(@RequestParam Integer id) {

        productService.remove(id);
        return "redirect:/products";
    }
}
