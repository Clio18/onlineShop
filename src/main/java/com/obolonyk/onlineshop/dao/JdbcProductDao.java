package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date FROM products;";

    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            ProductRowMapper productRowMapper = new ProductRowMapper();
            while (resultSet.next()){
                products.add(productRowMapper.mapRow(resultSet));
            }
        }
        return products;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:8200/shop", "postgres", "postgres");
    }
}
