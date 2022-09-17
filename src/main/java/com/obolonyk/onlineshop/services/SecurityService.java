package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Setter
public class SecurityService {
    private List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private UserService userService;
    private int durationInSeconds;

    public void logOut(String token) {
        if (!sessionList.isEmpty()) {
            sessionList.removeIf(session -> session.getToken().equals(token));
        }
    }

    public Session login(Credentials credentials) {
        Optional<User> userByLogin = userService.getByLogin(credentials.getLogin());
        if (userByLogin.isPresent()) {
            User user = userByLogin.get();
            String salt = user.getSalt();
            String password = user.getPassword();
            String hashedPass = PasswordGenerator.generateEncrypted(credentials.getPassword(), salt);
            if (hashedPass.equals(password)) {
                String token = UUID.randomUUID().toString();
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

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

}
