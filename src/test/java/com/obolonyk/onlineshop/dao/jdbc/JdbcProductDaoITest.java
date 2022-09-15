package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.entity.Product;
import com.obolonyk.onlineshop.utils.DataSourceCreator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoITest {

    private DataSource dataSource;
    private JdbcProductDao jdbcProductDao;
    private Flyway flyway;

    @BeforeEach
    void init() {
        dataSource = DataSourceCreator.getDataSource();
        flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        jdbcProductDao = new JdbcProductDao(dataSource);
    }

    @Test
    @DisplayName("getAll Test And Return The List And NotNull And NotNull Fields")
    void getAllTestAndReturnTheListAndCheckSizeAndNotNullAndNotNullFields() {
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
    void getByIdTestAndReturnTheProductAndCheckNotNullAndNotNullFields() {
        Product product = jdbcProductDao.getById(2);
        assertNotNull(product);
        assertNotNull(product.getName());
        assertNotNull(product.getCreationDate());
        assertTrue(product.getId() > 0);
        assertTrue(product.getPrice() >= 0);
    }

    @Test
    @DisplayName("save Test And Check Quantity Of Products Before And After ")
    void saveTestAndCheckQuantityOfProductsBeforeAndAfter() {
        List<Product> before = jdbcProductDao.getAll();
        Product product = Product.builder()
                .name("Superman 2")
                .price(25.00)
                .creationDate(LocalDateTime.now())
                .build();
        jdbcProductDao.save(product);
        List<Product> after = jdbcProductDao.getAll();
        assertEquals(before.size() + 1, after.size());
    }

    @Test
    @DisplayName("remove Test And Check Quantity Of Products Before And After")
    void removeTestAndCheckQuantityOfProductsBeforeAndAfter() {
        List<Product> before = jdbcProductDao.getAll();
        jdbcProductDao.remove(1);
        List<Product> after = jdbcProductDao.getAll();
        assertEquals(before.size() - 1, after.size());
    }

    @Test
    @DisplayName("update Test And Check Fields And Equals And All Size Before And After")
    void updateTestAndCheckFieldsAndEqualsAndAllSizeBeforeAndAfter() {
        Product productBefore = jdbcProductDao.getById(3);
        List<Product> allBefore = jdbcProductDao.getAll();
        Product newProduct = Product.builder()
                .id(3)
                .name("Mario")
                .price(10.00)
                .build();
        jdbcProductDao.update(newProduct);
        Product productAfter = jdbcProductDao.getById(3);
        List<Product> allAfter = jdbcProductDao.getAll();
        assertNotEquals(productBefore, productAfter);
        assertEquals(newProduct.getName(), productAfter.getName());
        assertEquals(newProduct.getPrice(), productAfter.getPrice());
        assertEquals(newProduct.getId(), productAfter.getId());
        assertEquals(allAfter.size(), allBefore.size());
    }

    @Test
    @DisplayName("getBySearch Test And Return The List And NotNull And NotNull Fields")
    void getBySearchTestAndReturnTheListAndCheckSizeAndNotNullAndNotNullFields() {
        String pattern = "DD";
        List<Product> products = jdbcProductDao.getBySearch(pattern);
        assertNotNull(products);
        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
            assertTrue(product.getId() > 0);
            assertTrue(product.getPrice() >= 0);
        }
        assertEquals(1, products.size());
    }
}