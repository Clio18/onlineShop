package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class LoginServlet extends HttpServlet {
    private static final SecurityService securityService = ServiceLocator.getService(SecurityService.class);
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private static final Properties props = ServiceLocator.getService(Properties.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("templates/login.html", null);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        Credentials credentials = Credentials.builder()
                .login(login)
                .password(password)
                .build();

        Session session = securityService.login(credentials);
        log.info("Retrieved session during logging in {}", session);

        if (session != null) {
            int durationInSeconds = Integer.parseInt(props.getProperty("durationInSeconds"));
            String token = session.getToken();
            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(durationInSeconds);
            resp.addCookie(cookie);
            resp.sendRedirect("/products");
        } else {
            String errorMessage = "You are not registered";
            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("templates/registration.html", parameters);

            resp.getWriter().write(page);
        }
    }
}
