package cafe;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Connection;



public class Frm_TrangChu extends JFrame {

    private JButton btnAdmin;
    private JButton btnOrder;
    private JButton btnDangXuat;
    private JButton btnInformation;
    private JLabel pictureBox;
    private JPanel txtOrder;


    public Frm_TrangChu() {
        initComponents();
        checkPermissions(); 
        
         addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            btnLogoutClick(); 
        }
    });
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void initComponents() {
        setTitle("Quán Cafe - Trang Chủ");
        setSize(970, 578);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        txtOrder = new JPanel();
        txtOrder.setBounds(0, 0, 970, 578);
        txtOrder.setLayout(null);
        txtOrder.setBackground(new Color(135, 206, 250)); 

        pictureBox = new JLabel();
        pictureBox.setBounds(16, 20, 531, 500);
        pictureBox.setIcon(loadIcon("src/images/home.jpg", 531, 500));
        txtOrder.add(pictureBox);

        // --- NÚT QUẢN LÝ BÁN HÀNG (ĐÃ CHỈNH MÀU XANH ĐẬM VÀ VÀO THẲNG) ---
        btnOrder = new JButton("QUẢN LÝ BÁN HÀNG");
        btnOrder.setBounds(566, 20, 378, 208);
        btnOrder.setBackground(new Color(0, 51, 102)); // Màu xanh Navy đậm
        btnOrder.setForeground(Color.WHITE);           // Chữ trắng
        btnOrder.setFont(new Font("Consolas", Font.BOLD, 18));
        btnOrder.setIcon(loadIcon("src/images/coffee.jpg", 120, 120));
        btnOrder.setHorizontalTextPosition(SwingConstants.CENTER);
        btnOrder.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnOrder.setIconTextGap(15);
        btnOrder.setFocusPainted(false);
        btnOrder.setBorderPainted(false);
        btnOrder.addActionListener(e -> btnOrderClick()); // Gọi hàm vào thẳng
        txtOrder.add(btnOrder);

        btnAdmin = new JButton("QUẢN TRỊ HỆ THỐNG");
        btnAdmin.setBounds(566, 245, 378, 220);
        btnAdmin.setBackground(Color.WHITE);
        btnAdmin.setForeground(Color.BLACK);
        btnAdmin.setFont(new Font("Consolas", Font.BOLD, 18));
        btnAdmin.setIcon(loadIcon("src/images/admin.jpg", 120, 120)); 
        btnAdmin.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAdmin.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnAdmin.setIconTextGap(15);
        btnAdmin.setFocusPainted(false);
        btnAdmin.setBorderPainted(false);
        btnAdmin.addActionListener(e -> btnAdminClick());
        txtOrder.add(btnAdmin);

        btnInformation = new JButton("ĐỔI MẬT KHẨU");
        btnInformation.setBounds(566, 490, 198, 42);
        btnInformation.setBackground(new Color(162, 155, 254));
        btnInformation.setForeground(Color.WHITE);
        btnInformation.setFocusPainted(false);
        btnInformation.setBorderPainted(false);
        btnInformation.addActionListener(e -> btnInformationClick());
        txtOrder.add(btnInformation);

        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBounds(770, 490, 174, 42);
        btnDangXuat.setBackground(new Color(162, 155, 254));
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.addActionListener(e -> btnLogoutClick());
        txtOrder.add(btnDangXuat);

        add(txtOrder);
    }
    private void btnInformationClick() {
    FrmDoiMatKhau frm = new FrmDoiMatKhau(this);
    frm.setVisible(true);
    this.setVisible(false);
}


    private void checkPermissions() {
        // Lưu ý: Đảm bảo class MySession và Taikhoan đã được định nghĩa trong project
        try {
            Taikhoan currentUser = MySession.getCurrentUser();
            if (currentUser != null && currentUser.getQuyen() == 0) { 
                btnAdmin.setEnabled(false); 
                btnAdmin.setToolTipText("Bạn không có quyền truy cập!");
            }
        } catch (Exception e) {
            // Tránh lỗi nếu MySession chưa được khởi tạo
        }
    }

    // --- HÀM VÀO THẲNG TRANG BÁN HÀNG ---
    private void btnOrderClick() {
        // Mở thẳng trang quản lý bán hàng
        FrmYeuCauGoiThucUong frmOrder = new FrmYeuCauGoiThucUong();
        frmOrder.setVisible(true);
        
        // Đóng trang chủ
        this.dispose();
    }

    private void btnAdminClick() {
        Taikhoan currentUser = MySession.getCurrentUser();
        if (currentUser != null && currentUser.getQuyen() == 1) {
            new FrmQuanLyHeThong().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập hệ thống quản lý!");
        }
    }

 private void btnLogoutClick() {
    try {
        Taikhoan user = MySession.getCurrentUser();
        LocalDateTime loginTime = MySession.loginTime;

        if (user != null && loginTime != null) {
            LocalDateTime logoutTime = LocalDateTime.now();

            String sql = "UPDATE lichdangnhap "
                       + "SET ThoiGianRa=? "
                       + "WHERE TaiKhoan=? AND ThoiGianVao=?";

            MyConnection db = new MyConnection();
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setTimestamp(1, Timestamp.valueOf(logoutTime));
                ps.setString(2, user.getTen());
                ps.setTimestamp(3, Timestamp.valueOf(loginTime));

                ps.executeUpdate();
            }
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    // Đóng trang chủ
    this.dispose();

    // Quay lại màn hình đăng nhập
    new Frm_DangNhap().setVisible(true);
}



}