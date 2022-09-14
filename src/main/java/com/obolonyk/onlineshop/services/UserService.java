package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.JdbcUserDao;
import com.obolonyk.onlineshop.entity.User;
import lombok.Setter;

import java.util.Optional;

@Setter
public class UserService {
    private JdbcUserDao jdbcUserDao;

    public Optional<User> getByLogin(String login) {
        return jdbcUserDao.getByLogin(login);
    }

    public void save(User user) {
        jdbcUserDao.save(user);
    }
}
