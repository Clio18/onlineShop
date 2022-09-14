package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Setter
public class LoginServlet extends HttpServlet {
    private SecurityService securityService;
    private PageGenerator pageGenerator = PageGenerator.instance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("templates/login.html", null);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        boolean isAuth;
        isAuth = securityService.isAuth(login, password);
        if (isAuth) {
            String token = securityService.getToken();
            Cookie cookie = new Cookie("user-token", token);
            resp.addCookie(cookie);
            resp.sendRedirect("/products");
        } else {
            String errorMessage = "You are not registered";
            PageGenerator pageGenerator = PageGenerator.instance();
            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("templates/registration.html", parameters);
            resp.getWriter().write(page);
        }
    }
}
