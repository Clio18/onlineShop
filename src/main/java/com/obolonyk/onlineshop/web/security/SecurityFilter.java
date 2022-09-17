package com.obolonyk.onlineshop.web.security;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.SecurityService;
import lombok.Setter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class SecurityFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private SecurityService securityService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        if (path.equals("/login")||path.equals("/registration")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(USER_TOKEN)) {
                    String token = cookie.getValue();
                    Session session = securityService.getSession(token);
                    if (session != null) {
                        servletRequest.setAttribute("session", session);
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        }
        response.sendRedirect("/login");
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
