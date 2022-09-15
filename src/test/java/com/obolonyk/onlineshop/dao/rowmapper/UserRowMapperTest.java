package com.obolonyk.onlineshop.dao.rowmapper;

import com.obolonyk.onlineshop.dao.rowmapper.UserRowMapper;
import com.obolonyk.onlineshop.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRowMapperTest {

    @Test
    @DisplayName("user rowMapper Test And Check NotNull And Equals")
    void userRowMapperTestAndCheckNotNullAndEquals() throws SQLException {
        ResultSet mockRs = mock(ResultSet.class);
        when(mockRs.getLong("id")).thenReturn(1L);
        when(mockRs.getString("name")).thenReturn("Kim");
        when(mockRs.getString("last_name")).thenReturn("Chen");
        when(mockRs.getString("login")).thenReturn("kim88");
        when(mockRs.getString("email")).thenReturn("kim@gmail.com");
        when(mockRs.getString("password")).thenReturn("0751c66270213218b65b45152b7349da");
        when(mockRs.getString("salt")).thenReturn("513072e6-c539-45f3-be77-2fbaf003d6c3");

        User user = UserRowMapper.mapRow(mockRs);
        User expectedUser = User.builder()
                .id(1L)
                .name("Kim")
                .lastName("Chen")
                .login("kim88")
                .email("kim@gmail.com")
                .password("0751c66270213218b65b45152b7349da")
                .salt("513072e6-c539-45f3-be77-2fbaf003d6c3")
                .build();
        assertNotNull(user);
        assertEquals(expectedUser, user);
    }

}