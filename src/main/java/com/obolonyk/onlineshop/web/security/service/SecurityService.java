package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SecurityService {

    void logOut(List<Session> sessionList);

    Optional<Session> login(Credentials credentials);

    Session getSession(String token);
}
