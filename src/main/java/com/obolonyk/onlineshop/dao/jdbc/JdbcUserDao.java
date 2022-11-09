package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.dao.jdbc.rowmapper.UserRowMapper;
import com.obolonyk.onlineshop.entity.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SELECT_BY_LOGIN = "SELECT id, name, last_name, login, email, password, salt, role FROM users WHERE login = ?;";
    private static final String SAVE = "INSERT INTO users (name, last_name, login, email, password, salt, role) VALUES (?, ?, ?, ?, ?, ?, 'USER');";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRowMapper userRowMapper;

    @Override
    @SneakyThrows
    public User getByLogin(String login) {
        return jdbcTemplate.queryForObject(SELECT_BY_LOGIN, userRowMapper, login);
    }

    @Override
    @SneakyThrows
    public void save(User user) {
        jdbcTemplate.update(SAVE,
                user.getName(),
                user.getLastName(),
                user.getLogin(),
                user.getEmail(),
                user.getPassword(),
                user.getSalt());
    }
}
