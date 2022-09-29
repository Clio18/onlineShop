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
class RegistrationServletTest {

    @Mock
    HttpServletRequest mockReq;
    @Mock
    HttpServletResponse mockResp;

    @InjectMocks
    RegistrationServlet registrationServlet;

    @Test
    @DisplayName("test DoGet And Check Result The Form Is Not Empty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);
        registrationServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
    }

}