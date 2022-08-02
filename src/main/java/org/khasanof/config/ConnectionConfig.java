package org.khasanof.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionConfig {
    private static Connection connection;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        config.setJdbcUrl(PropertyConfig.get("db.jdbc"));
        config.setUsername(PropertyConfig.get("db.username"));
        config.setPassword(PropertyConfig.get("db.password"));
        config.addDataSourceProperty("cachePrepsStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    @Deprecated
    public static Connection getConnection() {
        try {
            if (Objects.isNull(connection) || connection.isClosed()) {
                connection = DriverManager.getConnection(PropertyConfig.get("db.jdbc"), PropertyConfig.get("db.username"), PropertyConfig.get("db.password"));
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

    @SneakyThrows
    @Deprecated
    public static void close() {
        if (!connection.isClosed()) {
            connection.commit();
            connection.close();
        }
    }
}
