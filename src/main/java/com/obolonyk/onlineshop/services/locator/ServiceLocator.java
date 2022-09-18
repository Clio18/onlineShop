package com.obolonyk.onlineshop.services.locator;

import com.obolonyk.onlineshop.dao.jdbc.JdbcProductDao;
import com.obolonyk.onlineshop.dao.jdbc.JdbcUserDao;
import com.obolonyk.onlineshop.services.CartService;
import com.obolonyk.onlineshop.services.ProductService;
import com.obolonyk.onlineshop.services.SecurityService;
import com.obolonyk.onlineshop.services.UserService;
import com.obolonyk.onlineshop.utils.DataSourceCreator;
import com.obolonyk.onlineshop.utils.PageGenerator;
import com.obolonyk.onlineshop.utils.PropertiesReader;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        Properties props = PropertiesReader.getProperties();
        int duration = Integer.parseInt(props.getProperty("durationInSeconds"));
        DataSource dataSource = DataSourceCreator.getDataSource(props);
        PageGenerator pageGenerator = PageGenerator.instance();
        SERVICES.put(PageGenerator.class, pageGenerator);

        //config dao
        JdbcProductDao jdbcProductDao = new JdbcProductDao(dataSource);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);

        //flyway
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        SERVICES.put(Flyway.class, flyway);

        //config services
        ProductService productService = new ProductService();
        productService.setJdbcProductDao(jdbcProductDao);
        SERVICES.put(ProductService.class, productService);

        UserService userService = new UserService();
        userService.setJdbcUserDao(jdbcUserDao);
        SERVICES.put(UserService.class, userService);

        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);
        securityService.setDurationInSeconds(duration);
        SERVICES.put(SecurityService.class, securityService);

        CartService cartService = new CartService();
        SERVICES.put(CartService.class, cartService);
    }

    public static <T> T getService(Class<T> clazz){
        return clazz.cast(SERVICES.get(clazz));
    }
}
