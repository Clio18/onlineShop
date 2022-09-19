package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.*;

public class DefaultSecurityService implements SecurityService {
    private List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private UserService userService = ServiceLocator.getService(UserService.class);
    private Properties props = ServiceLocator.getService(Properties.class);

    @Override
    public void logOut(String token) {
        if (!sessionList.isEmpty()) {
            sessionList.removeIf(session -> session.getToken().equals(token));
        }
    }

    @Override
    public Session login(Credentials credentials) {
        Optional<User> userByLogin = userService.getByLogin(credentials.getLogin());
        if (userByLogin.isPresent()) {
            User user = userByLogin.get();
            String salt = user.getSalt();
            String password = user.getPassword();
            String hashedPass = PasswordGenerator.generateEncrypted(credentials.getPassword(), salt);
            if (hashedPass.equals(password)) {
                String token = UUID.randomUUID().toString();
                int durationInSeconds = Integer.parseInt(props.getProperty("durationInSeconds"));
                Session session = Session.builder()
                        .user(user)
                        .token(token)
                        .cart(new ArrayList<>(1))
                        .expirationTime(LocalDateTime.now().plusSeconds(durationInSeconds))
                        .build();
                sessionList.add(session);
                return session;
            }
        }
        return null;
    }

    @Override
    public Session getSession(String token) {
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                if (session.getExpirationTime().isBefore(LocalDateTime.now())) {
                    sessionList.remove(session);
                    return null;
                }
                return session;
            }
        }
        return null;
    }
}