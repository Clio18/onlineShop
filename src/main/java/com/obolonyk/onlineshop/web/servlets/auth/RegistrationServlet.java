package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import com.obolonyk.onlineshop.utils.PageGenerator;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RegistrationServlet extends HttpServlet {
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private static final UserService userService = ServiceLocator.getService(UserService.class);
    private static final SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("templates/registration.html", null);
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
        log.info("Retrieved session during registration {}", session);

        if (session == null) {
            String salt = UUID.randomUUID().toString();
            String encrypted = PasswordGenerator.generateEncrypted(password, salt);
            User user = User.builder()
                    .name(req.getParameter("name"))
                    .email(req.getParameter("email"))
                    .lastName(req.getParameter("last_name"))
                    .login(login)
                    .password(encrypted)
                    .salt(salt)
                    .build();
            userService.save(user);
            resp.sendRedirect("/login");
        } else {
            String errorMessage = "This login is already exists";
            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("templates/registration.html", parameters);
            resp.getWriter().write(page);
        }
    }
}
