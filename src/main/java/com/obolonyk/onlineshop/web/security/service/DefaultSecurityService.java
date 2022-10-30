package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Setter
public class DefaultSecurityService implements SecurityService {
    private String duration;

    private List<Session> sessionList = new CopyOnWriteArrayList<>();

    private UserService userService;

    @Override
    public Session login(Credentials credentials) {
        return login(credentials, userService);
    }


    @Override
    public Session getSession(String token) {
        return getSession(token, sessionList);
    }

    @Override
    public void logOut(List<Session> sessionList) {
        //TODO: remove all expired sessions
    }

    Session getSession(String token, List<Session> sessionList) {
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

    Session login(Credentials credentials, UserService userService) {
        Optional<User> userByLogin = userService.getByLogin(credentials.getLogin());
        if (userByLogin.isPresent()) {
            User user = userByLogin.get();
            String salt = user.getSalt();
            String password = user.getPassword();
            String hashedPass = PasswordGenerator.generateEncrypted(credentials.getPassword(), salt);
            if (hashedPass.equals(password)) {

                for (Session session : sessionList) {
                    if (session.getUser().getLogin().equals(user.getLogin())
                            && session.getExpirationTime().isAfter(LocalDateTime.now())) {

                        log.info("User already has a valid session");
                        return session;
                    }
                }

                String token = UUID.randomUUID().toString();

                int durationInSeconds = Integer.parseInt(duration);

                Session session = Session.builder()
                        .user(user)
                        .token(token)
                        .expirationTime(LocalDateTime.now().plusSeconds(durationInSeconds))
                        .build();
                sessionList.add(session);
                log.info("User was logged in. New session was created");
                return session;
            }
        }
        return null;
    }

}
