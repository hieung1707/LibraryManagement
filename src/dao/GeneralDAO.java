/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Muon;
import entity.Nguoi;
import entity.Sach;
import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class GeneralDAO {
        private Connection con;    
    
    public GeneralDAO() {
        con = DatabaseConnector.getInstance().getConnection();
    }

    public boolean themSach(Sach sach) {
        boolean detectError = false;
        String sql = "INSERT INTO `sach`(`ten`, `tacgia`, `namxuatban`, `soluong`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, sach.getTen());
            stm.setString(2, sach.getTacgia());
            stm.setInt(3, sach.getNamxuatban());
            stm.setInt(4, sach.getSoluong());
            stm.execute();
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public List<Sach> layDSSach() {
        List<Sach> listSach = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sach";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                listSach.add(new Sach(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listSach;
        }
    }
    
    public boolean xoaSach(int id) {
        boolean detectError = false;
        try {
            String sql = "DELETE FROM `sach` WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.execute();
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public boolean suaSach(Sach sach) {
        boolean detectError = false;
        try {
            String sql = "UPDATE sach"
                    + " SET ten = ?, tacgia = ?, namxuatban = ?, soluong = ?"
                    + " WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, sach.getTen());
            stm.setString(2, sach.getTacgia());
            stm.setInt(3, sach.getNamxuatban());
            stm.setInt(4, sach.getSoluong());
            stm.setInt(5, sach.getId());
            int columnsAffected = stm.executeUpdate();
            System.out.println(columnsAffected);
            detectError = columnsAffected <= 0? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public Sach timSach(int id) {
        Sach book = null;
        try {
            String sql = "SELECT * FROM sach WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                book = new Sach(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return book;
        }
    }
    
    
    public boolean themNguoi(Nguoi nguoi) {
        boolean detectError = false;
        String sql = "INSERT INTO `nguoi`(`ten`, `diachi`, `sdt`) VALUES (?, ?, ?)";
        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, nguoi.getTen());
            stm.setString(2, nguoi.getDiachi());
            stm.setString(3, nguoi.getSdt());
            stm.execute();
        } catch (Exception e) {
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public List<Nguoi> layDSNguoi() {
        List<Nguoi> listNguoi = new ArrayList<>();
        try {
            String sql = "SELECT * FROM nguoi";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                listNguoi.add(new Nguoi(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listNguoi;
        }
    }
    
    public boolean suaNguoi(Nguoi nguoi) {
        boolean detectError = false;
        try {
            String sql = "UPDATE nguoi SET ten = ?, diachi = ?, sdt = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, nguoi.getTen());
            stm.setString(2, nguoi.getDiachi());
            stm.setString(3, nguoi.getSdt());
            stm.setInt(4, nguoi.getId());
            int columnsAffected = stm.executeUpdate();
            detectError = columnsAffected <= 0? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public Nguoi timNguoi(int id) {
        Nguoi nguoi = null;
        try {
            String sql = "SELECT * FROM nguoi WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                nguoi = new Nguoi(id, rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return nguoi;
        }
    }
    
    public boolean themPhieuMuon(Muon muon) {
        boolean detectError = false;
        String sql = "INSERT INTO muon (idSach, idNguoi, ngayMuon, ngayTra, soluong) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, muon.getSach().getId());
            stm.setInt(2, muon.getNguoi().getId());
            stm.setString(3, muon.getNgayMuon());
            stm.setString(4, muon.getNgayTra());
            stm.setInt(5, muon.getSoluong());
            stm.execute();
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public boolean xoaNguoi(int id) {
        boolean detectError = false;
        try {
            String sql = "DELETE FROM `nguoi` WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.execute();
        } catch (Exception e) {
            e.printStackTrace();
            detectError = true;
        } finally {
            return detectError;
        }
    }
    
    public List<Muon> layDSMuon() {
        List<Muon> listMuon = new ArrayList<>();
        try {
            String sql = "SELECT * FROM muon";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Sach sach = timSach(rs.getInt(2));
                Nguoi nguoi = timNguoi(rs.getInt(3));
                listMuon.add(new Muon(rs.getInt(1), nguoi, sach, rs.getString(4), rs.getString(5), rs.getInt(6)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listMuon;
        }
    }
}
