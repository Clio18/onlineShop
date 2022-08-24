package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoITest {

    @Test
    @DisplayName("getAll Test And Return The List And Check Size And NotNull And NotNull Fields")
    void getAllTestAndReturnTheListAndCheckSizeAndNotNullAndNotNullFields() throws SQLException {
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        List<Product> products = jdbcProductDao.getAll();
        assertNotNull(products);
        assertEquals(5, products.size());
        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
            assertTrue(product.getId()>0);
            assertTrue(product.getPrice()>=0);
        }
    }

}