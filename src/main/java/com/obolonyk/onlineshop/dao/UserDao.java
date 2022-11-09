package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.User;

public interface UserDao {

    User getByLogin(String login);

    void save(User user);
}
