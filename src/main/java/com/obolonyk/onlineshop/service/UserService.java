package com.obolonyk.onlineshop.service;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserDao jdbcUserDao;

    @Autowired
    public UserService(UserDao jdbcUserDao) {
        this.jdbcUserDao = jdbcUserDao;
    }

    public Optional<User> getByLogin(String login) {
        User user = jdbcUserDao.getByLogin(login);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public void save(User user) {
        jdbcUserDao.save(user);
    }
}
