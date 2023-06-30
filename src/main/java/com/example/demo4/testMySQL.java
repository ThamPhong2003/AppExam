package com.example.demo4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testMySQL {
    public static void main(String[] args) {
        // Thông tin kết nối MySQL
        String hostname = "localhost:3306";
        String username = "root";
        String password = "Phong2003";
        String databaseName = "exam";
        String url = "jdbc:mysql://"+hostname+"/"+databaseName;

        // Kết nối tới MySQL Server
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Kết nối thành công!");

            // Thực hiện các thao tác với cơ sở dữ liệu ở đây

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}