package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import com.obolonyk.onlineshop.web.security.entity.Credentials;
import com.obolonyk.onlineshop.web.security.entity.Session;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthController {

    @Value("${durationInSeconds}")
    private String duration;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;


    @GetMapping(path = "/login")
    protected String loginGet() {
        return "login";
    }


    @PostMapping(path = "/login")
    protected String loginPost(@RequestParam String password,
                               @RequestParam String login,
                               HttpServletResponse resp) {

        Credentials credentials = Credentials.builder()
                .login(login)
                .password(password)
                .build();

        Session session = securityService.login(credentials);

        if (session != null) {

            int durationInSeconds = Integer.parseInt(duration);

            String token = session.getToken();
            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(durationInSeconds);
            resp.addCookie(cookie);
            return "redirect:/products";
        }
        return "redirect:/registration";
    }

    @PostMapping (path = "/logout")
    protected String logoutPost(HttpServletRequest req,
                                HttpServletResponse resp) {

        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                resp.addCookie(new Cookie("user-token", null));
            }
        }
        return "redirect:/login";
    }

    @GetMapping(path = "/registration")
    protected String registrationGet() {
        return "registration";
    }

    @PostMapping(path = "/registration")
    protected String registrationPost(@RequestParam String password,
                                      @RequestParam String login,
                                      @RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String last_name) {

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
                    .name(name)
                    .email(email)
                    .lastName(last_name)
                    .login(login)
                    .password(encrypted)
                    .salt(salt)
                    .build();
            userService.save(user);
            return "redirect:/login";
        }
        return "/registration";
    }
}
