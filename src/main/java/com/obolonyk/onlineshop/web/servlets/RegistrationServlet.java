package com.obolonyk.onlineshop.web.servlets;

import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.PageGenerator;
import lombok.Setter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Setter
public class RegistrationServlet extends HttpServlet {
    private PageGenerator instance = PageGenerator.instance();
    private UserService userService;
    private SecurityService securityService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = instance.getPage("templates/registration.html", null);
        resp.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = User.builder()
                .name(req.getParameter("name"))
                .email(req.getParameter("email"))
                .lastName(req.getParameter("last_name"))
                .login(req.getParameter("login"))
                .password(req.getParameter("password"))
                .build();

        User authorizedUser = securityService.createAuthorized(user);
        if (!authorizedUser.equals(user)) {
            userService.save(authorizedUser);
            resp.sendRedirect("/login");
        } else {
            String errorMessage = "This login is already exists";
            PageGenerator pageGenerator = PageGenerator.instance();
            Map<String, Object> parameters = Map.of("errorMessage", errorMessage);
            String page = pageGenerator.getPage("templates/registration.html", parameters);
            resp.getWriter().write(page);
        }
    }
}
