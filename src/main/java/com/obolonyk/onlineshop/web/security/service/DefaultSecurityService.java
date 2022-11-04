package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Setter
@RequiredArgsConstructor
public class DefaultSecurityService implements SecurityService {
    private final UserService userService;

    private int duration;

    private List<Session> sessionList = new CopyOnWriteArrayList<>();

    @Override
    public Session getSession(String token) {
        Optional<Session> optional = getSession(token, sessionList);
        if (optional.isPresent()){
            return optional.get();
        }
        throw new RuntimeException("No Session for token");
    }

    @Override
    public void logOut(List<Session> sessionList) {
        //TODO: remove all expired sessions
    }

    public Optional<Session> login(Credentials credentials) {
        Optional<User> userByLogin = userService.getByLogin(credentials.getLogin());
        if (userByLogin.isPresent()) {
            User user = userByLogin.get();
            String salt = user.getSalt();
            String password = user.getPassword();
            String hashedPass = PasswordGenerator.generateEncrypted(credentials.getPassword(), salt);

            if (hashedPass.equals(password)) {
                //TODO: method
                for (Session session : sessionList) {
                    if (session.getUser().getLogin().equals(user.getLogin())
                            && session.getExpirationTime().isAfter(LocalDateTime.now())) {

                        log.info("User already has a valid session");
                        return Optional.of(session);
                    }
                }

                String token = UUID.randomUUID().toString();

                Session session = Session.builder()
                        .user(user)
                        .token(token)
                        .expirationTime(LocalDateTime.now().plusSeconds(duration))
                        .build();
                sessionList.add(session);
                log.info("User was logged in. New session was created");
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }

    Optional<Session> getSession(String token, List<Session> sessionList) {
        for (Session session : sessionList) {
            if (session.getToken().equals(token)) {
                if (session.getExpirationTime().isBefore(LocalDateTime.now())) {
                    sessionList.remove(session);
                    return Optional.empty();
                }
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }
}
