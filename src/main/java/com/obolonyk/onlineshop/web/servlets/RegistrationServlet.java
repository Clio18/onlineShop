package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.Credentials;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Setter
public class RegistrationServlet extends HttpServlet {
    private PageGenerator pageGenerator;
    private UserService userService;
    private SecurityService securityService;

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
