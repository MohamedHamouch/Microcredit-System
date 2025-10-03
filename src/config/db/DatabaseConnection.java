package config.db;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/MicroCredit";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";

    private static Connection connection;

    // private DatabaseConnection() {}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

//    public static Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        }
//        return connection;
//
//    }
}
