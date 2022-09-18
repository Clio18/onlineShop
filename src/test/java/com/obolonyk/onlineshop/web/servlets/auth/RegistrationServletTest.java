package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import com.obolonyk.onlineshop.web.servlets.auth.RegistrationServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServletTest {

    @Test
    @DisplayName("test DoGet And Check Result The Form Is Not Empty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        RegistrationServlet registrationServlet = new RegistrationServlet();
        PageGenerator pageGenerator = PageGenerator.instance();
        registrationServlet.setPageGenerator(pageGenerator);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        registrationServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
    }

    @Test
    @DisplayName("test DoPost If Is New User")
    void testDoPostIfIsNewUser() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        SecurityService securityService = mock(SecurityService.class);
        UserService userService = mock(UserService.class);
        RegistrationServlet registrationServlet = new RegistrationServlet();
        registrationServlet.setUserService(userService);
        registrationServlet.setSecurityService(securityService);

        when(mockReq.getParameter("login")).thenReturn("admin");
        when(mockReq.getParameter("password")).thenReturn("admin");

        Credentials credentials = Credentials.builder()
                .login("admin")
                .password("admin")
                .build();

        when(securityService.login(credentials)).thenReturn(null);

        registrationServlet.doPost(mockReq, mockResp);

        verify(userService, times(1)).save(isA(User.class));
        verify(mockResp, times(1)).sendRedirect(isA(String.class));
    }


    @Test
    @DisplayName("test DoPost If Is Existing User")
    void testDoPostIfIsExistingUser() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        SecurityService securityService = mock(SecurityService.class);
        UserService userService = mock(UserService.class);
        RegistrationServlet registrationServlet = new RegistrationServlet();
        PageGenerator pageGenerator = PageGenerator.instance();
        registrationServlet.setPageGenerator(pageGenerator);
        registrationServlet.setUserService(userService);
        registrationServlet.setSecurityService(securityService);

        when(mockReq.getParameter("login")).thenReturn("admin");
        when(mockReq.getParameter("password")).thenReturn("admin");

        Credentials credentials = Credentials.builder()
                .login("admin")
                .password("admin")
                .build();

        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        Session session = Session.builder()
                .user(user)
                .cart(new ArrayList<>())
                .token("user")
                .build();

        when(securityService.login(credentials)).thenReturn(session);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        registrationServlet.doPost(mockReq, mockResp);
        assertNotNull(stringWriter);
    }

}