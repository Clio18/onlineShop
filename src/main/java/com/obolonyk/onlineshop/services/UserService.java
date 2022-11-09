package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao jdbcUserDao;

    public Optional<User> getByLogin(String login) {
        return jdbcUserDao.getByLogin(login);
    }

    public void save(User user) {
        jdbcUserDao.save(user);
    }
}
