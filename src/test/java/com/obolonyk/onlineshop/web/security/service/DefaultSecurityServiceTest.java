package com.obolonyk.onlineshop.web.security.service;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultSecurityServiceTest {
    private User user;
    private Credentials credentials;
    private Session session;
    private List<Session> sessionList;
    private String token;

    @BeforeEach
    void init() {
        user = User.builder()
                .login("admin")
                .password("998c9d0f24add08a1a50e631802eb015")
                .lastName("C")
                .email("D")
                .salt("c8b82aa5-88dc-45c3-96a0-05234da8b0d2")
                .build();

        credentials = Credentials.builder()
                .login("admin")
                .password("admin")
                .build();

        token = UUID.randomUUID().toString();
        session = Session.builder()
                .cart(new ArrayList<>(1))
                .token(token)
                .expirationTime(LocalDateTime.now().plusMinutes(2))
                .user(user)
                .build();

        sessionList = new ArrayList<>();
        sessionList.add(session);
    }

    @Test
    @DisplayName("test Login With Valid Credentials")
    void testLoginWithValidCredentials() {
        UserService userService = mock(UserService.class);
        when(userService.getByLogin("admin")).thenReturn(Optional.of(user));

        DefaultSecurityService securityService = new DefaultSecurityService();
        Session session = securityService.login(credentials, userService);
        assertNotNull(session);
    }

    @Test
    @DisplayName("test Login With Invalid Login")
    void testLoginWithInvalidLogin() {
        UserService userService = mock(UserService.class);
        when(userService.getByLogin("admin")).thenReturn(Optional.empty());

        DefaultSecurityService securityService = new DefaultSecurityService();
        Session session = securityService.login(credentials, userService);
        assertNull(session);
    }

    @Test
    @DisplayName("test Login With Invalid Password")
    void testLoginWithInvalidPassword() {
        Credentials credentials = Credentials.builder()
                .login("admin")
                .password("xxxxx")
                .build();

        UserService userService = mock(UserService.class);
        when(userService.getByLogin("admin")).thenReturn(Optional.of(user));

        DefaultSecurityService securityService = new DefaultSecurityService();
        Session session = securityService.login(credentials, userService);
        assertNull(session);
    }

    @Test
    @DisplayName("test LogOut")
    void testLogOut() {
        DefaultSecurityService securityService = new DefaultSecurityService();
        securityService.logOut(token, sessionList);
        assertTrue(sessionList.isEmpty());
    }

    @Test
    @DisplayName("test Get Session With Valid Token")
    void testGetSessionWithValidToken() {
        DefaultSecurityService securityService = new DefaultSecurityService();
        Session serviceSession = securityService.getSession(token, sessionList);
        assertNotNull(serviceSession);
        assertTrue(session.equals(serviceSession));
    }

    @Test
    @DisplayName("test Get Session With Invalid Token")
    void testGetSessionWithInvalidToken() {
        String newToken = UUID.randomUUID().toString();
        DefaultSecurityService securityService = new DefaultSecurityService();
        Session serviceSession = securityService.getSession(newToken, sessionList);
        assertNull(serviceSession);
    }

}