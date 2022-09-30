package com.obolonyk.onlineshop.dao.jdbc;

import com.obolonyk.onlineshop.entity.User;
import com.obolonyk.onlineshop.utils.DataSourceFactory;
import com.obolonyk.onlineshop.utils.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoITest {
    private DataSource dataSource;
    private JdbcUserDao jdbcUserDao;
    private Flyway flyway;

    @BeforeEach
    void init() {
        PropertiesReader propertiesReader = new PropertiesReader();
        Properties props = propertiesReader.getProperties();
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSource = dataSourceFactory.getDataSource(props);;
        flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        DataSource dataSource = dataSourceFactory.getDataSource(props);
        jdbcUserDao = new JdbcUserDao();
        jdbcUserDao.setDataSource(dataSource);
    }

    @Test
    @DisplayName("getByLogin Test And Return The User")
    void getByLoginTestAndReturnTheUserAndCheckNotNullAndNotNullFields() {
        Optional<User> optionalUser = jdbcUserDao.getByLogin("AAA");
        User user = optionalUser.get();
        assertNotNull(user);
        assertNotNull(user.getName());
        assertNotNull(user.getLastName());
        assertNotNull(user.getPassword());
        assertNotNull(user.getSalt());
        assertNotNull(user.getEmail());
    }

    @Test
    @DisplayName("getByLogin Test And Return Empty Optional")
    void getByLoginTestAndReturnEmptyOptional() {
        Optional<User> optionalUser = jdbcUserDao.getByLogin("vvv");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    @DisplayName("save Test And Check Quantity Of Products Before And After ")
    void saveTestAndCheckQuantityOfProductsBeforeAndAfter() {
        User user = User.builder()
                .name("c")
                .lastName("cc")
                .email("ccc")
                .login("cccc")
                .password("ccccc")
                .salt("cccccc")
                .build();
        jdbcUserDao.save(user);
        Optional<User> optionalUser = jdbcUserDao.getByLogin("cccc");
        User userFromDB = optionalUser.get();
        assertNotNull(userFromDB);
        assertNotNull(userFromDB.getName());
        assertNotNull(userFromDB.getLastName());
        assertNotNull(userFromDB.getPassword());
        assertNotNull(userFromDB.getSalt());
        assertNotNull(userFromDB.getEmail());
    }
}