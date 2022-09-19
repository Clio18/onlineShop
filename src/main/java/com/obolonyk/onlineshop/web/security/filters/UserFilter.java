package com.obolonyk.onlineshop.web.security.filters;

import com.obolonyk.onlineshop.entity.Role;

import java.util.Set;


public class UserFilter extends SecurityFilter {
    @Override
    Set<Role> requiredRole() {
        return Set.of(Role.USER, Role.ADMIN);
    }
}
