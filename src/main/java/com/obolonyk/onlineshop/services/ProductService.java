package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.JdbcProductDao;
import com.obolonyk.onlineshop.entity.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private JdbcProductDao jdbcProductDao;

    public List<Product> getAllProducts() {
        List<Product> productList;
        try {
            productList = jdbcProductDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public Product getProductById(int id) {
        Product product;
        try {
            product = jdbcProductDao.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public void save(Product product) {
        jdbcProductDao.save(product);
    }

    public void remove(int id) {
        jdbcProductDao.remove(id);
    }

    public void update(Product product) {
        jdbcProductDao.update(product);
    }

    public List<Product> getBySearch(String pattern) {
        List<Product> productList;
        try {
            productList = jdbcProductDao.getBySearch(pattern);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    public void setJdbcProductDao(JdbcProductDao jdbcProductDao) {
        this.jdbcProductDao = jdbcProductDao;
    }
}
