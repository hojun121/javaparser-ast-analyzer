package analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static Connection dbConn;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (dbConn == null) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/project?serverTimezone=UTC";
            String user = "root";
            String password = "password";
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConn = DriverManager.getConnection(jdbcUrl, user, password);
        }
        return dbConn;
    }

    public static Connection getConnection(String url, String user, String pwd, String driverName) throws ClassNotFoundException, SQLException {
        if (dbConn == null) {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(url, user, pwd);
        }
        return dbConn;
    }

    public static void close() throws SQLException {
        if (dbConn != null) {
            if (!dbConn.isClosed()) {
                dbConn.close();
            }
        }

        dbConn = null;

    }
}