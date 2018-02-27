package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBProcessor {

    private Connection connection;
    DBProcessor() throws SQLException {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
    }

    public Connection getConnection(String url, String username, String password) throws SQLException {
        if(connection != null)
            return connection;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
}