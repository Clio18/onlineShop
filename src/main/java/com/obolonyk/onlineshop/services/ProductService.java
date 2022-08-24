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

    public void setJdbcProductDao(JdbcProductDao jdbcProductDao) {
        this.jdbcProductDao = jdbcProductDao;
    }
}
