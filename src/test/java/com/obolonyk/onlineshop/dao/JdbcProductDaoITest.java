package com.obolonyk.onlineshop.dao;

import com.obolonyk.onlineshop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoITest {

    @Test
    @DisplayName("getAll Test And Return The List And NotNull And NotNull Fields")
    void getAllTestAndReturnTheListAndCheckSizeAndNotNullAndNotNullFields() throws SQLException {
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        List<Product> products = jdbcProductDao.getAll();
        assertNotNull(products);
        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
            assertTrue(product.getId() > 0);
            assertTrue(product.getPrice() >= 0);
        }
    }

    @Test
    @DisplayName("getById Test And Return The Product")
    void getByIdTestAndReturnTheProductAndCheckNotNullAndNotNullFields() throws SQLException {
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        Product product = jdbcProductDao.getById(1);
        assertNotNull(product);
        assertNotNull(product.getName());
        assertNotNull(product.getCreationDate());
        assertTrue(product.getId() > 0);
        assertTrue(product.getPrice() >= 0);
    }

    @Test
    @DisplayName("save Test And Check Quantity Of Products Before And After ")
    void getByIdTestAndCheckQuantityOfProductsBeforeAndAfter() throws SQLException {
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        List<Product> before = jdbcProductDao.getAll();
        Product product = Product.builder()
                .name("Superman 2")
                .price(25.00)
                .creationDate(LocalDateTime.now())
                .build();
        jdbcProductDao.save(product);
        List<Product> after = jdbcProductDao.getAll();
        assertEquals(before.size()+1, after.size());
    }
}