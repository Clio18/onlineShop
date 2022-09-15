package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
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
    @DisplayName("test CreateAuthorizedUser When User Is New")
    void testCreateAuthorizedUserWhenUserIsNew(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        when(userService.getByLogin("kim88")).thenReturn(Optional.empty());
        User authorized = securityService.createAuthorized(user);
        assertFalse(authorized.getSalt().isEmpty());
        assertNotEquals(authorized.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("test CreateAuthorizedUser When User Is Already Exist")
    void testCreateAuthorizedUserWhenUserIsAlreadyExist(){
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        Optional<User> optionalUser = Optional.of(user);
        when(userService.getByLogin("kim88")).thenReturn(optionalUser);
        User authorized = securityService.createAuthorized(user);
        assertEquals(authorized, user);
    }

    @Test
    void testIsAlreadyAuthWithRightCookieNameAndValue(){
        ArrayList<String> sessionList = mock(ArrayList.class);
        SecurityService securityService = new SecurityService();
        securityService.setSessionList(sessionList);
        when(sessionList.contains(isA(String.class))).thenReturn(true);
        Cookie [] cookies = {new Cookie("user-token", "cookie")};
        boolean auth = securityService.isAlreadyAuth(cookies);
        assertTrue(auth);
    }

    @Test
    void testIsAlreadyAuthWithWrongCookieName(){
        ArrayList<String> sessionList = mock(ArrayList.class);
        SecurityService securityService = new SecurityService();
        securityService.setSessionList(sessionList);
        when(sessionList.contains(isA(String.class))).thenReturn(true);
        Cookie [] cookies = {new Cookie("token", "cookie")};
        boolean auth = securityService.isAlreadyAuth(cookies);
        assertFalse(auth);
    }

    @Test
    void testIsAlreadyAuthWithRightCookieValue(){
        ArrayList<String> sessionList = mock(ArrayList.class);
        SecurityService securityService = new SecurityService();
        securityService.setSessionList(sessionList);
        when(sessionList.contains("cookie")).thenReturn(true);
        Cookie [] cookies = {new Cookie("user-token", "cookie")};
        boolean auth = securityService.isAlreadyAuth(cookies);
        assertTrue(auth);
    }

    @Test
    void testIsAlreadyAuthWittWrongCookieValue(){
        ArrayList<String> sessionList = mock(ArrayList.class);
        SecurityService securityService = new SecurityService();
        securityService.setSessionList(sessionList);
        when(sessionList.contains("cookie")).thenReturn(false);
        Cookie [] cookies = {new Cookie("user-token", "cookie")};
        boolean auth = securityService.isAlreadyAuth(cookies);
        assertFalse(auth);
    }

    @Test
    void testIsAuthWithRightLoginAndPassword(){
        user.setSalt("513072e6-c539-45f3-be77-2fbaf003d6c3");
        user.setPassword("0751c66270213218b65b45152b7349da");
        Optional<User> optionalUser = Optional.of(user);
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        when(userService.getByLogin("kim88")).thenReturn(optionalUser);
        boolean auth = securityService.isAuth("kim88", "admin");
        assertTrue(auth);
    }

    @Test
    void testIsAuthWithRightLoginAndWrongPassword(){
        user.setSalt("513072e6-c539-45f3-be77-2fbaf003d6c3");
        user.setPassword("0751c66270213218b65b45152b7349da");
        Optional<User> optionalUser = Optional.of(user);
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        when(userService.getByLogin("kim88")).thenReturn(optionalUser);
        boolean auth = securityService.isAuth("kim88", "password");
        assertFalse(auth);
    }

    @Test
    void testIsAuthWithWrongLoginAndRightPassword(){
        Optional<User> optionalUser = Optional.empty();
        UserService userService = mock(UserService.class);
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        when(userService.getByLogin("kim")).thenReturn(optionalUser);
        boolean auth = securityService.isAuth("kim", "admin");
        assertFalse(auth);
    }

}