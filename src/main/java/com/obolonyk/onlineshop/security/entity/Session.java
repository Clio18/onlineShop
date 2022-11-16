package com.obolonyk.onlineshop.security.entity;

import  com.obolonyk.onlineshop.entity.Order;
import com.obolonyk.onlineshop.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Session {
    private final User user;
    private final String token;
    private final LocalDateTime expirationTime;
    private List<Order> cart;
}
