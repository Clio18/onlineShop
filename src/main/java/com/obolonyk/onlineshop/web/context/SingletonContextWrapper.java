package com.obolonyk.onlineshop.web.context;

import com.obolonyk.ioc.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class SingletonContextWrapper {
  private static ApplicationContext context;

    public SingletonContextWrapper(ApplicationContext context) {
        printEnvVariables();
        printSystemVariables();
        this.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
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
