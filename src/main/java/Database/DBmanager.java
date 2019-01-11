package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBmanager class.
 *
 * Class that handles the connection with the database.
 *
 * @author Group 3
 * @version 0.1
 */

public class DBmanager {
    private static final  String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static DBmanager uniqueInstance=null;
    private static Connection connection = null ;

    // checks if a database is running. If not print error string
    public DBmanager(){
        if(!dbExists()) {
            System.err.println(">>> com.main.Database.DBManager: The database doesn't exist ...") ; }
    }

    //if there is no dbmanager instance, create one and return it
    public static synchronized DBmanager getInstance() {
        if (uniqueInstance==null) uniqueInstance = new DBmanager();
        return uniqueInstance;
    }

    //check if the weatherstation query exists
    private Boolean dbExists() {
        boolean exists = false;
        Statement statement = null;
        try {
            if(connection == null) makeConnection();
            statement = connection.createStatement();
            statement.executeQuery( "USE Weatherstation");
        }
        catch( SQLException se ) {
            se.printStackTrace();
        }
        finally {
            try {
                if(statement != null) {
                    statement.close();
                    exists = true;
                }
            }
            catch( SQLException se ) {
                se.printStackTrace();
            }
        }
        return exists;
    }

    //make the connection with the database. Gets properties from the properties file. Please change
    //these paramaters to matc your running database
    public void makeConnection() {
        try{
            Class.forName(JDBC_DRIVER);
            LoadDatabaseProperties properties = new LoadDatabaseProperties("data.properties");
            connection = DriverManager.getConnection(
                    properties.getProperty("jdbc.db_url") +
                            properties.getProperty("jdbc.db_params"),
                    properties.getProperty("jdbc.username"),
                    properties.getProperty("jdbc.password"));
        }
        catch( SQLException se ) {
            System.err.println("Connection error....") ;
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            connection.close();
            uniqueInstance=null;
            connection=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
}