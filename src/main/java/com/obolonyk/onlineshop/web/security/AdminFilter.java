package com.obolonyk.onlineshop.web.security;

import com.obolonyk.onlineshop.entity.Role;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class AdminFilter extends SecurityFilter {
    @Override
    Set<Role> requiredRole() {
        log.info("ADMIN FILTER");
        return Set.of(Role.ADMIN);
    }
}
