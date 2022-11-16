package com.obolonyk.onlineshop.security.service;

import com.obolonyk.onlineshop.security.entity.Credentials;
import com.obolonyk.onlineshop.security.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSecurityServiceTest {
    @InjectMocks
    private DefaultSecurityService securityService;
    @Mock
    private UserService userService;

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
    @DisplayName("Login Method With Valid Credentials")
    void testLoginWithValidCredentials() {
        when(userService.getByLogin("admin")).thenReturn(Optional.of(user));
        Optional<Session> optional = securityService.login(credentials);
        assertFalse(optional.isEmpty());
        Session session = optional.get();
        assertNotNull(session);
    }

    @Test
    @DisplayName("Login Method With Invalid Login")
    void testLoginWithInvalidLogin() {
        when(userService.getByLogin("admin")).thenReturn(Optional.empty());
        Optional<Session> optional = securityService.login(credentials);
        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("Login Method With Invalid Password")
    void testLoginWithInvalidPassword() {
        Credentials credentials = Credentials.builder()
                .login("admin")
                .password("xxxxx")
                .build();

        when(userService.getByLogin("admin")).thenReturn(Optional.of(user));
        Optional<Session> optional = securityService.login(credentials);
        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("GetSession Method With Valid Token")
    void testGetSessionWithValidToken() {
        Optional<Session> optional = securityService.getSession(token, sessionList);
        assertFalse(optional.isEmpty());
        Session session = optional.get();
        assertNotNull(session);
        assertEquals(session, this.session);
    }

    @Test
    @DisplayName("GetSession Method With Invalid Token")
    void testGetSessionWithInvalidToken() {
        String newToken = UUID.randomUUID().toString();
        Optional<Session> optional = securityService.getSession(newToken, sessionList);
        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("GetSessionIfExists Method With Valid User")
    void testGetSessionIfExists() {
        Optional<Session> optional = securityService.getSessionIfExists(user, sessionList);
        assertTrue(optional.isPresent());
        Session sessionUser = optional.get();
        assertEquals(session, sessionUser);
    }

    @Test
    @DisplayName("GetSessionIfExists Method With Invalid User")
    void testGetSessionIfNotExists() {
        User user = User.builder().build();
        Optional<Session> optional = securityService.getSessionIfExists(user, sessionList);
        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("GetSessionIfExists Method With User With Valid And Invalid Login")
    void testGetSessionIfExistsUserWithInvalidLogin() {
        User userSame = User.builder()
                .login("admin")
                .password("998c9d0f24add08a1a50e631802eb015")
                .lastName("C")
                .email("D")
                .salt("c8b82aa5-88dc-45c3-96a0-05234da8b0d2")
                .build();
        Optional<Session> optionalValid = securityService.getSessionIfExists(userSame, sessionList);
        assertTrue(optionalValid.isPresent());
        assertEquals(session, optionalValid.get());

        User userChanged = User.builder()
                .login("xxx")
                .password("998c9d0f24add08a1a50e631802eb015")
                .lastName("C")
                .email("D")
                .salt("c8b82aa5-88dc-45c3-96a0-05234da8b0d2")
                .build();

        Optional<Session> optionalInvalid = securityService.getSessionIfExists(userChanged, sessionList);
        assertFalse(optionalInvalid.isPresent());
    }
}