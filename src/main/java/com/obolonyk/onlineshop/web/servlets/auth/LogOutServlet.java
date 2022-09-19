package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.web.security.service.DefaultSecurityService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import lombok.Setter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class LogOutServlet extends HttpServlet {
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("user-token")){
                String token = cookie.getValue();
                securityService.logOut(token);
                resp.addCookie(new Cookie("user-token", null));
                resp.sendRedirect("/login");
            }
        }
    }

}
