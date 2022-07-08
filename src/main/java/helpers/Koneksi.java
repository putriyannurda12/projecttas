package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
;


public class Koneksi {
    public static Connection getConnection(){
        Connection c = null;

        try{
            System.out.println("Connecting...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(
                    "jdbc:mysql://localhost/tas",
                    "root",
                    ""
            );
            System.out.println("Connecting!");
        }catch (ClassNotFoundException e){
            System.err.println("Error Driver");
        }catch (SQLException e){
            System.err.println("Error SQL");
        }
        return c;
    }
}



