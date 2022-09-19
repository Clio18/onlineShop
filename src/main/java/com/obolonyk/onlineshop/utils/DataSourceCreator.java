package com.obolonyk.onlineshop.utils;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceCreator {
    private static final String DB_URL = "jdbc.url";
    private static final String USER_NAME = "jdbc.user";
    private static final String PASSWORD = "jdbc.password";

    public static DataSource getDataSource(Properties props) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(props.getProperty(USER_NAME));
        dataSource.setPassword(props.getProperty(PASSWORD));
        dataSource.setUrl(props.getProperty(DB_URL));
        return dataSource;
    }

}
