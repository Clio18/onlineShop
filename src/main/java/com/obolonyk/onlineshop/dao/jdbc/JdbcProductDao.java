package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.dao.ProductDao;
import com.obolonyk.onlineshop.dao.jdbc.rowmapper.ProductRowMapper;
import com.obolonyk.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcProductDao implements ProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date, description FROM products;";
    private static final String SELECT_BY_ID = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?;";
    private static final String SAVE = "INSERT INTO products (name, price, creation_date, description) VALUES (?, ?, ?, ?);";
    private static final String DELETE = "DELETE FROM Products WHERE id = ?;";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? where id = ?;";
    private static final String SEARCH = "SELECT id, name, price, creation_date, description FROM products WHERE name ilike ? OR description ilike ?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRowMapper productRowMapper;

    @Override
    public List<Product> getAll() {
      return jdbcTemplate.query(SELECT_ALL, productRowMapper);
    }

    @Override
    public Product getById(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, productRowMapper, id);
    }

    @Override
    public void save(Product product) {
       jdbcTemplate.update(SAVE,
               product.getName(),
               product.getPrice(),
               product.getCreationDate(),
               product.getDescription());
    }

    @Override
    public void remove(int id) {
     jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(UPDATE,
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getId());
    }

    @Override
    public List<Product> getBySearch(String pattern) {
        return jdbcTemplate.query(SEARCH, productRowMapper, pattern, pattern);
    }

}
