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
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;


    @RequestMapping(path = "/products", method = RequestMethod.GET)
    protected String getProduct(HttpServletRequest req, ModelMap model) {

        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        int count = cartService.getTotalProductCount(cart);
        model.addAttribute("count", count);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @RequestMapping(path = "/products/add", method = RequestMethod.GET)
    protected String addProductGet() {
        return "addProduct";
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
    protected String updateProductGet(@RequestParam Integer id, ModelMap model) {

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "updateProduct";
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
    protected String searchProductPost(@RequestParam String search,
                                       HttpServletRequest req,
                                       ModelMap model) {

        List<Product> bySearch = productService.getBySearch(search);
        Session session = (Session) req.getAttribute("session");
        List<Order> cart = session.getCart();
        int count = cartService.getTotalProductCount(cart);
        model.addAttribute("count", count);
        model.addAttribute("products", bySearch);
        return "products";
    }

    @RequestMapping(path = "/products/delete", method = RequestMethod.POST)
    protected String deleteProductPost(@RequestParam Integer id) {

        productService.remove(id);
        return "redirect:/products";
    }
}
