package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.PropertiesReader;
import com.obolonyk.onlineshop.web.PageGenerator;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import com.obolonyk.templator.TemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@Controller
@Slf4j
public class AuthController {
    private static final TemplateFactory pageGenerator = PageGenerator.instance();

    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    protected @ResponseBody String loginGet() {
        return pageGenerator.getPage("login.html");
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected void loginPost(@RequestParam String password,
                          @RequestParam String login, HttpServletResponse resp) throws IOException {

        Credentials credentials = Credentials.builder()
                .login(login)
                .password(password)
                .build();

        Session session = securityService.login(credentials);

        if (session != null) {
            PropertiesReader propertiesReader = new PropertiesReader();
            Properties props = propertiesReader.getProperties();

            int durationInSeconds = Integer.parseInt(props.getProperty("durationInSeconds"));
            String token = session.getToken();
            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(durationInSeconds);
            resp.addCookie(cookie);
            resp.sendRedirect("/products");
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    protected void logoutPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                resp.addCookie(new Cookie("user-token", null));
                resp.sendRedirect("/login");
            }
        }
    }

    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    protected void registrationGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = pageGenerator.getPage("registration.html");
        resp.getWriter().write(page);
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
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
        }
    }
}
