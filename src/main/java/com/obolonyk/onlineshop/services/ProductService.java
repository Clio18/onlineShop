package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.entity.Product;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
public class ProductService {
    private JdbcProductDao jdbcProductDao;

    public List<Product> getAllProducts() {
        return jdbcProductDao.getAll();
    }

    public Product getProductById(int id) {
        return jdbcProductDao.getById(id);
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
        return jdbcProductDao.getBySearch(pattern);
    }
}
