package com.controleestoque.config;

import java.sql.Connection;
import java.sql.SQLException;

import com.controleestoque.persistence.connection.DatabaseConnection;

public class DatabaseConfig {

    private static final DatabaseConnection INSTANCE = new DatabaseConnection(
            "localhost", "controleEstoque", "sa", "P4ssw0rd");

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        return INSTANCE.getConnection();
    }
}
