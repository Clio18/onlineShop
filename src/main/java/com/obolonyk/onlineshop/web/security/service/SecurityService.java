package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;

public interface SecurityService {

    void logOut(String token);

    Session login(Credentials credentials);

    Session getSession(String token);
}
