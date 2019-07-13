/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author HP
 */
public class Muon {
    private int id;
    private Nguoi nguoi;
    private Sach sach;
    private String ngayMuon;
    private String ngayTra;
    private int soluong;

    public Muon(int id, Nguoi nguoi, Sach sach, String ngayMuon, String ngayTra, int soluong) {
        this.id = id;
        this.nguoi = nguoi;
        this.sach = sach;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.soluong = soluong;
    }

    public int getId() {
        return id;
    }

    public Nguoi getNguoi() {
        return nguoi;
    }

    public Sach getSach() {
        return sach;
    }

    public String getNgayMuon() {
        return ngayMuon;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNguoi(Nguoi nguoi) {
        this.nguoi = nguoi;
    }

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public void setNgayMuon(String ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
