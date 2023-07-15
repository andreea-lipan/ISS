package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtils {

    private Properties properties;

    public DatabaseUtils(Properties props){
        properties=props;
    }

    private Connection instance = null;

    private Connection getNewConnection(){

        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.pass");

        Connection connection = null;
        try {
            if (username != null && password != null)
                connection = DriverManager.getConnection(url, username, password);
            else
                connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return connection;
    }

    public Connection getConnection(){
        try {
            if (instance == null || instance.isClosed())
                instance=getNewConnection();
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }
}
