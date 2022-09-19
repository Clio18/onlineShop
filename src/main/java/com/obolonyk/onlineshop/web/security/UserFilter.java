package com.obolonyk.onlineshop.web.security;

import com.obolonyk.onlineshop.entity.Role;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class UserFilter extends SecurityFilter{
    @Override
    Set<Role> requiredRole() {
        log.info("USER FILTER");
        return Set.of(Role.USER, Role.ADMIN);
    }
}
