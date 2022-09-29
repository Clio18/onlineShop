package com.obolonyk.onlineshop.web.servlets.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock
    HttpServletRequest mockReq;
    @Mock
    HttpServletResponse mockResp;

    @InjectMocks
    LoginServlet loginServlet;

    @Test
    @DisplayName("test DoGet And Check If Form Is Not Empty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);
        loginServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
        assertTrue(stringWriter.toString().contains("Login"));
    }
}