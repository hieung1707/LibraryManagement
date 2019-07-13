/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quanlythuvien;

import dao.DatabaseConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *
 * @author HP
 */
public class QuanLyThuVien {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Connection cnt = DatabaseConnector.getInstance().getConnection();
            if (cnt == null)
                System.out.println("Null");
            else {
                Statement stm = cnt.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM sach");
                while (rs.next()) {
                    System.out.println(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
