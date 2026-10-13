package cafe;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class FrmLichSuDangNhap extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private MyConnection db;

    public FrmLichSuDangNhap() {

        setTitle("Lịch Sử Đăng Nhập");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        db = new MyConnection();

        // ================================
        // HEADER
        // ================================
        JPanel header = new JPanel();
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(800, 60));
        header.setLayout(null);

        JLabel title = new JLabel("LỊCH SỬ ĐĂNG NHẬP");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Roboto", Font.BOLD, 22));
        title.setBounds(20, 10, 300, 40);
        header.add(title);

       JButton btnThoat = new JButton("Thoát");
btnThoat.setBounds(650, 15, 120, 30);
btnThoat.setFocusPainted(false);
btnThoat.setBackground(new Color(231, 76, 60));
btnThoat.setForeground(Color.WHITE);
btnThoat.setFont(new Font("Roboto", Font.BOLD, 14));

btnThoat.addActionListener(e -> {
    this.dispose();                   
    new Frm_TrangChu().setVisible(true);  
});

header.add(btnThoat);


        // ================================
        // TABLE DESIGN
        // ================================
        String[] col = {"Tài khoản", "Chức vụ", "Thời gian vào", "Thời gian ra"};
        model = new DefaultTableModel(col, 0);

        table = new JTable(model);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader headerTable = table.getTableHeader();
        headerTable.setFont(new Font("Roboto", Font.BOLD, 15));
        headerTable.setBackground(new Color(52, 152, 219));
        headerTable.setForeground(Color.WHITE);
        headerTable.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
        scroll.setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        try {
            String sql = "SELECT * FROM lichdangnhap ";
            Connection c = db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("TaiKhoan"),
                        rs.getInt("Quyen") == 1 ? "Admin" : "Nhân viên",
                        rs.getTimestamp("ThoiGianVao"),
                        rs.getTimestamp("ThoiGianRa"),
                    
                });
            }

            rs.close();
            ps.close();
            c.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
