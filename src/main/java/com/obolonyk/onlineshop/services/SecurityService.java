package com.obolonyk.onlineshop.services;

import lombok.Setter;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.UUID;

@Setter
public class SecurityService {
    private List<String> sessionList;

    public Cookie getCookie (){
        String uuid = UUID.randomUUID().toString();
        sessionList.add(uuid);
        return new Cookie("user-token", uuid);
    }

    public boolean isAuth(Cookie[] cookies) {
        boolean isValid = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    if (sessionList.contains(cookie.getValue())) {
                        isValid = true;
                    }
                    break;
                }
            }
        }
        return isValid;
    }
}
