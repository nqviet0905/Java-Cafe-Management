package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Luong extends JFrame {

    // ===== DB =====
    private MyConnection db = new MyConnection();

    // ===== TABLE =====
    private JTable table;
    private DefaultTableModel model;

    // ===== INPUT =====
    private JComboBox<Taikhoan> cboNhanVien;
    private JComboBox<String> cboThang;
    private JTextField txtSoCa;
    private JTextArea txtGhiChu;
    private JLabel lblTongTien;

    // ===== DATA =====
    private ArrayList<Taikhoan> dsNhanVien;

    // ================= CONSTRUCTOR =================
    public Luong() {
        setTitle("Quản lý lương nhân viên");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new FrmQuanLyHeThong().setVisible(true);
                dispose();
            }
        });

        add(panelLeft(), BorderLayout.WEST);
        add(panelRight(), BorderLayout.CENTER);
        add(panelBottom(), BorderLayout.SOUTH);

        loadNhanVien();
        loadLuong(); 
    }

    // ================= LEFT PANEL (Đã sửa giao diện nút) =================
    private JPanel panelLeft() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(350, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Thông tin tính lương");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(title);
        p.add(Box.createVerticalStrut(15));

        p.add(new JLabel("Nhân viên"));
        cboNhanVien = new JComboBox<>();
        p.add(cboNhanVien);

        p.add(Box.createVerticalStrut(10));
        p.add(new JLabel("Tháng"));
        cboThang = new JComboBox<>(new String[]{
                "Tháng 01 - 2024", "Tháng 02 - 2024", "Tháng 03 - 2024",
                "Tháng 04 - 2024", "Tháng 05 - 2024", "Tháng 06 - 2024"
        });
        p.add(cboThang);

        p.add(Box.createVerticalStrut(10));
        p.add(new JLabel("Số ca trong tháng"));
        txtSoCa = new JTextField();
        txtSoCa.setPreferredSize(new Dimension(100, 28));
        txtSoCa.setMaximumSize(new Dimension(350, 28)); // Chỉnh để rộng đều
        txtSoCa.setHorizontalAlignment(JTextField.LEFT);
        txtSoCa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txtSoCa.getText().length() >= 2) {
                    e.consume();
                }
            }
        });
        p.add(txtSoCa);

        p.add(Box.createVerticalStrut(10));
        p.add(new JLabel("Ghi chú"));
        txtGhiChu = new JTextArea(4, 20);
        p.add(new JScrollPane(txtGhiChu));

        // --- PHẦN NÚT BẤM (GIỐNG ẢNH 1) ---
        p.add(Box.createVerticalStrut(20));

        // Nút Thêm và Xóa nằm trên cùng một dòng
        JPanel pnlButtonsTop = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlButtonsTop.setMaximumSize(new Dimension(350, 50));
        pnlButtonsTop.setOpaque(false);

        JButton btnThem = new JButton("Thêm mới");
        btnThem.setBackground(new Color(155, 89, 182)); // Màu tím
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThem.setFocusPainted(false);

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(231, 76, 60)); // Màu đỏ cam
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setFocusPainted(false);

        pnlButtonsTop.add(btnThem);
        pnlButtonsTop.add(btnXoa);
        p.add(pnlButtonsTop);

        p.add(Box.createVerticalStrut(10));

        // Nút Làm mới nằm riêng ở dưới, to hơn
        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBackground(new Color(46, 204, 113)); // Màu xanh lá
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setMaximumSize(new Dimension(350, 50));
        btnLamMoi.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(btnLamMoi);

        p.add(Box.createVerticalStrut(10));

        // Giữ lại nút Sửa của bạn (Màu xanh dương cho chuyên nghiệp)
        JButton btnSua = new JButton("Sửa");
        btnSua.setBackground(new Color(52, 152, 219)); 
        btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSua.setFocusPainted(false);
        btnSua.setMaximumSize(new Dimension(350, 40));
        p.add(btnSua);

        // ActionListeners
        btnThem.addActionListener(e -> themLuong());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnXoa.addActionListener(e -> xoaLuong());
        btnSua.addActionListener(e -> suaLuong());

        return p;
    }

    // ================= RIGHT PANEL =================
    private JPanel panelRight() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Danh sách lương theo tháng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(title, BorderLayout.NORTH);

        String[] cols = {
                "STT", "Mã NV", "Tên NV", "SĐT", "Địa chỉ",
                "Tháng", "Lương cơ bản", "Số ca", "Tổng lương", "Ghi chú"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ================= BOTTOM PANEL =================
    private JPanel panelBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lblTongTien);
        return p;
    }

    // ================= LOGIC (GIỮ NGUYÊN) =================
    private void loadNhanVien() {
        dsNhanVien = db.getAll(); 
        DefaultComboBoxModel<Taikhoan> cbModel = new DefaultComboBoxModel<>();
        for (Taikhoan tk : dsNhanVien) {
            cbModel.addElement(tk); 
        }
        cboNhanVien.setModel(cbModel);
    }

    private void loadLuong() {
        model.setRowCount(0); 

        String sql = "SELECT l.MaNV, l.TenNV, l.Thang, l.Luong, l.SoCa, l.TongLuong, l.GhiChu, t.DienThoai, t.DiaChi " +
                     "FROM luong l JOIN taikhoan t ON l.MaNV = t.Ma";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int stt = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                        stt++,
                        rs.getInt("MaNV"),
                        rs.getString("TenNV"),
                        rs.getString("DienThoai"),
                        rs.getString("DiaChi"),
                        rs.getString("Thang"),
                        format(rs.getInt("Luong")),
                        rs.getInt("SoCa"),
                        format(rs.getInt("TongLuong")),
                        rs.getString("GhiChu")
                });
            }
            tinhTongTien();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load lương: " + e.getMessage());
        }
    }

    private void themLuong() {
        try {
            if (txtSoCa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số ca!");
                return;
            }
            int soCa = Integer.parseInt(txtSoCa.getText());
            if (soCa < 0 || soCa > 31) {
                JOptionPane.showMessageDialog(this, "Số ca phải từ 0 đến 31!");
                return;
            }
            Taikhoan tk = (Taikhoan) cboNhanVien.getSelectedItem();
            int luongCB = tk.getLuong();
            int tongLuong = luongCB * soCa;
            String thang = cboThang.getSelectedItem().toString();

            db.insertLuong(tk.getMa(), tk.getTen(), thang, luongCB, soCa, tongLuong, txtGhiChu.getText());
            loadLuong(); 
            lamMoi();
            JOptionPane.showMessageDialog(this, "Đã lưu lương vào database!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage());
        }
    }

    private void xoaLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int maNV = (int) table.getValueAt(selectedRow, 1);
            String thang = table.getValueAt(selectedRow, 5).toString();
            String sql = "DELETE FROM luong WHERE MaNV = ? AND Thang = ?";
            try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, maNV);
                ps.setString(2, thang);
                ps.executeUpdate();
            }
            model.removeRow(selectedRow);
            tinhTongTien();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
        }
    }

    private void suaLuong() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
            return;
        }
        try {
            int soCa = Integer.parseInt(txtSoCa.getText());
            if (soCa < 0 || soCa > 31) {
                JOptionPane.showMessageDialog(this, "Số ca phải từ 0 đến 31!");
                return;
            }
            int maNV = (int) table.getValueAt(selectedRow, 1);
            String thang = table.getValueAt(selectedRow, 5).toString();
            int luongCB = Integer.parseInt(
        table.getValueAt(selectedRow, 6)
                .toString()
                .replace(".", "")   // VERY IMPORTANT
                .replace(",", "")   // chứa luôn cho chắc
);

            int tongLuong = luongCB * soCa;

            String sql = "UPDATE luong SET SoCa = ?, TongLuong = ?, GhiChu = ? WHERE MaNV = ? AND Thang = ?";
            try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, soCa);
                ps.setInt(2, tongLuong);
                ps.setString(3, txtGhiChu.getText());
                ps.setInt(4, maNV);
                ps.setString(5, thang);
                ps.executeUpdate();
            }
            loadLuong();
            lamMoi();
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + e.getMessage());
        }
    }

    private void lamMoi() {
        txtSoCa.setText("");
        txtGhiChu.setText("");
    }

    private void tinhTongTien() {
        long sum = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            sum += Long.parseLong(
        model.getValueAt(i, 8)
                .toString()
                .replace(".", "")
                .replace(",", "")
);

        }
        lblTongTien.setText("Tổng tiền: " + format(sum));
    }

    private String format(long n) {
        return String.format("%,d", n);
    }
}