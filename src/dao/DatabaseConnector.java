/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author HP
 */
public class DatabaseConnector {
    
    private static DatabaseConnector dbCon = new DatabaseConnector();
    
    private  Connection con;
    
    private DatabaseConnector() {
        con = getConnection();
    }
    
    public static DatabaseConnector getInstance() {
        if (dbCon == null) {
            dbCon = new DatabaseConnector();
        }
        return dbCon;
    }
    
    public Connection getConnection() {
        String user = "root";
        String password = "";
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thuvien", user, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return con;
    }
}
