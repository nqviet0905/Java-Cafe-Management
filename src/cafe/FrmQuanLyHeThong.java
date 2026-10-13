package cafe;



import javax.swing.*;

import javax.swing.border.EmptyBorder;

import javax.swing.border.TitledBorder;

import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.util.ArrayList;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import javax.swing.text.AttributeSet;

import javax.swing.text.BadLocationException;



public class FrmQuanLyHeThong extends JFrame {



    class OnlyNumberFilter extends javax.swing.text.DocumentFilter {



        @Override

        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)

                throws BadLocationException {

            if (string != null && string.matches("\\d+")) {

                super.insertString(fb, offset, string, attr);

            }

        }



        @Override

        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)

                throws BadLocationException {

            if (text != null && text.matches("\\d+")) {

                super.replace(fb, offset, length, text, attrs);

            }

        }

    }



    private JTable table;

    private DefaultTableModel tableModel;

    private MyConnection db;

    private JPanel pnlContent;





    public FrmQuanLyHeThong() {

        db = new MyConnection();

        initComponents();

        loadData();



        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override

            public void windowClosing(java.awt.event.WindowEvent e) {

                new Frm_TrangChu().setVisible(true);

                dispose(); 

            }

        });



    }



    private void initComponents() {

        setTitle("Admin - Quản Lý Hệ Thống");

        setSize(1250, 720);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(249, 245, 240));



        JPanel pnlHeader = new JPanel(new GridLayout(1, 8, 10, 10));

        pnlHeader.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlHeader.setBackground(new Color(135, 206, 250));



       // ===== HÓA ĐƠN =====

        JButton btnHoaDon = createNavButton("HÓA ĐƠN", "📄");

        btnHoaDon.addActionListener(e -> {

            new HoaDon().setVisible(true);

            dispose();

        });

        pnlHeader.add(btnHoaDon);

        

       // ===== DOANH THU (ĐÃ KẾT NỐI) =====

        JButton btnDoanhThu = createNavButton("DOANH THU", "💰");

        btnDoanhThu.addActionListener(e -> {

            new FrmDoanhThu().setVisible(true);

            dispose();

        });

        pnlHeader.add(btnDoanhThu);

        

        JButton btnSanPham = createNavButton("SẢN PHẨM", "🍹");

        // --- Thêm đoạn kết nối này vào bên dưới ---

        btnSanPham.addActionListener(new java.awt.event.ActionListener() {

         @Override

         public void actionPerformed(java.awt.event.ActionEvent e) {

              // Khởi tạo form sản phẩm

               new FrmSanPham().setVisible(true); 

               // Đóng form quản lý hệ thống hiện tại

               dispose(); 

          }

});

        pnlHeader.add(btnSanPham);





       JButton btnLoai = createNavButton("LOẠI", "🔲");

    btnLoai.addActionListener(e -> {

    new FrmLoaisanpham().setVisible(true);

    dispose(); // nếu muốn đóng FrmQuanLyHeThong

});

    pnlHeader.add(btnLoai);



     // Nút Nguyên Liệu

        JButton btnNguyenLieu = createNavButton("NGUYÊN LIỆU", "🍾");

        btnNguyenLieu.addActionListener(e -> {

            new QuanLyNguyenLieu().setVisible(true);

            dispose();

        });

        pnlHeader.add(btnNguyenLieu);



        // Tìm chỗ này trong FrmQuanLyHeThong.java

        JButton btnBanNav = createNavButton("BÀN", "🍸");



        btnBanNav.addActionListener(new java.awt.event.ActionListener() {

            @Override

            public void actionPerformed(java.awt.event.ActionEvent e) {

                new FrmBan().setVisible(true); // Mở trang Bàn (đã viết hoa F)

                dispose(); // Đóng trang hiện tại

            }

        });



// Thêm vào thanh Header

        pnlHeader.add(btnBanNav);



        JButton btnTaiKhoan = createNavButton("TÀI KHOẢN", "👤");

        btnTaiKhoan.setBackground(new Color(249, 245, 240));

        pnlHeader.add(btnTaiKhoan);

       // Nút Lương

        JButton btnLuong = createNavButton("LƯƠNG", "💴");

        btnLuong.addActionListener(e -> {

            // Mở form Luong

            Luong luongForm = new Luong();

            luongForm.setVisible(true);

            luongForm.toFront();

        });

        pnlHeader.add(btnLuong);



        add(pnlHeader, BorderLayout.NORTH);



        pnlContent = new JPanel(new BorderLayout());

        pnlContent.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnlContent.setOpaque(false);



        

        JPanel pnlLeft = new JPanel(new BorderLayout());

        pnlLeft.setOpaque(false);



        JPanel pnlForm = new JPanel(null);

        pnlForm.setBorder(new TitledBorder("Thông tin tài khoản"));

        pnlForm.setOpaque(false);



        Font f = new Font("Segoe UI", Font.PLAIN, 14);



        JLabel lblMa = new JLabel("Mã nhân viên:");

        lblMa.setBounds(10, 40, 110, 25);

        JTextField txtMa = new JTextField();

        txtMa.setBounds(120, 40, 130, 25);

        JLabel lblMKMacDinh = new JLabel("Mật khẩu mặc định:");

        lblMKMacDinh.setBounds(10, 75, 130, 25);



        JLabel lblMKValue = new JLabel("1234567");

        lblMKValue.setBounds(125, 75, 130, 25);

        lblMKValue.setForeground(Color.RED);



        JLabel lblTen = new JLabel("Họ và tên:");

        lblTen.setBounds(270, 40, 80, 25);

        JTextField txtTen = new JTextField();

        txtTen.setBounds(350, 40, 160, 25);



        JLabel lblCMND = new JLabel("CCCD:");

        lblCMND.setBounds(270, 100, 80, 25);

        JTextField txtCMND = new JTextField();

        txtCMND.setBounds(350, 100, 160, 25);



        ((javax.swing.text.AbstractDocument) txtCMND.getDocument())

                .setDocumentFilter(new OnlyNumberFilter());



        JLabel lblSdt = new JLabel("Số điện thoại:");

        lblSdt.setBounds(10, 100, 110, 25);

        JTextField txtSdt = new JTextField();

        txtSdt.setBounds(120, 100, 130, 25);



        ((javax.swing.text.AbstractDocument) txtSdt.getDocument())

                .setDocumentFilter(new OnlyNumberFilter());



        JLabel lblDiaChi = new JLabel("Địa chỉ:");

        lblDiaChi.setBounds(10, 140, 110, 25);

        JTextArea txtDiaChi = new JTextArea();

        JScrollPane spDiaChi = new JScrollPane(txtDiaChi);

        spDiaChi.setBounds(120, 140, 390, 80);



        JLabel lblQueQuan = new JLabel("Quê quán:");

        lblQueQuan.setBounds(10, 230, 110, 25);

        JTextField txtQueQuan = new JTextField();

        txtQueQuan.setBounds(120, 230, 130, 25);



        JLabel lblChucVu = new JLabel("Chức vụ:");

        lblChucVu.setBounds(10, 270, 110, 25);

        JComboBox<String> cbChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});

        cbChucVu.setBounds(120, 270, 130, 25);



        JLabel lblTrangThai = new JLabel("Trạng thái:");

        lblTrangThai.setBounds(270, 270, 80, 25);

        JRadioButton rbMo = new JRadioButton("Mở khóa", true);

        rbMo.setBounds(350, 270, 90, 25);

        JRadioButton rbKhoa = new JRadioButton("Khóa");

        rbKhoa.setBounds(440, 270, 70, 25);

        ButtonGroup bg = new ButtonGroup();

        bg.add(rbMo);

        bg.add(rbKhoa);



        JLabel lblLuong = new JLabel("Lương:");

        lblLuong.setBounds(10, 310, 110, 25);

        JTextField txtLuong = new JTextField();

        txtLuong.setBounds(120, 310, 130, 25);

        ((javax.swing.text.AbstractDocument) txtLuong.getDocument())

                .setDocumentFilter(new OnlyNumberFilter());



        pnlForm.add(lblMa); pnlForm.add(txtMa);

        pnlForm.add(lblTen); pnlForm.add(txtTen);

        pnlForm.add(lblMKMacDinh); pnlForm.add(lblMKValue);

        pnlForm.add(lblCMND); pnlForm.add(txtCMND);

        pnlForm.add(lblSdt); pnlForm.add(txtSdt);

        pnlForm.add(lblDiaChi); pnlForm.add(spDiaChi);

        pnlForm.add(lblQueQuan); pnlForm.add(txtQueQuan);

        pnlForm.add(lblChucVu); pnlForm.add(cbChucVu);

        pnlForm.add(lblTrangThai); pnlForm.add(rbMo); pnlForm.add(rbKhoa);

        pnlForm.add(lblLuong); pnlForm.add(txtLuong);



        pnlLeft.add(pnlForm, BorderLayout.CENTER);





        // BUTTONS PANEL

        JPanel pnlButtons = new JPanel(new GridLayout(2, 3, 10, 10));

        pnlButtons.setOpaque(false);

        pnlButtons.setBorder(new EmptyBorder(20, 0, 0, 0));



        JButton btnThem = createActionButton("Thêm mới");

        JButton btnXoa = createActionButton("Xóa tài khoản");

        JButton btnThoat = createActionButton("Thoát");

        JButton btnLichsu = createActionButton("Lịch Sử đăng nhập");

        JButton btnCapNhat = createActionButton("Cập nhật");

        JButton btnKhoiPhuc = createActionButton("Khôi phục mật khẩu");



        pnlButtons.add(btnThem);

        pnlButtons.add(btnXoa);

        pnlButtons.add(btnThoat);

        pnlButtons.add(btnLichsu);

        pnlButtons.add(btnCapNhat);

        pnlButtons.add(btnKhoiPhuc);



        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);



        // TABLE PANEL

        String[] col = {"Mã", "Tên", "CMND", "Điện thoại", "Địa chỉ", "Quê Quán", "Chức vụ", "Trạng thái", "Lương"};

        tableModel = new DefaultTableModel(col, 0);

        table = new JTable(tableModel);

        table.setRowHeight(28);

        table.setFont(f);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        table.getTableHeader().setBackground(Color.WHITE);



        JScrollPane sp = new JScrollPane(table);



      JPanel pnlTaiKhoan = new JPanel(new GridLayout(1, 2, 10, 0));

        pnlTaiKhoan.setOpaque(false);



        pnlTaiKhoan.add(pnlLeft);

        pnlTaiKhoan.add(sp);



        pnlContent.add(pnlTaiKhoan, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);





        LocalDate today = LocalDate.now();

        String ngayHomNay = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));



        JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));

        pnlStatus.add(new JLabel("Nhân viên đang đăng nhập: admin  |  Ngày hôm nay: " + ngayHomNay));

        pnlStatus.setOpaque(false);

        add(pnlStatus, BorderLayout.SOUTH);



       // TABLE SELECTION

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                txtMa.setText(table.getValueAt(row, 0).toString());

                txtTen.setText(table.getValueAt(row, 1).toString());

                txtCMND.setText(table.getValueAt(row, 2).toString());

                txtSdt.setText(table.getValueAt(row, 3).toString());

                txtDiaChi.setText(table.getValueAt(row, 4).toString());
                
                txtQueQuan.setText(table.getValueAt(row, 5).toString());  


                cbChucVu.setSelectedItem(table.getValueAt(row, 6).toString());

                if (table.getValueAt(row, 7).toString().equals("Mở khóa")) rbMo.setSelected(true);

                else rbKhoa.setSelected(true);

                txtLuong.setText(table.getValueAt(row, 8).toString());

            }

        });

        // BUTTON ACTIONS

        btnThem.addActionListener(e -> addTaiKhoan(txtMa, txtTen, txtCMND, txtSdt, txtDiaChi, txtQueQuan, cbChucVu, rbMo, txtLuong));

        btnCapNhat.addActionListener(e -> updateTaiKhoan(txtMa, txtTen, txtCMND, txtSdt, txtDiaChi,txtQueQuan, cbChucVu, rbMo, txtLuong));

        btnThoat.addActionListener(e -> {

            new Frm_TrangChu().setVisible(true);

            dispose();

        });

        btnLichsu.addActionListener(e -> {

            new FrmLichSuDangNhap().setVisible(true);

        });



        btnXoa.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");

                return;

            }



            String ma = table.getValueAt(row, 0).toString();  // cột Mã nhân viên

            String ten = table.getValueAt(row, 1).toString(); // cột Tên



            int confirm = JOptionPane.showConfirmDialog(

                    this,

                    "Bạn có chắc chắn muốn xóa tài khoản:\n" + ten + " (Mã: " + ma + ")?",

                    "Xác nhận xóa",

                    JOptionPane.YES_NO_OPTION

            );



            if (confirm == JOptionPane.YES_OPTION) {

                try {

                    db.deleteTaiKhoan(Integer.parseInt(ma));

                    loadData();

                    JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(this, "Lỗi xóa tài khoản: " + ex.getMessage());

                }

            }

        });



        btnKhoiPhuc.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần khôi phục mật khẩu!");

                return;

            }



            String ma = table.getValueAt(row, 0).toString();

            String ten = table.getValueAt(row, 1).toString();



            int confirm = JOptionPane.showConfirmDialog(

                    this,

                    "Bạn có chắc chắn muốn khôi phục mật khẩu cho tài khoản:\n"

                    + ten + " (Mã: " + ma + ")?",

                    "Xác nhận khôi phục",

                    JOptionPane.YES_NO_OPTION

            );



            if (confirm == JOptionPane.YES_OPTION) {

                try {

                    db.resetPassword(Integer.parseInt(ma));

                    JOptionPane.showMessageDialog(this, "Khôi phục mật khẩu thành công!");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(this, "Lỗi khôi phục: " + ex.getMessage());

                }

            }

        });



    }



    private JButton createNavButton(String text, String icon) {

        JButton btn = new JButton("<html><div style='width:75px; text-align:center;'>" + icon + "<br>" + text + "</div></html>");

        btn.setPreferredSize(new Dimension(130, 90));

        btn.setFont(new Font("Arial", Font.BOLD, 16));

        btn.setFocusPainted(false);

        btn.setBackground(Color.WHITE);

        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        return btn;

    }



    private JButton createActionButton(String text) {

        JButton btn = new JButton(text);

        btn.setBackground(Color.WHITE);

        btn.setForeground(Color.BLACK);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        return btn;

    }



    private void loadData() {

        tableModel.setRowCount(0);

        ArrayList<Taikhoan> list = db.getAll();

        for (Taikhoan tk : list) {

            tableModel.addRow(new Object[]{
    tk.getMa(),
    tk.getTen(),
    tk.getMaCCCD(),
    tk.getDienThoai(),
    tk.getDiaChi(),
    tk.getQueQuan(),    // ← PHẢI SỬA Ở ĐÂY
    tk.getQuyen() == 1 ? "Quản lý" : "Nhân viên",
    tk.getTrangThai() == 1 ? "Mở khóa" : "Khóa",
    tk.getLuong()
});

        }

    }



    private void addTaiKhoan(JTextField txtMa, JTextField txtTen, JTextField txtCMND,

            JTextField txtSdt, JTextArea txtDiaChi,JTextField txtQueQuan, JComboBox<String> cbChucVu,

            JRadioButton rbMo, JTextField txtLuong) {

        try {

            String cccd = txtCMND.getText().trim();



            if (cccd.length() != 13) {

                JOptionPane.showMessageDialog(this, "CCCD phải đủ 13 số!",

                        "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);

                return;

            }



            String sdt = txtSdt.getText().trim();



            if (sdt.length() != 10) {

                JOptionPane.showMessageDialog(this, "Số điện thoại phải đúng 10 số!");

                return;

            }



            if (!sdt.startsWith("0")) {

                JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu bằng số 0!");

                return;

            }



            int ma = Integer.parseInt(txtMa.getText());

            int luong = Integer.parseInt(txtLuong.getText());

            String dienThoai = txtSdt.getText();

            String queQuan = txtQueQuan.getText().trim();


            int quyen = cbChucVu.getSelectedIndex() == 0 ? 1 : 0;

            int trangThai = rbMo.isSelected() ? 1 : 0;



            Taikhoan tk = new Taikhoan(

                    ma,

                    "1234567",

                    txtTen.getText(),

                    cccd,

                    queQuan,

                    dienThoai,

                    txtDiaChi.getText(),

                    quyen,

                    trangThai,

                    luong

            );



            db.insertTaiKhoan(tk);

            loadData();

            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Lỗi thêm tài khoản: " + ex.getMessage());

        }

    }



    private void updateTaiKhoan(JTextField txtMa, JTextField txtTen, JTextField txtCMND,

            JTextField txtSdt, JTextArea txtDiaChi,JTextField txtQueQuan, JComboBox<String> cbChucVu,

            JRadioButton rbMo, JTextField txtLuong) {



        try {



            if (txtMa.getText().trim().isEmpty()

                    || txtTen.getText().trim().isEmpty()

                    || txtCMND.getText().trim().isEmpty()

                    || txtSdt.getText().trim().isEmpty()

                    || txtLuong.getText().trim().isEmpty()) {



                JOptionPane.showMessageDialog(this,

                        "Không được để trống thông tin!",

                        "Lỗi nhập liệu",

                        JOptionPane.ERROR_MESSAGE);

                return;

            }



            String cccd = txtCMND.getText().trim();

            if (cccd.length() < 11 || cccd.length() > 13) {

                JOptionPane.showMessageDialog(this, "CCCD phải từ 11 đến 13 số!");

                return;

            }



            String sdt = txtSdt.getText().trim();

            if (sdt.length() != 10 || !sdt.startsWith("0")) {

                JOptionPane.showMessageDialog(this, "Số điện thoại phải 10 số và bắt đầu bằng 0!");

                return;

            }



            int ma = Integer.parseInt(txtMa.getText().trim());

            int luong = Integer.parseInt(txtLuong.getText().trim());



            String ten = txtTen.getText().trim();

            String diaChi = txtDiaChi.getText().trim();

            String queQuan = txtQueQuan.getText().trim();


            int quyen = cbChucVu.getSelectedItem().equals("Quản lý") ? 1 : 0;

            int trangThai = rbMo.isSelected() ? 1 : 0;



            Taikhoan tk = new Taikhoan(

                    ma,

                    "1234567",

                    ten,

                    cccd,

                    queQuan,

                    sdt,

                    diaChi,

                    quyen,

                    trangThai,

                    luong

            );



            db.updateTaiKhoan(tk);

            loadData();



            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");



        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,

                    "Lỗi cập nhật tài khoản: " + ex.getMessage());

        }

    }

    



}