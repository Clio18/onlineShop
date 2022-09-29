package com.obolonyk.onlineshop.web.servlets.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class LogOutServletTest {

    @Test
    @DisplayName("test DoPost And Verify Service Work")
    void testDoPostAndVerifyServiceWork() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        LogOutServlet logOutServlet = new LogOutServlet();

        Cookie[] cookies = new Cookie[1];
        Cookie cookie = new Cookie("user-token", "user");
        cookies[0] = cookie;
        when(mockReq.getCookies()).thenReturn(cookies);

        logOutServlet.doPost(mockReq, mockResp);

        verify(mockResp, times(1)).addCookie(isA(Cookie.class));
        verify(mockResp, times(1)).sendRedirect(isA(String.class));
    }

}