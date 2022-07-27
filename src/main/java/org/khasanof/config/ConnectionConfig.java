package org.khasanof.config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionConfig {
    private static Connection connection;

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

    @SneakyThrows
    public static void close() {
        if (!connection.isClosed()) {
            connection.commit();
            connection.close();
        }
    }
}
