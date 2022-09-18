package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.web.servlets.auth.LogOutServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class LogOutServletTest {

    @Test
    @DisplayName("test DoPost And Verify Service Work")
    void testDoPostAndVerifyServiceWork() throws IOException {
        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);

        SecurityService securityService = mock(SecurityService.class);
        LogOutServlet logOutServlet = new LogOutServlet();
        logOutServlet.setSecurityService(securityService);

        Cookie [] cookies = new Cookie[1];
        Cookie cookie = new Cookie("user-token", "user");
        cookies[0] = cookie;
        when(mockReq.getCookies()).thenReturn(cookies);

        doNothing().when(securityService).logOut(isA(String.class));

        logOutServlet.doPost(mockReq, mockResp);

        verify(securityService, times(1)).logOut(isA(String.class));
        verify(mockResp, times(1)).addCookie(isA(Cookie.class));
        verify(mockResp, times(1)).sendRedirect(isA(String.class));
    }

}