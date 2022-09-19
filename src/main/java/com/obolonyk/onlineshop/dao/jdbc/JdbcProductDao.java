package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.dao.ProductDao;
import com.obolonyk.onlineshop.dao.rowmapper.ProductRowMapper;
import com.obolonyk.onlineshop.entity.Product;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class JdbcProductDao implements ProductDao {
    private static final String SELECT_ALL = "SELECT id, name, price, creation_date, description FROM products;";
    private static final String SELECT_BY_ID = "SELECT id, name, price, creation_date, description FROM products WHERE id = ?;";
    private static final String SAVE = "INSERT INTO products (name, price, creation_date, description) VALUES (?, ?, ?, ?);";
    private static final String DELETE = "DELETE FROM Products WHERE id = ?;";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? where id = ?;";
    private static final String SEARCH = "SELECT id, name, price, creation_date, description FROM products WHERE name ilike ? OR description ilike ?;";

    private DataSource dataSource;

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

                while (resultSet.next()) {
                    products.add(ProductRowMapper.mapRow(resultSet));
                }
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> getById(int id) {
        Product product = Product.builder().build();
        try {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new RuntimeException();
                    }
                    product = ProductRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            new RuntimeException(e);
        }
        return Optional.of(product);
    }

    @Override
    public void save(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {

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

    @Override
    public void remove(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setLong(4, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getBySearch(String pattern) {
        List<Product> products = new ArrayList<>();
        try {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SEARCH)) {

                preparedStatement.setString(1, "%" + pattern + "%");
                preparedStatement.setString(2, "%" + pattern + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        products.add(ProductRowMapper.mapRow(resultSet));
                    }
                }
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
