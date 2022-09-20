package com.obolonyk.onlineshop.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Session {
    private User user;
    private String token;
    private LocalDateTime expirationTime;
    private List<Order> cart;
}
