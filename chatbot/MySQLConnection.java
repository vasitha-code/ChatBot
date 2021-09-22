package chatbot;

import java.sql.*;

/**
 * Write a description of class MySQLConnection here.
  *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MySQLConnection
{
    static Connection conn = null;

    public static Connection getConnection(){
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
        }
        
        try {
            conn =
            DriverManager.getConnection("jdbc:mysql://localhost/cameroon?" +
                "user=AppUser&password=appuser");
            return conn;
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        
    }
    
}
