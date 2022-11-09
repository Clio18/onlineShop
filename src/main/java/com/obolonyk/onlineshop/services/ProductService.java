package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.ProductDao;
import com.obolonyk.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDao jdbcProductDao;

    public List<Product> getAll() {
        return jdbcProductDao.getAll();
    }

    public Optional<Product> getById(int id) {
        Product product = jdbcProductDao.getById(id);
        if(product==null){
            return Optional.empty();
        }
        return Optional.of(product);
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
