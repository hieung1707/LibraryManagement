/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.GeneralDAO;
import entity.Muon;
import entity.Nguoi;
import entity.Sach;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class GiaoDien extends javax.swing.JFrame implements ActionListener, ChangeListener{
    
    private DefaultTableModel modelNguoi;
    private DefaultTableModel modelSach;
    private DefaultTableModel modelMuon;
    
    private List<Nguoi> listNguoi;
    private List<Sach> listSach;
    private List<Muon> listMuon;
    
    private int previousSelectedRow = -1;

    /**
     * Creates new form GiaoDien
     */
    public GiaoDien() {
        initComponents();
        initOtherComponents();
        setLocationRelativeTo(null);
    }
    
    private void initOtherComponents() {
        modelNguoi = (DefaultTableModel) tblNguoi.getModel();
        modelSach = (DefaultTableModel) tblSach.getModel();
        modelMuon = (DefaultTableModel) tblMuon.getModel();
        listNguoi = new ArrayList<>();
        listSach = new ArrayList<>();
        listMuon = new ArrayList<>();
        btnThemSach.addActionListener(this);
        btnSuaSach.addActionListener(this);
        btnXoaSach.addActionListener(this);
        btnThemNguoi.addActionListener(this);
        btnSuaNguoi.addActionListener(this);
        btnXoaNguoi.addActionListener(this);
        btnMuon.addActionListener(this);
        tblSach.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                sachListSelected();
            }
        });
        tblNguoi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                nguoiListSelected();
            }
        });
        tabbedPane.addChangeListener(this);
        
        getListsFromDatabase();
        updateDSSachGUI();
    }
    
    // ham tinh toan logic
    
    private boolean checkDateExceptions(String date1, String date2) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date borrowDate = df.parse(date1);
        Date returnDate = df.parse(date2);
        return (returnDate.before(borrowDate));
    }
    
    private int checkQuantiyByDate(Sach sach, String date) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date borrowDate = df.parse(date);
        int quantityAvailable = sach.getSoluong();
        for (Muon muon : listMuon) {
            if (muon.getSach().getId() == sach.getId()) {
                Date rtDate = df.parse(muon.getNgayTra());
                if (rtDate.compareTo(borrowDate) >= 0)
                    quantityAvailable -= muon.getSoluong();
            }
        }
        return quantityAvailable;
    }
    
    // cap nhat bang va giao dien
    
    public void getListsFromDatabase() {
        getListSachFromDb();
        getListNguoiFromDb();
        getListMuonFromDb();
    }
    
    public void getListSachFromDb() {
        GeneralDAO dao = new GeneralDAO();
        listSach = dao.layDSSach();
    }
    
    public void getListNguoiFromDb() {
        GeneralDAO dao = new GeneralDAO();
        listNguoi = dao.layDSNguoi();
    }
    
    public void getListMuonFromDb() {
        GeneralDAO dao = new GeneralDAO();
        listMuon = dao.layDSMuon();
    }
    
    public void updateDSNguoiGUI() {
        modelNguoi.setRowCount(0);
        for (Nguoi nguoi : listNguoi)
            modelNguoi.addRow(new Object[] {
                nguoi.getId(),
                nguoi.getTen(),
                nguoi.getDiachi(),
                nguoi.getSdt()
            });
    }
        
    public void updateDSSachGUI() {
        modelSach.setRowCount(0);
        for (Sach sach : listSach)
            modelSach.addRow(new Object[] {
                sach.getId(),
                sach.getTen(),
                sach.getTacgia(),
                sach.getNamxuatban(),
                sach.getSoluong()
            });
    }
    
    public void updateDSMuonGUI() {
        modelMuon.setRowCount(0);
        for (Muon muon : listMuon)
            modelMuon.addRow(new Object[] {
                muon.getId(),
                muon.getSach().getTen(),
                muon.getNguoi().getTen(),
                muon.getNgayMuon(),
                muon.getNgayTra(),
                muon.getSoluong()
            });
    }
    
    private void updateComboSach() {
        comboSach.removeAllItems();
        for (Sach s : listSach)
            comboSach.addItem(s.toString());
//        comboSach.updateUI();
    }
    
    private void updateComboNguoi() {
        comboNguoi.removeAllItems();
        for (Nguoi n : listNguoi) {
            comboNguoi.addItem(n.toString());
            System.out.println(n.toString());
        }
//        comboNguoi.updateUI();
    }
    
    // Ham tuong tac khi chon 1 hang trong bang
    
    private void sachListSelected() {
        int index = tblSach.getSelectedRow();
        if (index >= 0) {
            previousSelectedRow = index;
            Sach sach = listSach.get(index);
            editTenSach.setText(sach.getTen());
            editTacGia.setText(sach.getTacgia());
            editNamXB.setText(String.valueOf(sach.getNamxuatban()));
            editSoluongSach.setText(String.valueOf(sach.getSoluong()));
        }
    }
    
    private void nguoiListSelected() {
        int index = tblNguoi.getSelectedRow();
        if (index >= 0) {
            previousSelectedRow = index;
            Nguoi nguoi = listNguoi.get(index);
            editTenNguoi.setText(nguoi.getTen());
            editDiaChi.setText(nguoi.getDiachi());
            editSdt.setText(nguoi.getSdt());
        }
    }
    
    // Ham tuong tac cua cac nut
    
    private void btnThemSachClick() {
        try {
            String ten = editTenSach.getText().trim(); // lay ten sach tu edittext
            String tacgia = editTacGia.getText().trim(); // lay ten tac gia tu edittext 
            String namXB = editNamXB.getText().trim(); // lay nam xuat ban tu edittext
            String soluong = editSoluongSach.getText().trim(); // lay so luong tu edittext
            System.out.println(ten + " " + tacgia + " " + namXB + " " + soluong);
            if (ten.equals("") || tacgia.equals("") || namXB.equals("") || soluong.equals("")) throw new Exception(); // kiem tra neu rong thi bat ngoai le
            int namXBInt = Integer.parseInt(namXB);
            int soluongInt = Integer.parseInt(soluong);
            Sach sach = new Sach(0, ten, tacgia, namXBInt, soluongInt);
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.themSach(sach);
            previousSelectedRow = -1;
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Thêm thành công");
                getListSachFromDb();
                updateDSSachGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thiếu hoặc sai đầu vào");
        }     
    }
    
    private void btnSuaSachClick() {
        try {
            if (previousSelectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Chưa chọn cột nào");
                return;
            }
            String ten = editTenSach.getText().trim(); // lay ten sach tu edittext
            String tacgia = editTacGia.getText().trim(); // lay ten tac gia tu edittext 
            String namXB = editNamXB.getText().trim(); // lay nam xuat ban tu edittext
            String soluong = editSoluongSach.getText().trim(); // lay so luong tu edittext
            if (ten.equals("") || tacgia.equals("") || namXB.equals("") || soluong.equals("")) throw new Exception(); // kiem tra neu rong thi bat ngoai le
            int namXBInt = Integer.parseInt(namXB);
            int soluongInt = Integer.parseInt(soluong);
            Sach sach = new Sach(listSach.get(previousSelectedRow).getId(), ten, tacgia, namXBInt, soluongInt);
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.suaSach(sach);
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Sửa thành công");
                getListSachFromDb();
                updateDSSachGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Sửa thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thiếu hoặc sai đầu vào");
        }
    }
    
    private void btnXoaSachClick() {
        try {
            int checkConfirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa cuốn sách này chứ?");
            if (checkConfirm == 1)
                return;
            int i = tblSach.getSelectedColumn();
            if (i < 0) throw new Exception();
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.xoaSach(listSach.get(i).getId());
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Xóa thành công");
                getListSachFromDb();
                updateDSSachGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Xóa thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Chưa chọn cột nào");
        }
    }
    
    private void btnThemNguoiClick() {
        try {
            String ten = editTenNguoi.getText().trim(); // lay ten nguoi tu edittext
            String sdt = editSdt.getText().trim(); // lay so dien thoai tu edittext 
            String diachi = editDiaChi.getText().trim(); // lay dia chi tu edittext
            if (ten.equals("") || sdt.equals("") || diachi.equals("")) throw new Exception(); // kiem tra neu rong thi bat ngoai le
            Nguoi nguoi = new Nguoi(0, ten, diachi, sdt);
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.themNguoi(nguoi);
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Thêm thành công");
                getListNguoiFromDb();
                updateDSNguoiGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thiếu hoặc sai đầu vào");
        }     
    }
    
    private void btnSuaNguoiClick() {
        try {
            if (previousSelectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Chưa chọn cột nào");
                return;
            }
            String ten = editTenNguoi.getText().trim(); // lay ten nguoi tu edittext
            String sdt = editSdt.getText().trim(); // lay so dien thoai tu edittext 
            String diachi = editDiaChi.getText().trim(); // lay dia chi tu edittext
            if (ten.equals("") || sdt.equals("") || diachi.equals("")) throw new Exception(); // kiem tra neu rong thi bat ngoai le
            Nguoi nguoi = new Nguoi(listNguoi.get(previousSelectedRow).getId(), ten, diachi, sdt);
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.suaNguoi(nguoi);
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Sửa thành công");
                getListNguoiFromDb();
                updateDSNguoiGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Sửa thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thiếu hoặc sai đầu vào");
        }
    }
    
    private void btnXoaNguoiClick() {
        try {
            int checkConfirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa người này chứ?");
            if (checkConfirm == 1)
                return;
            int i = tblNguoi.getSelectedRow();
            if (i < 0) throw new Exception();
            GeneralDAO dao = new GeneralDAO();
            boolean detectError = dao.xoaNguoi(listNguoi.get(i).getId());
            if (!detectError) {
                JOptionPane.showConfirmDialog(this, "Xóa thành công");
                getListNguoiFromDb();
                updateDSNguoiGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Xóa thất bại. Có lỗi xảy ra");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Chưa chọn cột nào");
        }
    }
    
    private void btnThemMuonClick() {
        try {
            int indexNguoi = comboNguoi.getSelectedIndex();
            int indexSach = comboSach.getSelectedIndex();
            Sach sach = listSach.get(indexSach);
            Nguoi nguoi = listNguoi.get(indexNguoi);
            String ngayMuon = editNgayMuon.getText().trim();
            String ngayTra = editNgayTra.getText().trim();
            String soluong = editSoluongMuon.getText().trim();
            int soluongInt = Integer.parseInt(soluong);
            if (checkDateExceptions(ngayMuon, ngayTra)) throw new Exception();
            GeneralDAO dao = new GeneralDAO();
            int quantityAvaiable = checkQuantiyByDate(sach, ngayMuon);
            if (quantityAvaiable < soluongInt) {
                JOptionPane.showConfirmDialog(this, "Số lượng mượn vượt quá số sách trong kho tại thời điểm mượn. Trong kho chỉ còn " + String.valueOf(quantityAvaiable));
                return;
            }
            Muon muon = new Muon(0, nguoi, sach, ngayMuon, ngayTra, soluongInt);
            boolean detectError = dao.themPhieuMuon(muon);
            if (!detectError) {
                JOptionPane.showMessageDialog(this, "Mượn thành công");
                getListMuonFromDb();
                updateDSMuonGUI();
            }
            else
                JOptionPane.showMessageDialog(this, "Mượn thất bại. Đã xẩy ra lỗi");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thiếu hoặc sai đầu vào");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        editTenSach = new javax.swing.JTextField();
        editTacGia = new javax.swing.JTextField();
        editNamXB = new javax.swing.JTextField();
        editSoluongSach = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSach = new javax.swing.JTable();
        btnThemSach = new javax.swing.JButton();
        btnSuaSach = new javax.swing.JButton();
        btnXoaSach = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        editTenNguoi = new javax.swing.JTextField();
        editDiaChi = new javax.swing.JTextField();
        editSdt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNguoi = new javax.swing.JTable();
        btnThemNguoi = new javax.swing.JButton();
        btnSuaNguoi = new javax.swing.JButton();
        btnXoaNguoi = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        editNgayMuon = new javax.swing.JTextField();
        editNgayTra = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMuon = new javax.swing.JTable();
        comboNguoi = new javax.swing.JComboBox<>();
        comboSach = new javax.swing.JComboBox<>();
        editSoluongMuon = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnMuon = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Thông tin Sách");

        editTacGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTacGiaActionPerformed(evt);
            }
        });

        jLabel2.setText("Tên");

        jLabel3.setText("Tác giả");

        jLabel4.setText("Năm XB");

        jLabel5.setText("Số lượng");

        tblSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Tên", "Tác giả", "Năm xuất bản", "Số lượng"
            }
        ));
        jScrollPane1.setViewportView(tblSach);

        btnThemSach.setText("Thêm");
        btnThemSach.setActionCommand("them_sach");

        btnSuaSach.setText("Sửa");
        btnSuaSach.setActionCommand("sua_sach");

        btnXoaSach.setText("Xóa");
        btnXoaSach.setActionCommand("xoa_sach");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editSoluongSach, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editNamXB, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnThemSach)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuaSach)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoaSach))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnSuaSach, btnThemSach, btnXoaSach});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(editNamXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editSoluongSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemSach)
                    .addComponent(btnSuaSach)
                    .addComponent(btnXoaSach))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Sách", jPanel1);

        jLabel6.setText("Thông tin người mượn");

        editDiaChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDiaChiActionPerformed(evt);
            }
        });

        jLabel7.setText("Tên");

        jLabel8.setText("Địa chỉ");

        jLabel9.setText("Số điện thoại");

        tblNguoi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Tên", "Địa chỉ", "Số điện thoại"
            }
        ));
        jScrollPane2.setViewportView(tblNguoi);

        btnThemNguoi.setText("Thêm");
        btnThemNguoi.setActionCommand("them_nguoi");

        btnSuaNguoi.setText("Sửa");
        btnSuaNguoi.setActionCommand("sua_nguoi");

        btnXoaNguoi.setText("Xóa");
        btnXoaNguoi.setActionCommand("xoa_nguoi");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editTenNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(editSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnThemNguoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuaNguoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoaNguoi))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnSuaNguoi, btnThemNguoi, btnXoaNguoi});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editTenNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(editDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)))
                            .addComponent(editSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNguoi)
                    .addComponent(btnSuaNguoi)
                    .addComponent(btnXoaNguoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Người", jPanel4);

        jLabel11.setText("Thông tin mượn");

        editNgayMuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNgayMuonActionPerformed(evt);
            }
        });

        jLabel12.setText("Người mượn");

        jLabel13.setText("Ngày mượn");

        jLabel14.setText("Sách");

        jLabel15.setText("Số lượng");

        tblMuon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Sách", "Người mượn", "Ngày mượn", "Ngày trả", "Số lượng"
            }
        ));
        jScrollPane3.setViewportView(tblMuon);

        comboNguoi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboSach.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        editSoluongMuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSoluongMuonActionPerformed(evt);
            }
        });

        jLabel16.setText("Ngày trả");

        btnMuon.setText("Mượn");
        btnMuon.setActionCommand("muon");

        jLabel10.setText("Format ngày: dd/MM/yyyy");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnMuon))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(editSoluongMuon, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editNgayMuon, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(comboNguoi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editNgayTra, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(comboSach, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {comboNguoi, comboSach, editNgayMuon, editNgayTra, editSoluongMuon});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel12, jLabel13, jLabel14, jLabel15, jLabel16});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(comboNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editSoluongMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel10))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMuon)
                .addContainerGap())
        );

        tabbedPane.addTab("Mượn", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editTacGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTacGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editTacGiaActionPerformed

    private void editDiaChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDiaChiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editDiaChiActionPerformed

    private void editNgayMuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNgayMuonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editNgayMuonActionPerformed

    private void editSoluongMuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSoluongMuonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editSoluongMuonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GiaoDien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GiaoDien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GiaoDien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GiaoDien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GiaoDien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMuon;
    private javax.swing.JButton btnSuaNguoi;
    private javax.swing.JButton btnSuaSach;
    private javax.swing.JButton btnThemNguoi;
    private javax.swing.JButton btnThemSach;
    private javax.swing.JButton btnXoaNguoi;
    private javax.swing.JButton btnXoaSach;
    private javax.swing.JComboBox<String> comboNguoi;
    private javax.swing.JComboBox<String> comboSach;
    private javax.swing.JTextField editDiaChi;
    private javax.swing.JTextField editNamXB;
    private javax.swing.JTextField editNgayMuon;
    private javax.swing.JTextField editNgayTra;
    private javax.swing.JTextField editSdt;
    private javax.swing.JTextField editSoluongMuon;
    private javax.swing.JTextField editSoluongSach;
    private javax.swing.JTextField editTacGia;
    private javax.swing.JTextField editTenNguoi;
    private javax.swing.JTextField editTenSach;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tblMuon;
    private javax.swing.JTable tblNguoi;
    private javax.swing.JTable tblSach;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        switch (action) {
            case "them_sach":
                btnThemSachClick();
                break;
            case "sua_sach":
                btnSuaSachClick();
                break;
            case "xoa_sach":
                btnXoaSachClick();
                break;
            case "them_nguoi":
                btnThemNguoiClick();
                break;
            case "sua_nguoi":
                btnSuaNguoiClick();
                break;
            case "xoa_nguoi":
                btnXoaNguoiClick();
                break;
            case "muon":
                btnThemMuonClick();
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        int index = tabbedPane.getSelectedIndex();
        previousSelectedRow = -1;
        switch (index) {
            case 0:
                updateDSSachGUI();
                break;
            case 1:
                updateDSNguoiGUI();
                break;
            case 2:
                updateComboNguoi();
                updateComboSach();
                updateDSMuonGUI();
                break;
        }
    }
}
