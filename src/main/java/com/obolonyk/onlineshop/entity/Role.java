package com.obolonyk.onlineshop.entity;

public enum Role {
    ADMIN("ADMIN"), USER("ADMIN");

    private final String role;

    Role(String userRole) {
        this.role = userRole;
    }

    public String getUserRole() {
        return role;
    }
}
