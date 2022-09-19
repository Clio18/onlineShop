package com.obolonyk.onlineshop.web.security;

import com.obolonyk.onlineshop.entity.Role;
import com.obolonyk.onlineshop.entity.Session;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.locator.ServiceLocator;
import lombok.Setter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Setter
public abstract class SecurityFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/registration")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String token = getToken(cookies);

        Session session = securityService.getSession(token);
        if(isAuthenticated(session)){
            servletRequest.setAttribute("session", session);
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            response.sendRedirect("/products");
        }
    }


    public boolean isAuthenticated(Session session){
        if(session==null){
            return false;
        }
        return requiredRole().contains(session.getUser().getRole());
    }

    private String getToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(USER_TOKEN)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    abstract Set<Role> requiredRole();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
