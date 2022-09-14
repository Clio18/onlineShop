package com.obolonyk.onlineshop.services;

import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.web.security.PasswordGenerator;
import lombok.Setter;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Setter
public class SecurityService {
    private List<String> sessionList;
    private UserService userService;

    public String getToken() {
        String uuid = UUID.randomUUID().toString();
        sessionList.add(uuid);
        return uuid;
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

    public User createAuthorized(User user) {
        String login = user.getLogin();
        Optional<User> userByLogin = userService.getByLogin(login);
        if (userByLogin.isEmpty()) {
            String salt = UUID.randomUUID().toString();
            String password = user.getPassword();
            String encryptedPassword = PasswordGenerator.generateEncrypted(password, salt);

            User userForDB = User.builder()
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .login(user.getLogin())
                    .password(encryptedPassword)
                    .salt(salt)
                    .build();
            return userForDB;
        }
        return user;
    }

    public boolean isAuth(String login, String password) {
        Optional<User> userByLogin = userService.getByLogin(login);
        if (userByLogin.isPresent()) {
            User user = userByLogin.get();
            String salt = user.getSalt();
            String enteredPass = PasswordGenerator.generateEncrypted(password, salt);
            return user.getPassword().equals(enteredPass);
        }
        return false;
    }
}
