package com.obolonyk.onlineshop.dao.rowmapper;

import com.obolonyk.onlineshop.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {
    public static User mapRow(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .password(resultSet.getString("password"))
                .salt(resultSet.getString("salt"))
                .build();
    }
}
