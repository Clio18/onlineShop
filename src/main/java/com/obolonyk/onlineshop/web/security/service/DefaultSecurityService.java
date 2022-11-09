package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class DefaultSecurityService implements SecurityService {

    @Autowired
    private UserService userService;

    @Value("${web.session.time-to-live}")
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

                //check if the session for this user is already exist
                Optional<Session> optionalSession = getSessionIfExists(user, sessionList);
                if (optionalSession.isPresent()) return optionalSession;

                //create new session and save it
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

    Optional<Session> getSessionIfExists(User user, List<Session> sessionList) {
        for (Session session : sessionList) {
            if (session.getUser().getLogin().equals(user.getLogin())
                    && session.getExpirationTime().isAfter(LocalDateTime.now())) {

                log.info("User already has a valid session");
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
