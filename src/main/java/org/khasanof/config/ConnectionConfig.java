package org.khasanof.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.khasanof.exception.exceptions.PropertiesNullException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionConfig {
    private static final String genericUsername = PropertyConfig.get("generic.username");
    private static final String genericPassword = PropertyConfig.get("generic.password");
    private static final String genericHost = PropertyConfig.get("generic.host");
    private static final String genericDatabase = PropertyConfig.get("generic.database");
    private static Connection connection;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        checkPropertyValues();
        String genericJdbc = "jdbc:postgresql://".concat(genericHost).concat("/").concat(genericDatabase);
        config.setJdbcUrl(genericJdbc);
        config.setUsername(genericUsername);
        config.setPassword(genericPassword);
        config.addDataSourceProperty("cachePrepsStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    private static void checkPropertyValues() {
        if (Objects.isNull(genericUsername) || Objects.isNull(genericPassword) || Objects.isNull(genericHost) || Objects.isNull(genericDatabase)) {
            throw new PropertiesNullException("property config value is null");
        }
    }

    @Deprecated
    public static Connection getConnection() {
        try {
            if (Objects.isNull(connection) || connection.isClosed()) {
                String genericJdbc = "jdbc:postgresql://".concat(PropertyConfig.get("generic.host")).concat("/").concat(PropertyConfig.get("generic.database"));
                connection = DriverManager.getConnection(genericJdbc, PropertyConfig.get("generic.username"), PropertyConfig.get("generic.password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getHikariConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
