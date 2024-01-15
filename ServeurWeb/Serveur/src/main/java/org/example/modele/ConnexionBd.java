package org.example.modele;

import java.sql.*;
import java.util.Hashtable;

public class ConnexionBd {
    private static Connection connection;
    private static final String MYSQL = "MySql";
    private static final String SERVER = "192.168.207.250";
    private static final String DB_NAME = "PourStudent";
    private static final String USER = "Student";
    private static final String PASSWORD = "PassStudent1_";

    private static Hashtable<String, String> drivers;

    static {
        drivers = new Hashtable<>();
        drivers.put(MYSQL, "com.mysql.cj.jdbc.Driver");
    }

    // Constructeur privé pour empêcher l'instanciation directe
    private ConnexionBd() throws SQLException, ClassNotFoundException {
        initializeConnection();
    }

    // Méthode getInstance pour obtenir l'instance unique de la connexion
    public static synchronized ConnexionBd getInstance() throws ClassNotFoundException, SQLException {
        ConnexionBd con = null;
        if (connection == null || connection.isClosed()) {
            con = new ConnexionBd();
        }
        return con;
    }

    private static void initializeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(drivers.get(MYSQL));

        String url = "jdbc:mysql://" + SERVER + "/" + DB_NAME;
        connection = DriverManager.getConnection(url, USER, PASSWORD);
    }

    //faire un select par exemple app dans le get
    public synchronized ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    //faire un update app apres une modif dans le post
    public synchronized int executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    //ferme la connexion a la base de donnée
    public synchronized void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


}


