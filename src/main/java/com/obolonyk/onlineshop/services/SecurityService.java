package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.Setter;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.*;

@Setter
public class SecurityService {
    private static final String USER_TOKEN = "user-token";
    private List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
    private UserService userService;

    public Session getSession(String token){
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
                        .expirationTime(LocalDateTime.now().plusMinutes(60))
                        .cart(new ArrayList<>())
                        .build();
                sessionList.add(session);
                return session;
            }
        }
        return null;
    }

    public Session getSessionIfAuth(Cookie[] cookies) {
        Session session = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(USER_TOKEN)) {
                    String token = cookie.getValue();
                     return getSession(token);
                }
            }
        }
        return session;
    }
}
