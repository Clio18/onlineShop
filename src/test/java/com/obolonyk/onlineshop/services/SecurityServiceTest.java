package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityServiceTest {
    private User user;

    @BeforeEach
    void init (){
        user = User.builder()
                .login("kim88")
                .name("Kim")
                .lastName("Chen")
                .email("kim@gmail.com")
                .password("admin")
                .build();
    }

    @Test
    @DisplayName("test GetSession When Token Is Wrong And Return Null")
    void testGetSessionWhenTokenIsEmptyAndReturnNull(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        Session session = Session.builder()
                .user(user)
                .token("admin")
                .cart(new ArrayList<>())
                .expirationTime(LocalDateTime.now().plusMinutes(1))
                .build();
        sessionList.add(session);
        securityService.setSessionList(sessionList);

        when(userService.getByLogin("kim88")).thenReturn(Optional.of(user));
        Session serviceSession = securityService.getSession("a");
        assertNull(serviceSession);
    }

    @Test
    @DisplayName("test GetSession When Session Has Expired And Return Null")
    void testGetSessionWhenSessionHasExpiredAndReturnNull(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        Session session = Session.builder()
                .user(user)
                .token("admin")
                .cart(new ArrayList<>())
                .expirationTime(LocalDateTime.now().minusHours(1))
                .build();
        sessionList.add(session);
        securityService.setSessionList(sessionList);

        when(userService.getByLogin("kim88")).thenReturn(Optional.of(user));
        Session serviceSession = securityService.getSession("admin");
        assertNull(serviceSession);
    }

    @Test
    @DisplayName("test GetSession")
    void testGetSession(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        Session session = Session.builder()
                .user(user)
                .token("admin")
                .cart(new ArrayList<>())
                .expirationTime(LocalDateTime.now().plusMinutes(1))
                .build();
        sessionList.add(session);
        securityService.setSessionList(sessionList);

        when(userService.getByLogin("kim88")).thenReturn(Optional.of(user));
        Session serviceSession = securityService.getSession("admin");
        assertNotNull(serviceSession);
    }

    @Test
    @DisplayName("test Logout")
    void testLogOut(){
        SecurityService securityService = new SecurityService();

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        Session session = Session.builder()
                .user(user)
                .token("admin")
                .cart(new ArrayList<>())
                .expirationTime(LocalDateTime.now().plusMinutes(1))
                .build();
        sessionList.add(session);

        securityService.setSessionList(sessionList);

        securityService.logOut("admin");
        assertTrue(sessionList.isEmpty());
    }

    @Test
    @DisplayName("test Login")
    void testLogin(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        securityService.setSessionList(sessionList);

        Credentials credentials = Credentials.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .build();

        user.setSalt("admin");
        user.setPassword("f6fdffe48c908deb0f4c3bd36c032e72");
        when(userService.getByLogin("kim88")).thenReturn(Optional.of(user));
        Session session = securityService.login(credentials);
        assertNotNull(session);
        assertNotNull(sessionList);
        assertTrue(sessionList.size()==1);
    }

    @Test
    @DisplayName("test Login When Login Is Not In Db")
    void testLoginWhenLoginIsNotInDb(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        Credentials credentials = Credentials.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .build();
        when(userService.getByLogin("kim88")).thenReturn(Optional.empty());
        Session session = securityService.login(credentials);
        assertNull(session);
    }

    @Test
    @DisplayName("test Login With Wrong Password")
    void testLoginWrongPassword(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        securityService.setSessionList(sessionList);

        Credentials credentials = Credentials.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .build();

        user.setSalt("admin");
        when(userService.getByLogin("kim88")).thenReturn(Optional.of(user));
        Session session = securityService.login(credentials);
        assertNull(session);
    }

    @Test
    @DisplayName("test getSessionIfAuth")
    void testGetSessionIfAuth(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        List<Session> sessionList = Collections.synchronizedList(new ArrayList<>());
        Session session = Session.builder()
                .user(user)
                .token("admin")
                .cart(new ArrayList<>())
                .expirationTime(LocalDateTime.now().plusMinutes(1))
                .build();
        sessionList.add(session);
        securityService.setSessionList(sessionList);

        Cookie [] cookies = new Cookie[1];
        Cookie cookie = new Cookie("user-token", "admin");
        cookies[0] = cookie;

        Session sessionIfAuth = securityService.getSessionIfAuth(cookies);
        assertNotNull(sessionIfAuth);
    }

}