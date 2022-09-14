package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.entity.Product;
import java.time.LocalDateTime;
import java.util.List;

public class ProductService {
    private JdbcProductDao jdbcProductDao;

    public List<Product> getAllProducts() {
        List<Product> productList;
        productList = jdbcProductDao.getAll();
        return productList;
    }

    public Product getProductById(int id) {
        Product product;
        product = jdbcProductDao.getById(id);
        return product;
    }

    public void save(Product product) {
        LocalDateTime date = LocalDateTime.now();
        product.setCreationDate(date);
        jdbcProductDao.save(product);
    }

    public void remove(int id) {
        jdbcProductDao.remove(id);
    }

    public void update(Product product) {
        LocalDateTime date = LocalDateTime.now();
        product.setCreationDate(date);
        jdbcProductDao.update(product);
    }

    public List<Product> getBySearch(String pattern) {
        List<Product> productList;
        productList = jdbcProductDao.getBySearch(pattern);
        return productList;
    }

    public void setJdbcProductDao(JdbcProductDao jdbcProductDao) {
        this.jdbcProductDao = jdbcProductDao;
    }
}
