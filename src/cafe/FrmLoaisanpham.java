package cafe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmLoaisanpham extends JFrame {

    private JTextField txtMaLoai, txtTenLoai;
    private JRadioButton rbHoatDong, rbKhoa;
    private JButton btnLamMoi, btnThem, btnXoa;
    private JTable tblLoaiSP;
    private DefaultTableModel model;
    private MyConnection db = new MyConnection();

    public FrmLoaisanpham() {

        setTitle("Thông tin loại sản phẩm");
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new FrmQuanLyHeThong().setVisible(true);
            }
        });

        // ===== PANEL TRÁI =====
        JPanel pnlLeft = new JPanel(null);
        pnlLeft.setPreferredSize(new Dimension(350, 500));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Thông tin loại sản phẩm"));

        JLabel lblMa = new JLabel("Mã loại:");
        lblMa.setBounds(10, 30, 120, 25);
        txtMaLoai = new JTextField();
        txtMaLoai.setBounds(130, 30, 180, 25);
        txtMaLoai.setEditable(false);

        JLabel lblTen = new JLabel("Tên loại:");
        lblTen.setBounds(10, 70, 120, 25);
        txtTenLoai = new JTextField();
        txtTenLoai.setBounds(130, 70, 180, 25);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(10, 110, 80, 25);
        rbHoatDong = new JRadioButton("Đang hoạt động", true);
        rbHoatDong.setBounds(100, 110, 130, 25);
        rbKhoa = new JRadioButton("Khóa");
        rbKhoa.setBounds(230, 110, 80, 25);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbHoatDong);
        bg.add(rbKhoa);

        // --- ĐỊNH DẠNG NÚT BẤM THEO MÀU QUẢN LÝ BÀN ---
        
        btnThem = new JButton("Thêm / Cập nhật");
        btnThem.setBounds(180, 160, 140, 40);
        btnThem.setBackground(new Color(155, 89, 182)); // Màu tím
        btnThem.setForeground(Color.WHITE);
        btnThem.setFocusPainted(false);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnXoa = new JButton("Xóa");
        btnXoa.setBounds(20, 220, 120, 40);
        btnXoa.setBackground(new Color(231, 76, 60)); // Màu đỏ cam
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setBounds(20, 160, 120, 40);
        btnLamMoi.setBackground(new Color(46, 204, 113)); // Màu xanh lá
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlLeft.add(lblMa);
        pnlLeft.add(txtMaLoai);
        pnlLeft.add(lblTen);
        pnlLeft.add(txtTenLoai);
        pnlLeft.add(lblTrangThai);
        pnlLeft.add(rbHoatDong);
        pnlLeft.add(rbKhoa);
        pnlLeft.add(btnLamMoi);
        pnlLeft.add(btnThem);
        pnlLeft.add(btnXoa);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"STT", "Mã loại", "Tên loại", "Trạng thái"}, 0
        );
        tblLoaiSP = new JTable(model);
        tblLoaiSP.setRowHeight(25);

        add(pnlLeft, BorderLayout.WEST);
        add(new JScrollPane(tblLoaiSP), BorderLayout.CENTER);

        // ===== LOAD DATA =====
        db.loadDataLoaiSP(model);

        // ===== LÀM MỚI =====
        btnLamMoi.addActionListener(e -> {
            int nextSTT = model.getRowCount() + 1; 
            txtMaLoai.setText(formatMaLoai(nextSTT));
            txtTenLoai.setText("");
            rbHoatDong.setSelected(true);
            tblLoaiSP.clearSelection();
        });

        // ===== THÊM / CẬP NHẬT =====
        btnThem.addActionListener(e -> {
            String ten = txtTenLoai.getText().trim();
            int trangThai = rbHoatDong.isSelected() ? 1 : 0;

            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập tên loại!");
                return;
            }

            int row = tblLoaiSP.getSelectedRow();

            if (row == -1) {
                db.insertLoaiSP(ten, trangThai);
                JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công!");
            }
            else {
                int maDB = Integer.parseInt(
                        model.getValueAt(row, 1).toString().replace("LSP", "")
                );
                db.updateLoaiSP(maDB, ten, trangThai);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }

            db.loadDataLoaiSP(model);
            btnLamMoi.doClick();
        });

        // ===== XÓA =====
        btnXoa.addActionListener(e -> {
            int row = tblLoaiSP.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
                return;
            }

            int maDB = Integer.parseInt(
                    model.getValueAt(row, 1).toString().replace("LSP", "")
            );

            if (JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa loại này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION) {

                db.deleteLoaiSP(maDB);
                db.loadDataLoaiSP(model);
                btnLamMoi.doClick();
            }
        });

        // ===== CLICK TABLE =====
        tblLoaiSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblLoaiSP.getSelectedRow();
                if (row == -1) return;

                txtMaLoai.setText(model.getValueAt(row, 1).toString());
                txtTenLoai.setText(model.getValueAt(row, 2).toString());

                String tt = model.getValueAt(row, 3).toString();
                rbHoatDong.setSelected(tt.equals("Đang hoạt động"));
                rbKhoa.setSelected(tt.equals("Khóa"));
            }
        });

        // ===== KHỞI TẠO =====
        btnLamMoi.doClick();
    }

    private String formatMaLoai(int soThuTu) {
        return String.format("LSP%03d", soThuTu);
    }
}