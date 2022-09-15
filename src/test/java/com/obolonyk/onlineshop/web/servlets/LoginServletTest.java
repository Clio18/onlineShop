package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

class LoginServletTest {

    @Test
    @DisplayName("testDoGetAndCheckResultIsNotEmpty")
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

       when(securityService.isAuth(isA(String.class), isA(String.class))).thenReturn(true);

        loginServlet.doPost(mockReq, mockResp);
        verify(securityService, times(1)).getToken();
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

        when(securityService.isAuth(isA(String.class), isA(String.class))).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        loginServlet.doPost(mockReq, mockResp);

        assertTrue(stringWriter.toString().contains("Registration"));
    }
}