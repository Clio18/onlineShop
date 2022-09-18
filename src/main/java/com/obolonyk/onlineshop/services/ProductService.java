package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.ProductDao;
import com.obolonyk.onlineshop.entity.Product;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Setter
public class ProductService {
    private ProductDao jdbcProductDao;

    public List<Product> getAllProducts() {
        return jdbcProductDao.getAll();
    }

    public Optional<Product> getProductById(int id) {
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
