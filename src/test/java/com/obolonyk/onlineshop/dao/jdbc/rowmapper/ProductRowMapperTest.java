package com.obolonyk.onlineshop.dao.jdbc.rowmapper;

import com.obolonyk.onlineshop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductRowMapperTest {

    @Test
    @DisplayName("Product RowMapper Test And Check NotNull And Equals")
    void productRowMapperTestAndCheckNotNullAndEquals() throws SQLException {
        ProductRowMapper productRowMapper = new ProductRowMapper();
        ResultSet mockRs = mock(ResultSet.class);
        when(mockRs.getLong("id")).thenReturn(1L);
        when(mockRs.getString("name")).thenReturn("Teddy Bear");
        when(mockRs.getDouble("price")).thenReturn(10.99);
        LocalDateTime localDateTime = LocalDateTime.now();
        when(mockRs.getObject("creation_date", LocalDateTime.class)).thenReturn(localDateTime);
        Product product = productRowMapper.mapRow(mockRs, 1);
        Product expectedProduct = Product.builder()
                .id(1L)
                .name("Teddy Bear")
                .price(10.99)
                .creationDate(localDateTime)
                .build();
        assertNotNull(product);
        assertEquals(expectedProduct, product);
    }

}