package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAll();
    Product getById(int id);
    void save(Product product);
    void remove(int id);
    void update(Product product);
    List<Product> getBySearch(String pattern);

}
