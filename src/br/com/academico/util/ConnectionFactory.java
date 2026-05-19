package br.com.academico.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // força o carregamento do driver
            String url      = "jdbc:mysql://localhost:3306/academico";
            String username = "academico";
            String password = "1234"; // se sua instalação do MySQL tiver senha, coloque aqui

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public static void closeConnection(Connection conn) throws Exception {
        close(conn);
    }

    private static void close(Connection conn) throws Exception {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}