package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.jdbctemplate.JDBCTemplate;
import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.dao.jdbc.rowmapper.UserRowMapper;
import com.obolonyk.onlineshop.entity.User;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Optional;

@Setter
public class JdbcUserDao implements UserDao {
    private static final String SELECT_BY_LOGIN = "SELECT id, name, last_name, login, email, password, salt, role FROM users WHERE login = ?;";
    private static final String SAVE = "INSERT INTO users (name, last_name, login, email, password, salt, role) VALUES (?, ?, ?, ?, ?, ?, 'USER');";

    private UserRowMapper userRowMapper;
    private JDBCTemplate<User> jdbcTemplate;

    @Override
    @SneakyThrows
    public Optional<User> getByLogin(String login) {
        return jdbcTemplate.queryObject(SELECT_BY_LOGIN, userRowMapper, login);
    }

    @Override
    @SneakyThrows
    public void save(User user) {
        jdbcTemplate.executeUpdate(SAVE,
                user.getName(),
                user.getLastName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getSalt());
    }
}
