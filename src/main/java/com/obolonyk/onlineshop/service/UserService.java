package com.obolonyk.onlineshop.service;

import com.obolonyk.onlineshop.dao.UserDao;
import com.obolonyk.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
        User user = null;

        //in case of new user with new login jdbcTemplate.queryForObject
        //will throw exception
        try {
            user = jdbcUserDao.getByLogin(login);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public void save(User user) {
        jdbcUserDao.save(user);
    }
}
