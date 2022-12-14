package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.entity.Product;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath:applicationContext-test.xml"})
class JdbcProductDaoITest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcProductDao jdbcProductDao;

    @BeforeEach
    void init() {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }

    @Test
    @DisplayName("GetAll Test And Return The List And NotNull And NotNull Fields")
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
    @DisplayName("GetById Test And Return The Product")
    void getByIdTestAndReturnTheProductAndCheckNotNullAndNotNullFields() {
        Product product = jdbcProductDao.getById(2);
        assertNotNull(product);
        assertNotNull(product.getName());
        assertNotNull(product.getCreationDate());
        assertTrue(product.getId() > 0);
        assertTrue(product.getPrice() >= 0);
    }

    @Test
    @DisplayName("Save Test And Check Quantity Of Products Before And After ")
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
    @DisplayName("Remove Test And Check Quantity Of Products Before And After")
    void removeTestAndCheckQuantityOfProductsBeforeAndAfter() {
        List<Product> before = jdbcProductDao.getAll();
        jdbcProductDao.remove(1);
        List<Product> after = jdbcProductDao.getAll();
        assertEquals(before.size() - 1, after.size());
    }

    @Test
    @DisplayName("Update Test And Check Fields And Equals And All Size Before And After")
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
    @DisplayName("GetBySearch Test And Return The List And NotNull And NotNull Fields")
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