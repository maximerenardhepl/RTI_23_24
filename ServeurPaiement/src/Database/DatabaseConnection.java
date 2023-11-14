package Database;

import java.sql.*;
import java.util.Hashtable;

public class DatabaseConnection {
    private Connection connection;
    public static final String MYSQL = "MySql";
    private static Hashtable<String, String> drivers;

    static {
        drivers = new Hashtable<>();
        drivers.put(MYSQL, "com.mysql.cj.jdbc.Driver");
    }

    public DatabaseConnection(String type, String server, String dbName, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(drivers.get(type));

        String url = null;

        switch(type) {
            case MYSQL: url = "jdbc:mysql://" + server + "/" + dbName;
            break;
        }

        connection = DriverManager.getConnection(url, user, password);
    }

    public synchronized ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public synchronized int executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public synchronized void close() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
