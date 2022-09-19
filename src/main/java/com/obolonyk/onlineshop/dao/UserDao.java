package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getByLogin(String login);

    void save(User user);
}
