package com.obolonyk.onlineshop.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PropertiesReader {
    private static final String PATH_TO_PROPS = "application.properties";

    private static final Map<String, Properties> cachedProps = new ConcurrentHashMap<>();

    public static Properties getProperties() {
        String env = System.getenv("env");
        if ("production".equalsIgnoreCase(env)) {
            Properties properties = new Properties();
            properties.setProperty("server.port", System.getenv("PORT"));
            properties.setProperty("jdbc.url", System.getenv("JDBC_DATABASE_URL"));
            properties.setProperty("jdbc.user", System.getenv("JDBC_DATABASE_USERNAME"));
            properties.setProperty("jdbc.password", System.getenv("JDBC_DATABASE_PASSWORD"));
            properties.setProperty("durationInSeconds", "3600");
            properties.setProperty("pathToResources", "src/main/resources");

            log.info("Properties were read from environment {}", properties);
            return properties;
        }

        if (cachedProps.isEmpty()) {
            Properties properties = readProperties();
            log.info("Properties were read from application properties file {}", properties);
            return properties;
        } else {
            return cachedProps.get(PATH_TO_PROPS);
        }
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream(PATH_TO_PROPS)) {
            properties.load(inputStream);
            cachedProps.put(PATH_TO_PROPS, properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
