package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date FROM products;";
    private static final String SELECT_BY_ID = "SELECT id, name, price, creation_date FROM products WHERE id = ?;";
    private static final String INSERT = "INSERT INTO products (name, price, creation_date) VALUES (?, ?, ?);";

    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            ProductRowMapper productRowMapper = new ProductRowMapper();
            while (resultSet.next()) {
                products.add(productRowMapper.mapRow(resultSet));
            }
        }
        return products;
    }

    public Product getById(int id) throws SQLException {
        Product product = Product.builder().build();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ProductRowMapper productRowMapper = new ProductRowMapper();
            while (resultSet.next()) {
                product = productRowMapper.mapRow(resultSet);
            }
        }
        return product;
    }

    public void save(Product product) {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
        ){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            LocalDateTime localDateTime = product.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:8200/shop", "postgres", "postgres");
    }


}
