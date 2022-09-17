package com.obolonyk.onlineshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesReader {
    private static final String PATH_TO_PROPS = "application.properties";

    private static final Map<String, Properties> cachedProps = new ConcurrentHashMap<>();

    public static Properties getProperties() {
        if (cachedProps.isEmpty()) {
            Properties properties = readProperties();
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
