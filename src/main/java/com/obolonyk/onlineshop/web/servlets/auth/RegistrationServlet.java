package com.obolonyk.onlineshop.web.servlets.auth;

import com.obolonyk.ioc.context.ApplicationContext;
import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import com.obolonyk.onlineshop.web.security.service.DefaultSecurityService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RegistrationServlet extends HttpServlet {
    private static final PageGenerator pageGenerator = PageGenerator.instance();
    private ApplicationContext applicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
    }

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

        DefaultSecurityService securityService = applicationContext.getBean(DefaultSecurityService.class);
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
            UserService userService = applicationContext.getBean(UserService.class);
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
