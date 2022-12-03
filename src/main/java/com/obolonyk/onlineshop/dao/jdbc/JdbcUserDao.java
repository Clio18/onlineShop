package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.dao.jdbc.rowmapper.UserRowMapper;
import com.obolonyk.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SELECT_BY_LOGIN = "SELECT id, name, last_name, login, email, password, salt, role FROM users WHERE login = ?;";
    private static final String SAVE = "INSERT INTO users (name, last_name, login, email, password, salt, role) VALUES (?, ?, ?, ?, ?, ?, 'USER');";

    private static final RowMapper<User> rowMapper = new UserRowMapper();

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getByLogin(String login) {
        return jdbcTemplate.queryForObject(SELECT_BY_LOGIN, rowMapper, login);
    }

    //TODO: Named parameters
    @Override
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
