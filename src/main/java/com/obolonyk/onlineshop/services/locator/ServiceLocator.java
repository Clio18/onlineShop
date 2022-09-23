package com.obolonyk.onlineshop.services.locator;

import com.obolonyk.onlineshop.dao.jdbc.JdbcProductDao;
import com.obolonyk.onlineshop.dao.jdbc.JdbcUserDao;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.web.security.service.DefaultSecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.DataSourceCreator;
import com.obolonyk.onlineshop.utils.PropertiesReader;
import com.obolonyk.onlineshop.web.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        Properties props = PropertiesReader.getProperties();
        SERVICES.put(Properties.class, props);

        //config dao
        DataSource dataSource = DataSourceCreator.getDataSource(props);
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);
        SERVICES.put(ProductService.class, productService);

        UserService userService = new UserService();
        userService.setJdbcUserDao(jdbcUserDao);
        SERVICES.put(UserService.class, userService);

        DefaultSecurityService defaultSecurityService = new DefaultSecurityService();
        SERVICES.put(SecurityService.class, defaultSecurityService);

        CartService cartService = new CartService();
        SERVICES.put(CartService.class, cartService);

        printEnvVariables();
        printSystemVariables();
    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(SERVICES.get(clazz));
    }

    //shows all system variables
    private static void printSystemVariables() {
        log.info("=====SYSTEM VARIABLES START=====");
        Properties properties = System.getProperties();
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            log.info(entry.getKey() + " = " + entry.getValue());
        }
        log.info("=====SYSTEM VARIABLES STOP=====");
    }

    //shows all environment variables
    private static void printEnvVariables() {
        log.info("=====ENV VARIABLES START=====");
        Map<String, String> getenv = System.getenv();
        Set<Map.Entry<String, String>> entries = getenv.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            log.info(entry.getKey() + " = " + entry.getValue());
        }
        log.info("====ENV VARIABLES STOP=====");
    }
}
