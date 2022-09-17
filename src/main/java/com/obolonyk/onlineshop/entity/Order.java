package com.obolonyk.onlineshop.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Product product;
    private int quantity;
    private double total;
}
