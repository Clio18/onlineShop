package com.obolonyk.onlineshop.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceCreator {
    private static final String DB_URL = "url";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";

    public static DataSource getDataSource() {
        Properties props = PropertiesReader.getProperties();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(props.getProperty(USER_NAME));
        dataSource.setPassword(props.getProperty(PASSWORD));
        dataSource.setUrl(props.getProperty(DB_URL));
        return dataSource;
    }

}
