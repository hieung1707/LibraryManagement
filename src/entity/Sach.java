/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author HP
 */
public class Sach {
    private int id;
    private String ten;
    private String tacgia;
    private int namxuatban;
    private int soluong;
    
    public Sach(){}

    public Sach(int id, String ten, String tacgia, int namxuatban, int soluong) {
        this.id = id;
        this.ten = ten;
        this.tacgia = tacgia;
        this.namxuatban = namxuatban;
        this.soluong = soluong;
    }
    
    public int getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public String getTacgia() {
        return tacgia;
    }

    public int getNamxuatban() {
        return namxuatban;
    }

    public int getSoluong() {
        return soluong;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public void setNamxuatban(int namxuatban) {
        this.namxuatban = namxuatban;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public String toString() {
        return String.valueOf(id) + " - " + ten;
    }
}
