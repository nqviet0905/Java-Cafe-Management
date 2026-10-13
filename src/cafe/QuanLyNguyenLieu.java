package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class QuanLyNguyenLieu extends JFrame {

    private final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private final Color SIDEBAR_COLOR = new Color(245, 247, 250);

    private JTextField txtMaNL, txtTenNL, txtGia;
    private JTextArea txtGhiChu;
    private JTable table;
    private DefaultTableModel model;
    private MyConnection conn;

    public QuanLyNguyenLieu() {
        setTitle("Hệ Thống Quản Lý Nguyên Vật Liệu");
        setSize(1100, 700);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Quan trọng: xử lý dấu X
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Khi nhấn dấu X => quay về FrmQuanLyHeThong
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new FrmQuanLyHeThong().setVisible(true);
                dispose();
            }
        });

        conn = new MyConnection(); // Khởi tạo kết nối DB

        /* ================= SIDEBAR ================= */
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setPreferredSize(new Dimension(320, 700));
        pnlLeft.setBackground(SIDEBAR_COLOR);
        pnlLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel lblHeader = new JLabel("THÔNG TIN CHI TIẾT");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(PRIMARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(30, 20, 20, 20));
        pnlLeft.add(lblHeader);

        txtMaNL = new JTextField();
        txtTenNL = new JTextField();
        txtGia = new JTextField();
        txtGhiChu = new JTextArea(4, 20);

        pnlLeft.add(createStyledInput("Mã nguyên liệu", txtMaNL));
        pnlLeft.add(createStyledInput("Tên nguyên liệu", txtTenNL));
        pnlLeft.add(createStyledInput("Giá thành (VNĐ)", txtGia));
        pnlLeft.add(createStyledTextArea("Ghi chú", txtGhiChu));

        JPanel pnlButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlButtons.setBackground(SIDEBAR_COLOR);
        pnlButtons.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton btnThem = createCustomButton("Thêm Nguyên Liệu", PRIMARY_COLOR);
        JButton btnXoa = createCustomButton("Xóa Lựa Chọn", new Color(211, 47, 47));
        JButton btnLamMoi = createCustomButton("Làm Mới Biểu Mẫu", new Color(117, 117, 117));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlLeft.add(pnlButtons);
        add(pnlLeft, BorderLayout.WEST);

        /* ================= TABLE ================= */
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTableTitle = new JLabel("DANH SÁCH TỒN KHO");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlRight.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"STT", "Mã NL", "Tên NL", "Ngày nhập", "Giá thành", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        pnlRight.add(scrollPane, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.CENTER);

        /* ================= EVENTS ================= */
        btnThem.addActionListener(e -> themNguyenLieu());
        btnXoa.addActionListener(e -> xoaNguyenLieu());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        loadData(); // Load dữ liệu từ database khi mở form
    }

    /* ================= LOGIC ================= */

    private void themNguyenLieu() {
        if (txtMaNL.getText().isEmpty() ||
                txtTenNL.getText().isEmpty() ||
                txtGia.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNL = txtMaNL.getText();
        String tenNL = txtTenNL.getText();
        int gia;
        try {
            gia = Integer.parseInt(txtGia.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Giá phải là số nguyên!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String ghiChu = txtGhiChu.getText();
        LocalDate ngayNhap = LocalDate.now();

        // Lưu vào database
        String sql = "INSERT INTO nguyenlieu(MaNL, TenNL, NgayNhap, Gia, GhiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maNL);
            ps.setString(2, tenNL);
            ps.setDate(3, Date.valueOf(ngayNhap));
            ps.setInt(4, gia);
            ps.setString(5, ghiChu);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi lưu dữ liệu: " + e.getMessage(),
                    "SQL Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào JTable
        model.addRow(new Object[]{null, maNL, tenNL, ngayNhap, gia, ghiChu});
        capNhatSTT();
        lamMoiForm();
    }

    private void xoaNguyenLieu() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn dòng cần xóa!",
                    "Chưa chọn",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            // Xóa database
            String maNL = (String) model.getValueAt(row, 1);
            String sql = "DELETE FROM nguyenlieu WHERE MaNL=?";
            try (Connection c = conn.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, maNL);
                ps.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi xóa dữ liệu: " + e.getMessage(),
                        "SQL Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            model.removeRow(row);
            capNhatSTT();
        }
    }

    private void capNhatSTT() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

    private void lamMoiForm() {
        txtMaNL.setText("");
        txtTenNL.setText("");
        txtGia.setText("");
        txtGhiChu.setText("");
        txtMaNL.requestFocus();
    }

    /* ================= UI ================= */

    private JPanel createStyledInput(String label, JTextField field) {
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setBackground(SIDEBAR_COLOR);
        container.setMaximumSize(new Dimension(320, 80));
        container.setBorder(new EmptyBorder(5, 20, 5, 20));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        field.setPreferredSize(new Dimension(0, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        container.add(lbl, BorderLayout.NORTH);
        container.add(field, BorderLayout.CENTER);
        return container;
    }

    private JPanel createStyledTextArea(String label, JTextArea area) {
        JPanel container = new JPanel(new BorderLayout(5, 5));
        container.setBackground(SIDEBAR_COLOR);
        container.setBorder(new EmptyBorder(5, 20, 5, 20));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        container.add(lbl, BorderLayout.NORTH);
        container.add(sp, BorderLayout.CENTER);
        return container;
    }

    private JButton createCustomButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(JLabel.LEFT);
    }

    // ================= LOAD DỮ LIỆU =================
    private void loadData() {
        String sql = "SELECT * FROM nguyenlieu";
        try (Connection c = conn.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0); // Xóa dữ liệu cũ
            int stt = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                        stt++,
                        rs.getString("MaNL"),
                        rs.getString("TenNL"),
                        rs.getDate("NgayNhap").toLocalDate(),
                        rs.getInt("Gia"),
                        rs.getString("GhiChu")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi load dữ liệu: " + e.getMessage(),
                    "SQL Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

   
}
