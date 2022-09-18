package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.web.servlets.auth.LoginServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

class LoginServletTest {

    @Test
    @DisplayName("test DoGet And Check If Form Is Not Empty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        LoginServlet loginServlet = new LoginServlet();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        loginServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
        assertTrue(stringWriter.toString().contains("Login"));
    }

    @Test
    @DisplayName("test DoPost And Verify Service Work If IsAuth")
    void testDoPostAndVerifyServiceWorkIfIsAuth() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("login")).thenReturn("admin");
        when(mockReq.getParameter("password")).thenReturn("admin");

        SecurityService securityService = mock(SecurityService.class);
        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);

        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        Session session = Session.builder()
                .user(user)
                .cart(new ArrayList<>())
                .token("user")
                .build();
        when(securityService.login(isA(Credentials.class))).thenReturn(session);

        loginServlet.doPost(mockReq, mockResp);

        verify(mockResp, times(1)).addCookie(isA(Cookie.class));
        verify(mockResp, times(1)).sendRedirect(isA(String.class));
    }

    @Test
    @DisplayName("test DoPost And Verify Service Work If Not IsAuth")
    void testDoPostAndVerifyServiceWorkIfNotIsAuth() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        when(mockReq.getParameter("login")).thenReturn("admin");
        when(mockReq.getParameter("password")).thenReturn("admin");

        SecurityService securityService = mock(SecurityService.class);
        LoginServlet loginServlet = new LoginServlet();
        loginServlet.setSecurityService(securityService);

        when(securityService.login(isA(Credentials.class))).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        loginServlet.doPost(mockReq, mockResp);

        assertTrue(stringWriter.toString().contains("Registration"));
    }
}