package com.obolonyk.onlineshop.dao.jdbc.rowmapper;

import com.obolonyk.onlineshop.entity.Role;
import com.obolonyk.onlineshop.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .password(resultSet.getString("password"))
                .salt(resultSet.getString("salt"))
                .role(Role.valueOf(resultSet.getString("role")))
                .build();
    }
}
