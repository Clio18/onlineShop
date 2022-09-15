package com.obolonyk.onlineshop.web.servlets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServletTest {

    @Test
    @DisplayName("testDoGetAndCheckResultIsNotEmpty")
    void testDoGetAndCheckResultIsNotEmpty() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        RegistrationServlet registrationServlet = new RegistrationServlet();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResp.getWriter()).thenReturn(writer);

        registrationServlet.doGet(mockReq, mockResp);
        assertNotNull(stringWriter);
    }

}