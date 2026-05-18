package br.com.academico.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection getConnection() throws Exception {
        try {
            // A linha do Class.forName foi removida (não é mais necessária)
            String url = "jdbc:mysql://localhost:3306/academico";
            String username = "root";
            String password = ""; // <-- CORRIGIDO: aspas totalmente vazias (sem espaço dentro)

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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