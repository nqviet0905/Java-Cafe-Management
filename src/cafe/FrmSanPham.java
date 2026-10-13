package cafe;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class FrmSanPham extends JFrame {

    // --- KHAI BÁO BIẾN ---
    private MyConnection db = new MyConnection();
    private JTextField txtMa, txtTen, txtGia, txtTimKiem;
    private JComboBox<String> cbLoai;
    private JRadioButton rbHoatDong, rbNgung;
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public FrmSanPham() {
        setTitle("Quản Lý Sản Phẩm");
        setSize(1100, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new FrmQuanLyHeThong().setVisible(true);
            }
        });

        // ===== PANEL TRÁI =====
        JPanel pnlLeft = new JPanel(null);
        pnlLeft.setPreferredSize(new Dimension(350, 0));
        pnlLeft.setBorder(new TitledBorder("Thông tin sản phẩm"));

        JLabel lblMa = new JLabel("Mã sản phẩm:");
        lblMa.setBounds(20, 30, 100, 25);
        txtMa = new JTextField();
        txtMa.setBounds(130, 30, 180, 25);
        txtMa.setEditable(false);

        JLabel lblTen = new JLabel("Tên sản phẩm:");
        lblTen.setBounds(20, 70, 100, 25);
        txtTen = new JTextField();
        txtTen.setBounds(130, 70, 180, 25);

        JLabel lblLoai = new JLabel("Loại sản phẩm:");
        lblLoai.setBounds(20, 110, 120, 25);
        cbLoai = new JComboBox<>();
        db.loadLoaiSPToCombo(cbLoai);
        cbLoai.setBounds(130, 110, 180, 25);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(20, 150, 100, 25);
        rbHoatDong = new JRadioButton("Đang hoạt động", true);
        rbHoatDong.setBounds(100, 150, 120, 25);
        rbNgung = new JRadioButton("Ngừng bán");
        rbNgung.setBounds(220, 150, 100, 25);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbHoatDong);
        bg.add(rbNgung);

        JLabel lblGia = new JLabel("Đơn giá:");
        lblGia.setBounds(20, 200, 100, 25);
        txtGia = new JTextField();
        txtGia.setBounds(80, 200, 230, 25);

        // --- CHỈNH MÀU NÚT BÊN TRÁI ---
        JButton btnThem = new JButton("Thêm mới");
        btnThem.setBounds(100, 250, 100, 40);
        btnThem.setBackground(new Color(155, 89, 182)); // Màu tím
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton btnXoa = new JButton("Xóa sản phẩm");
        btnXoa.setBounds(210, 250, 120, 40);
        btnXoa.setBackground(new Color(231, 76, 60)); // Màu đỏ cam
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlLeft.add(lblMa); pnlLeft.add(txtMa);
        pnlLeft.add(lblTen); pnlLeft.add(txtTen);
        pnlLeft.add(lblLoai); pnlLeft.add(cbLoai);
        pnlLeft.add(lblTrangThai); pnlLeft.add(rbHoatDong); pnlLeft.add(rbNgung);
        pnlLeft.add(lblGia); pnlLeft.add(txtGia);
        pnlLeft.add(btnThem); pnlLeft.add(btnXoa);

        // ===== PANEL PHẢI =====
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(new JLabel("Từ khóa:"));
        txtTimKiem = new JTextField(15);
        pnlTop.add(txtTimKiem);

        // --- CHỈNH MÀU NÚT TÌM KIẾM & LÀM MỚI ---
        JButton btnTim = new JButton("Tìm kiếm");
        btnTim.setBackground(new Color(52, 152, 219)); // Màu xanh dương
        btnTim.setForeground(Color.WHITE);
        btnTim.setFocusPainted(false);

        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBackground(new Color(46, 204, 113)); // Màu xanh lá
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);

        pnlTop.add(btnTim);
        pnlTop.add(btnLamMoi);

        String[] columnNames = {
            "STT", "Mã sản phẩm", "Tên sản phẩm",
            "Loại", "Đơn giá", "Khuyến mãi", "Trạng thái"
        };

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setRowHeight(25);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // ===== FORMAT ĐƠN GIÁ =====
        DecimalFormat df = new DecimalFormat("#,##0");
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setText(df.format(value));
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        });

        pnlRight.add(pnlTop, BorderLayout.NORTH);
        pnlRight.add(new JScrollPane(table), BorderLayout.CENTER);

        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        // ===== LOAD DATA =====
        db.loadDataSanPham(model);

        // ===== SỰ KIỆN (GIỮ NGUYÊN) =====
        btnThem.addActionListener(e -> {
            String ma = db.getNextMaSanPham(); 
            txtMa.setText(ma);
            String ten = txtTen.getText().trim();
            String giaStr = txtGia.getText().trim();
            String loai = cbLoai.getSelectedItem().toString();
            String trangThai = rbHoatDong.isSelected() ? "Đang hoạt động" : "Ngừng bán";
            String khuyenMai = "Không";

            if (ten.isEmpty() || giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập đủ thông tin!");
                return;
            }

            try {
                double gia = Double.parseDouble(giaStr);
                db.insertSanPham(ma, ten, gia, khuyenMai, trangThai, loai);
                db.loadDataSanPham(model);
                lamMoiForm();
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số!");
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int modelRow = table.convertRowIndexToModel(row);
            String ma = model.getValueAt(modelRow, 1).toString();

            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                db.deleteSanPham(ma);
                db.loadDataSanPham(model);
                lamMoiForm();
            }
        });

        btnTim.addActionListener(e -> {
            String text = txtTimKiem.getText().trim();
            sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text, 2));
        });

        btnLamMoi.addActionListener(e -> {
            lamMoiForm();
            txtTimKiem.setText("");
            sorter.setRowFilter(null);
            db.loadDataSanPham(model);
        });
    }

    private void lamMoiForm() {
        txtMa.setText(db.getNextMaSanPham());
        txtTen.setText("");
        txtGia.setText("");
        txtTimKiem.setText("");
        cbLoai.setSelectedIndex(0);
        rbHoatDong.setSelected(true);
        table.clearSelection();
    }
}