package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JdbcProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date, description FROM products;";
    private static final String SELECT_BY_ID = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?;";
    private static final String INSERT = "INSERT INTO products (name, price, creation_date, description) VALUES (?, ?, ?, ?);";
    private static final String DELETE = "DELETE FROM Products WHERE id = ?;";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, creation_date = ?, description = ? where id = ?;";
    private static final String SEARCH = "SELECT id, name, price, creation_date, description FROM products WHERE name ilike ? OR description ilike ?;";

    private DataSource dataSource;

    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
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
        try (Connection connection = dataSource.getConnection();
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT);) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            LocalDateTime localDateTime = product.getCreationDate();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            LocalDateTime localDateTime = LocalDateTime.now();
            preparedStatement.setTimestamp(3, Timestamp.valueOf(localDateTime));
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.setLong(5, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getBySearch(String pattern) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH)) {
            preparedStatement.setString(1, "%"+pattern+"%");
            preparedStatement.setString(2, "%"+pattern+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ProductRowMapper productRowMapper = new ProductRowMapper();
            while (resultSet.next()) {
                products.add(productRowMapper.mapRow(resultSet));
            }
        }
        return products;
    }

}
