package com.obolonyk.onlineshop.web.filter;

import com.obolonyk.onlineshop.entity.Role;

import java.util.Set;


public class UserFilter extends SecurityFilter {
    @Override
    Set<Role> requiredRole() {
        return Set.of(Role.USER, Role.ADMIN);
    }
}
