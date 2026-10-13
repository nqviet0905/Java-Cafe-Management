package cafe;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class FrmDoiMatKhau extends JFrame {

    private Frm_TrangChu mainForm;
    private MyConnection db = new MyConnection();


    private JPanel panelLeft, panelMain, panelTop;
    private JLabel lblTitle, lblThongBao;
    private JComboBox<String> cbxNhanVien;
    private JTextField txtPassOld;
    private JPasswordField txtPasswordNew, txtRetypePass;
    private JButton btnChange, btnCancel, btnClose;
    private JLabel lblImage;

    public FrmDoiMatKhau(Frm_TrangChu mainForm) {

        this.mainForm = mainForm;

        initComponents();
    }
    
    private void loadUsernames() {

    cbxNhanVien.removeAllItems();
    cbxNhanVien.addItem("-- chọn nhân viên --");

    ArrayList<Taikhoan> list = db.getAll();

    if (list == null || list.isEmpty()) {
        lblThongBao.setText("Không có tài khoản trong DB!");
        return;
    }

    for (Taikhoan tk : list) {
        cbxNhanVien.addItem(tk.getTen());
    }
}


    private void initComponents() {

        setTitle("Đổi mật khẩu");
        setSize(680, 430);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);
        getContentPane().setBackground(new Color(135, 206, 250)); 

       

        panelTop = new JPanel();
        panelTop.setBounds(0, 0, 680, 32);
        panelTop.setBackground(new Color(41, 128, 185));
        panelTop.setLayout(null);

        lblTitle = new JLabel("Đổi Mật Khẩu");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 14));
        lblTitle.setBounds(10, 5, 200, 25);
        panelTop.add(lblTitle);

        btnClose = new JButton("x");
        btnClose.setBounds(625, 0, 55, 32);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Roboto", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);

        btnClose.addActionListener(e -> {

            mainForm.setVisible(true);
            dispose();
        });

        panelTop.add(btnClose);

        add(panelTop);

        panelLeft = new JPanel();
        panelLeft.setBounds(10, 50, 300, 330);
        panelLeft.setLayout(null);
        panelLeft.setBackground(Color.WHITE);

        lblImage = new JLabel();
        lblImage.setBounds(1, 30, 300, 330);
        lblImage.setIcon(new ImageIcon(getClass().getResource("/images/pass.jpg")));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setVerticalAlignment(SwingConstants.CENTER);

        panelLeft.add(lblImage);

        add(panelLeft);

        panelMain = new JPanel();
        panelMain.setBounds(320, 50, 350, 370);
        panelMain.setLayout(null);

        JLabel lblNhanVien = new JLabel("Tên đăng nhập:");
        lblNhanVien.setBounds(10, 10, 120, 25);
        panelMain.add(lblNhanVien);

        cbxNhanVien = new JComboBox<>();
        cbxNhanVien.setBounds(140, 10, 200, 25);
        panelMain.add(cbxNhanVien);
        
        loadUsernames(); 

        JLabel lblPassOld = new JLabel("Mật khẩu cũ:");
        lblPassOld.setBounds(10, 50, 120, 25);
        panelMain.add(lblPassOld);

        txtPassOld = new JTextField();
        txtPassOld.setBounds(140, 50, 200, 25);
        panelMain.add(txtPassOld);

        JLabel lblPassNew = new JLabel("Mật khẩu mới:");
        lblPassNew.setBounds(10, 90, 120, 25);
        panelMain.add(lblPassNew);

        txtPasswordNew = new JPasswordField();
        txtPasswordNew.setBounds(140, 90, 200, 25);
        panelMain.add(txtPasswordNew);

        JLabel lblRetype = new JLabel("Nhập lại:");
        lblRetype.setBounds(10, 130, 120, 25);
        panelMain.add(lblRetype);

        txtRetypePass = new JPasswordField();
        txtRetypePass.setBounds(140, 130, 200, 25);
        panelMain.add(txtRetypePass);

        lblThongBao = new JLabel();
        lblThongBao.setBounds(10, 170, 330, 25);
        lblThongBao.setForeground(Color.RED);
        panelMain.add(lblThongBao);

        btnChange = new JButton("Đổi mật khẩu");
        btnChange.setBounds(10, 210, 230, 35);
        btnChange.setBackground(new Color(162, 155, 254));
        btnChange.setForeground(Color.WHITE);

        btnChange.addActionListener(e -> doiMatKhau());

        panelMain.add(btnChange);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(250, 210, 80, 35);
        btnCancel.setBackground(new Color(162, 155, 254));
        btnCancel.setForeground(Color.WHITE);

        btnCancel.addActionListener(e -> {

            mainForm.setVisible(true);
            dispose();
        });

        panelMain.add(btnCancel);

        add(panelMain);
    }

    private void doiMatKhau() {

    Object selected = cbxNhanVien.getSelectedItem();

    if (selected == null || selected.toString().equals("-- chọn nhân viên --")) {
    lblThongBao.setText("Hãy chọn tên đăng nhập!");
    return;
    }

    String username = selected.toString();

    String passOld = txtPassOld.getText();
    String newPass = new String(txtPasswordNew.getPassword());
    String retype = new String(txtRetypePass.getPassword());

    if (newPass.isEmpty() || retype.isEmpty() || passOld.isEmpty()) {
        lblThongBao.setText("Không được để trống!");
        return;
    }

    if (!newPass.equals(retype)) {
        lblThongBao.setText("Mật khẩu mới không trùng nhau!");
        return;
    }

    boolean ok = db.changePassword(username, passOld, newPass);

    if (!ok) {
        lblThongBao.setText("Mật khẩu cũ không đúng!");
        return;
    }

    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");

    mainForm.setVisible(true);
    dispose();
}

}
