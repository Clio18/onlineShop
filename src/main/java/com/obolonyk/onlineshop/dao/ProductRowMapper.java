package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductRowMapper {
    public Product mapRow(ResultSet resultSet) throws SQLException {
        return Product.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getDouble("price"))
                .creationDate(resultSet.getObject("creation_date", LocalDateTime.class))
                .build();
    }
}
