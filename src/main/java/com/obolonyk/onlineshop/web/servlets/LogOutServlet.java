package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.services.SecurityService;
import lombok.Setter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class LogOutServlet extends HttpServlet {
    private SecurityService securityService;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie cookie = securityService.logOut();
        resp.addCookie(cookie);
        resp.sendRedirect("/login");
    }

}
