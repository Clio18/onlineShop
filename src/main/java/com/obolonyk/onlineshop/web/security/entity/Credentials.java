package com.obolonyk.onlineshop.web.security.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Credentials {
    private String login;
    private String password;
}
