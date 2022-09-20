package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;

import java.util.List;

public interface SecurityService {

    void logOut(List<Session> sessionList);

    Session login(Credentials credentials);

    Session getSession(String token);
}
