package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.jdbctemplate.JDBCTemplate;
import com.obolonyk.onlineshop.dao.ProductDao;
import com.obolonyk.onlineshop.dao.jdbc.rowmapper.ProductRowMapper;
import com.obolonyk.onlineshop.entity.Product;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

@Setter
public class JdbcProductDao implements ProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date, description FROM products;";
    private static final String SELECT_BY_ID = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?;";
    private static final String SAVE = "INSERT INTO products (name, price, creation_date, description) VALUES (?, ?, ?, ?);";
    private static final String DELETE = "DELETE FROM Products WHERE id = ?;";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? where id = ?;";
    private static final String SEARCH = "SELECT id, name, price, creation_date, description FROM products WHERE name ilike ? OR description ilike ?;";

    private ProductRowMapper productRowMapper;
    private JDBCTemplate<Product> jdbcTemplate;

    @Override
    @SneakyThrows
    public List<Product> getAll() {
        return jdbcTemplate.query(SELECT_ALL, productRowMapper);
    }

    @Override
    @SneakyThrows
    public Optional<Product> getById(int id) {
        return jdbcTemplate.queryObject(SELECT_BY_ID, productRowMapper, id);
    }

    @Override
    @SneakyThrows
    public void save(Product product) {
        jdbcTemplate.executeUpdate(SAVE,
                product.getName(),
                product.getPrice(),
                product.getCreationDate(),
                product.getDescription());
    }

    @Override
    @SneakyThrows
    public void remove(int id) {
       jdbcTemplate.executeUpdate(DELETE, id);
    }

    @Override
    @SneakyThrows
    public void update(Product product) {
       jdbcTemplate.executeUpdate(UPDATE,
               product.getName(),
               product.getPrice(),
               product.getDescription(),
               product.getId());
    }

    @Override
    @SneakyThrows
    public List<Product> getBySearch(String pattern) {
        return jdbcTemplate.query(SEARCH, productRowMapper, pattern, pattern);
    }

}
