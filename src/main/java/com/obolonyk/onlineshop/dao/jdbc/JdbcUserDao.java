package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.dao.rowmapper.UserRowMapper;
import com.obolonyk.onlineshop.entity.User;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@AllArgsConstructor
public class JdbcUserDao implements UserDao {
    private static final String SELECT_BY_LOGIN = "SELECT id, name, last_name, login, email, password, salt, role FROM users WHERE login = ?;";
    private static final String SAVE = "INSERT INTO users (name, last_name, login, email, password, salt) VALUES (?, ?, ?, ?, ?, ?);";

    private DataSource dataSource;

    @Override
    public Optional<User> getByLogin(String login) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_LOGIN)) {

                preparedStatement.setString(1, login);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(UserRowMapper.mapRow(resultSet));
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getSalt());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
