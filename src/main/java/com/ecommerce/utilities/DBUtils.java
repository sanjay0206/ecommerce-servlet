package com.ecommerce.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "java@2021";
    private static Connection conn = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            String SQL = "SET FOREIGN_KEY_CHECKS = 0;" +
                    "TRUNCATE TABLE products;" +
                    "TRUNCATE TABLE orders;" +
                    "TRUNCATE TABLE customers;" +
                    "SET FOREIGN_KEY_CHECKS = 1";
            stmt.executeQuery(SQL);
            System.out.println("connected to db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
