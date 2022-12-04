package com.obolonyk.onlineshop.web.controller;

import com.obolonyk.onlineshop.security.entity.Session;
import com.obolonyk.onlineshop.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@Slf4j
public class AuthController {

    @Value("${web.session.time-to-live}")
    private Integer duration;

    private SecurityService securityService;

    @Autowired
    public AuthController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping(path = "/login")
    protected String loginGet() {
        return "login";
    }


    @PostMapping(path = "/login")
    protected String loginPost(@RequestParam String password,
                               @RequestParam String login,
                               HttpServletResponse resp) {

        Optional<Session> optional = securityService.login(password, login);

        if (optional.isPresent()) {
            Session session = optional.get();
            String token = session.getToken();
            Cookie cookie = new Cookie("user-token", token);
            cookie.setMaxAge(duration);
            resp.addCookie(cookie);
            return "redirect:/products";
        }
        return "redirect:/registration";
    }

    @PostMapping(path = "/logout")
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

    //TODO: @ModelAttribute User
    @PostMapping(path = "/registration")
    protected String registrationPost(@RequestParam String password,
                                      @RequestParam String login,
                                      @RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String lastName) {

        Optional<Session> optional = securityService.login(password, login);
        if (optional.isEmpty()) {
            securityService.saveUser(password, login, name, email, lastName);
            //we return user to login page where he can get his auth token
            return "redirect:/login";
        }
        Session session = optional.get();
        log.info("Retrieved session during registration {}", session);
        return "/registration";
    }
}
