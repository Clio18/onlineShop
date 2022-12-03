package com.obolonyk.onlineshop.security.service;

import com.obolonyk.onlineshop.security.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SecurityService {

    void logOut(List<Session> sessionList);

    Optional<Session> login(String password, String login);

    Session getSession(String token);

    void saveUser(String password, String login, String name, String email, String lastName);
}
