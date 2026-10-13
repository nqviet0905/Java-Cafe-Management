package cafe;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;



public class Frm_DangNhap extends JFrame {

    private JPanel panel4;
    private JButton btnThoat, btnDangNhap, button1;
    private JComboBox<String> cbxNhanVien;
    private JPasswordField txtMatKhau;
    private JLabel lblNotification, label1, label2, label3;
    private JLabel pictureBox1;

    private MyConnection db; 
    private ArrayList<Taikhoan> taiKhoanList;

    public Frm_DangNhap() {
        db = new MyConnection();
        taiKhoanList = db.getAll(); 
        initComponents();
        loadNhanVien(); 
    }

    private void initComponents() {
        setTitle("Đăng nhập");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);
        getContentPane().setBackground(new Color(135, 206, 250));

        panel4 = new JPanel();
        panel4.setBackground(new Color(41, 128, 185));
        panel4.setBounds(0, 0, 500, 40);
        panel4.setLayout(null);

        label3 = new JLabel("Đăng nhập");
        label3.setFont(new Font("Roboto", Font.BOLD, 20));
        label3.setBounds(10, 5, 200, 30);
        panel4.add(label3);

        button1 = new JButton("x");
        button1.setBounds(454, 0, 46, 40);
        button1.setFont(new Font("Roboto", Font.BOLD, 18));
        button1.setForeground(Color.WHITE);
        button1.setFocusPainted(false);
        button1.setContentAreaFilled(false);
        button1.setBorderPainted(false);
        button1.addActionListener(e -> System.exit(0));
        panel4.add(button1);

        add(panel4);

        pictureBox1 = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/home.jpg"));
        Image img = icon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
        pictureBox1.setIcon(new ImageIcon(img));
        pictureBox1.setBounds(15, 60, 200, 250);
        add(pictureBox1);

        lblNotification = new JLabel("");
        lblNotification.setForeground(Color.RED);
        lblNotification.setBounds(240, 60, 240, 25);
        add(lblNotification);

        label1 = new JLabel("Tài khoản:");
        label1.setBounds(240, 95, 80, 25);
        add(label1);

        cbxNhanVien = new JComboBox<>();
        cbxNhanVien.setBounds(325, 95, 155, 25);
        add(cbxNhanVien);

        label2 = new JLabel("Mật khẩu:");
        label2.setBounds(240, 135, 80, 25);
        add(label2);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBounds(325, 135, 155, 25);
        add(txtMatKhau);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBounds(240, 180, 100, 30);
        btnDangNhap.setBackground(new Color(162, 155, 254));
        btnDangNhap.setForeground(Color.BLACK);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.addActionListener(e -> dangNhapClick());
        add(btnDangNhap);

        btnThoat = new JButton("Thoát");
        btnThoat.setBounds(360, 180, 100, 30);
        btnThoat.setBackground(new Color(162, 155, 254));
        btnThoat.setForeground(Color.BLACK);
        btnThoat.setFocusPainted(false);
        btnThoat.addActionListener(e -> System.exit(0));
        add(btnThoat);
    }

    private void loadNhanVien() {
        cbxNhanVien.removeAllItems();
        for (Taikhoan tk : taiKhoanList) {
            cbxNhanVien.addItem(tk.getTen());
        }
    }

    private void dangNhapClick() {
    String username = (String) cbxNhanVien.getSelectedItem();
    String password = new String(txtMatKhau.getPassword());

    Taikhoan tkLogin = null;
    for (Taikhoan tk : taiKhoanList) {
        if (tk.getTen().equals(username)) {
            tkLogin = tk;
            break;
        }
    }

    if (tkLogin != null) {
        if (tkLogin.getMatkhau().equals(password)) {
            lblNotification.setForeground(Color.GREEN);
            lblNotification.setText("Đăng nhập thành công!");

           
            MySession.setCurrentUser(tkLogin);

          
        LocalDateTime loginTime = LocalDateTime.now();
        MySession.loginTime = loginTime;

        try {
    String sql = "INSERT INTO lichdangnhap (TaiKhoan, Quyen, ThoiGianVao) VALUES (?, ?, ?)";
    Connection c = db.getConnection();   
    PreparedStatement ps = c.prepareStatement(sql);

    ps.setString(1, tkLogin.getTen());
    ps.setInt(2, tkLogin.getQuyen());
    ps.setTimestamp(3, Timestamp.valueOf(loginTime));

    ps.executeUpdate();
    ps.close();
    c.close();

} catch (Exception ex) {
    ex.printStackTrace();
}




            Frm_TrangChu frmTrangChu = new Frm_TrangChu();
            frmTrangChu.setVisible(true);

            
            this.dispose();
        } else {
            lblNotification.setForeground(Color.RED);
            lblNotification.setText("Mật khẩu sai!");
        }
    } else {
        lblNotification.setForeground(Color.RED);
        lblNotification.setText("Tài khoản không tồn tại!");
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Frm_DangNhap().setVisible(true);
        });
    }
}
