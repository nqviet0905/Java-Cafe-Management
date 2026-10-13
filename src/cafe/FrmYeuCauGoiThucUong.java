package cafe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FrmYeuCauGoiThucUong extends JFrame {

    private JPanel pnlTables;
    private JTable tblBill, tblProduct;
    private JTextField txtMaHD, txtBanSo, txtThoiGian, txtTongTien, txtTimKiem;
    private JComboBox<String> cbLoaiSP;
    private JButton btnThanhToan, btnChuyenBan, btnLamMoi,btnThoat;

    private MyConnection db = new MyConnection();
    private ArrayList<TableInfo> listTables = new ArrayList<>();
    private TableInfo currentTable = null;
    private HashMap<String, DefaultTableModel> orderMap = new HashMap<>();

    public FrmYeuCauGoiThucUong() {
        initComponents();
        // GỌI HÀM NÀY ĐỂ NẠP DỮ LIỆU VÀO DATABASE NẾU CHƯA CÓ
    db.checkAndCreateMangVe();
        loadSanPhamFromSQL(); 
        loadBanTuSQL();      
    }

    private void initComponents() {
        setTitle("QUẢN LÝ BÁN HÀNG");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(52, 152, 219));
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("QUẢN LÝ BÁN HÀNG ", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setPreferredSize(new Dimension(1300, 60));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(null);
        pnlCenter.setOpaque(false);
        add(pnlCenter, BorderLayout.CENTER);

        btnChuyenBan = new JButton("Chuyển - Đổi");
        btnChuyenBan.setBounds(20, 10, 120, 35);
        styleButton(btnChuyenBan, new Color(41, 128, 185));
        btnChuyenBan.addActionListener(e -> chuyenBan()); 
        pnlCenter.add(btnChuyenBan);

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setBounds(150, 10, 100, 35);
        styleButton(btnLamMoi, new Color(200, 162, 200));
        btnLamMoi.addActionListener(e -> resetToanBoHeThong()); 
        pnlCenter.add(btnLamMoi);
        
        btnThoat = new JButton("Thoát");
btnThoat.setBounds(260, 10, 100, 35);
styleButton(btnThoat, new Color(231, 76, 60));
btnThoat.addActionListener(e -> {
    new Frm_TrangChu().setVisible(true);
    dispose();
});
pnlCenter.add(btnThoat);


        pnlTables = new JPanel();
        pnlTables.setBackground(new Color(41, 128, 185));
        pnlTables.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JScrollPane scrollTables = new JScrollPane(pnlTables);
        scrollTables.setBounds(20, 55, 400, 595);
        scrollTables.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Thông tin bàn", 0, 0,
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        pnlCenter.add(scrollTables);

        JPanel pnlInput = new JPanel(null);
        pnlInput.setOpaque(false);
        pnlInput.setBounds(430, 10, 350, 140);
        addLabelAndField(pnlInput, "Mã hóa đơn:", txtMaHD = new JTextField(), 0);
        addLabelAndField(pnlInput, "Bàn số:", txtBanSo = new JTextField(), 35);
        addLabelAndField(pnlInput, "Thời gian:", txtThoiGian = new JTextField(), 70);
        addLabelAndField(pnlInput, "Tổng tiền:", txtTongTien = new JTextField("0"), 105);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTongTien.setForeground(Color.RED);
        pnlCenter.add(pnlInput);

        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setBounds(440, 155, 330, 40);
        styleButton(btnThanhToan, new Color(155, 89, 182));
        btnThanhToan.addActionListener(e -> handlePayment());
        pnlCenter.add(btnThanhToan);

        tblBill = new JTable(new DefaultTableModel(new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0));
        JScrollPane scrollBill = new JScrollPane(tblBill);
        scrollBill.setBounds(430, 205, 410, 445);
        scrollBill.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Danh mục order sản phẩm", 0, 0,
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        pnlCenter.add(scrollBill);

        JPanel pnlProductArea = new JPanel(null);
        pnlProductArea.setBounds(850, 10, 410, 640);
        pnlProductArea.setOpaque(false);
        pnlProductArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Thông tin sản phẩm", 0, 0,
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));

        cbLoaiSP = new JComboBox<>();
        cbLoaiSP.setBounds(110, 30, 200, 25);
        pnlProductArea.add(new JLabel("Loại sản phẩm:") {{ setForeground(Color.WHITE); setBounds(10, 30, 100, 25); }});
        pnlProductArea.add(cbLoaiSP);
        db.loadLoaiSPToCombo(cbLoaiSP);
        cbLoaiSP.addActionListener(e -> loadSanPhamTheoLoai());

        txtTimKiem = new JTextField();
        txtTimKiem.setBounds(110, 70, 150, 25);
        JButton btnTim = new JButton("Tìm");
        btnTim.setBounds(270, 70, 70, 25);
        styleButton(btnTim, new Color(52, 73, 94));
        btnTim.addActionListener(e -> timSanPhamTheoTen());
        pnlProductArea.add(new JLabel("Tên sản phẩm:") {{ setForeground(Color.WHITE); setBounds(10, 70, 100, 25); }});
        pnlProductArea.add(txtTimKiem);
        pnlProductArea.add(btnTim);

        tblProduct = new JTable(new DefaultTableModel(new String[]{"STT", "Tên sản phẩm", "Đơn giá"}, 0));
        JScrollPane scrollProd = new JScrollPane(tblProduct);
        scrollProd.setBounds(10, 110, 390, 520);
        pnlProductArea.add(scrollProd);
        pnlCenter.add(pnlProductArea);

        // Click chọn món
        tblProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentTable == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn bàn trước!");
                    return;
                }
                int row = tblProduct.getSelectedRow();
                if (row >= 0) {
                    String tenSp = tblProduct.getValueAt(row, 1).toString();
                    double gia = Double.parseDouble(tblProduct.getValueAt(row, 2).toString());
                    themSanPhamVaoOrder(tenSp, gia);
                    updateTrangThaiBanSQL(currentTable.ma, 1);
                    loadBanTuSQL(); 
                }
            }
        });

        add(new JLabel("  Nhân viên đang đăng nhập: admin") {{ setOpaque(true); setBackground(new Color(236, 240, 241)); setPreferredSize(new Dimension(1300, 25)); }}, BorderLayout.SOUTH);
    }

  private void loadBanTuSQL() {
    pnlTables.removeAll();
    listTables.clear();
    // SQL lấy tất cả bàn, ưu tiên hiện bàn mang về lên trước (MV1)
    String sql = "SELECT * FROM ban ORDER BY CASE WHEN ma = 'MV1' THEN 0 ELSE 1 END, ma ASC";
    
    try (Connection c = db.getConnection(); 
         Statement s = c.createStatement(); 
         ResultSet rs = s.executeQuery(sql)) {
         
        while (rs.next()) {
            String ma = rs.getString("ma");
            String ten = rs.getString("tenban");
            int tt = rs.getInt("trangthai");
            
            // QUAN TRỌNG: Khai báo biến table ở đây
            TableInfo currentInfo = new TableInfo(ma, ten, tt);
            listTables.add(currentInfo);

            String hienThiTen = ma.equalsIgnoreCase("MV1") ? "🛍️ " + ten : ten;

            JButton btn = new JButton("<html><center>" + hienThiTen + "<br>(" + (tt == 0 ? "Trống" : "Có khách") + ")</center></html>");
            btn.setPreferredSize(new Dimension(100, 80));
            btn.setBackground(tt == 0 ? new Color(46, 204, 113) : new Color(231, 76, 60));
            btn.setForeground(Color.WHITE);

            // Thêm sự kiện Click
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        // Sửa lỗi: dùng biến currentInfo thay vì biến 'table' không tồn tại
                        currentTable = currentInfo; 
                        txtBanSo.setText(ten);
                        
                        // Cập nhật mã hóa đơn và thời gian
                        txtMaHD.setText("HD-" + ma + "-" + new SimpleDateFormat("HHmm").format(new Date()));
                        txtThoiGian.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                        
                        if (!orderMap.containsKey(ma)) {
                            orderMap.put(ma, new DefaultTableModel(new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0));
                        }
                        tblBill.setModel(orderMap.get(ma));
                        tinhTongTien();
                    } else if (e.getClickCount() == 2) {
                        // Click đúp để Reset
                        int opt = JOptionPane.showConfirmDialog(null, "Xóa đơn bàn " + ten + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_OPTION) {
                            orderMap.remove(ma);
                            db.updateTrangThaiBan(ma, 0); 
                            loadBanTuSQL(); 
                        }
                    }
                }
            });
            pnlTables.add(btn);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    pnlTables.revalidate();
    pnlTables.repaint();
    
}

    private void resetToanBoHeThong() {
        int opt = JOptionPane.showConfirmDialog(this, "Làm mới TOÀN BỘ bàn về Trống?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            orderMap.clear();
            try (Connection c = db.getConnection(); Statement s = c.createStatement()) {
                s.executeUpdate("UPDATE ban SET trangthai = 0");
            } catch (Exception e) { e.printStackTrace(); }
            currentTable = null;
            txtBanSo.setText("");
            txtTongTien.setText("0");
            tblBill.setModel(new DefaultTableModel(new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0));
            loadBanTuSQL();
        }
    }

    private void updateTrangThaiBanSQL(String ma, int tt) {
        String sql = "UPDATE ban SET trangthai = ? WHERE ma = ?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tt); ps.setString(2, ma); ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void themSanPhamVaoOrder(String tenSp, double gia) {
        if (!orderMap.containsKey(currentTable.ma)) {
            orderMap.put(currentTable.ma, new DefaultTableModel(new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0));
        }
        DefaultTableModel model = orderMap.get(currentTable.ma);
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).toString().equals(tenSp)) {
                int sl = Integer.parseInt(model.getValueAt(i, 2).toString()) + 1;
                model.setValueAt(sl, i, 2);
                model.setValueAt(sl * gia, i, 4);
                found = true; break;
            }
        }
        if (!found) model.addRow(new Object[]{model.getRowCount() + 1, tenSp, 1, gia, gia});
        tblBill.setModel(model);
        tinhTongTien();
    }

    private void tinhTongTien() {
        double tong = 0;
        DefaultTableModel model = orderMap.get(currentTable.ma);
        if (model != null) {
            for (int i = 0; i < model.getRowCount(); i++) tong += Double.parseDouble(model.getValueAt(i, 4).toString());
        }
        txtTongTien.setText(String.format("%.0f", tong));
    }

  private void handlePayment() {
    if (currentTable == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần thanh toán!");
        return;
    }

    DefaultTableModel model = orderMap.get(currentTable.ma);
    if (model == null || model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Bàn này chưa có món!");
        return;
    }

    // ===== DỮ LIỆU TRUYỀN SANG FORM THANH TOÁN =====
    String maHoaDon = txtMaHD.getText();
    String tenBan = currentTable.ten;
    String tongTien = txtTongTien.getText().replace(",", "");

    // maHD để sau này lưu DB, tạm convert đơn giản
    int maHD = (int) (System.currentTimeMillis() % 100000);

    // ===== MỞ FORM THANH TOÁN (MODAL) =====
    FrmThanhToan frm = new FrmThanhToan(
            this,          // parent
            maHoaDon,      // tên hóa đơn
            tenBan,        // tên bàn
            maHD,          // mã HD (int)
            tongTien       // tổng tiền
    );

    frm.setVisible(true); // BLOCK tới khi đóng dialog

    // ===== NẾU THANH TOÁN THÀNH CÔNG =====
    if (frm.ketQua) {

        // 1. Cập nhật trạng thái bàn về TRỐNG
        updateTrangThaiBanSQL(currentTable.ma, 0);

        // 2. Xóa đơn trong RAM
        orderMap.remove(currentTable.ma);

        // 3. Reset giao diện
        currentTable = null;
        txtBanSo.setText("");
        txtMaHD.setText("");
        txtThoiGian.setText("");
        txtTongTien.setText("0");

        tblBill.setModel(new DefaultTableModel(
                new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0
        ));

        // 4. Reload sơ đồ bàn
        loadBanTuSQL();

        JOptionPane.showMessageDialog(this, "Đã thanh toán & đóng bàn thành công!");
    }
}


    private void chuyenBan() {
    // 1. Kiểm tra xem đã chọn bàn nguồn chưa và bàn đó có món không
    if (currentTable == null || !orderMap.containsKey(currentTable.ma) || orderMap.get(currentTable.ma).getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Bàn hiện tại không có khách hoặc không có món để chuyển!");
        return;
    }

    // 2. Hiển thị danh sách bàn trống để người dùng dễ chọn
    StringBuilder sb = new StringBuilder("Danh sách bàn TRỐNG khả dụng:\n");
    boolean coBanTrong = false;
    for (TableInfo t : listTables) {
        if (!t.hasCustomer) {
            sb.append("- Mã: ").append(t.ma).append(" (").append(t.ten).append(")\n");
            coBanTrong = true;
        }
    }

    if (!coBanTrong) {
        JOptionPane.showMessageDialog(this, "Hiện tại không còn bàn nào trống để chuyển đến!");
        return;
    }

    // 3. Nhập mã bàn đích
    String maMoi = JOptionPane.showInputDialog(this, sb.toString() + "\nNhập mã bàn muốn chuyển đến:");
    if (maMoi == null || maMoi.trim().isEmpty()) return;

    // 4. Tìm và kiểm tra bàn đích
    TableInfo banDich = null;
    for (TableInfo t : listTables) {
        if (t.ma.equalsIgnoreCase(maMoi.trim())) {
            banDich = t;
            break;
        }
    }

    // 🔥 LOGIC QUAN TRỌNG: Kiểm tra bàn đích có tồn tại và có trống không
    if (banDich == null) {
        JOptionPane.showMessageDialog(this, "Mã bàn vừa nhập không tồn tại!");
        return;
    }

    if (banDich.hasCustomer) {
        // Nếu bàn đích đã có khách (trạng thái != 0), báo lỗi ngay
        JOptionPane.showMessageDialog(this, "Bàn " + banDich.ten + " ĐANG CÓ KHÁCH! Không thể chuyển đến.");
        return;
    }

    // 5. Nếu vượt qua các kiểm tra trên -> Thực hiện chuyển
    // Chuyển Model dữ liệu sang mã bàn mới
    orderMap.put(banDich.ma, orderMap.get(currentTable.ma));
    orderMap.remove(currentTable.ma);

    // Cập nhật trạng thái SQL: Bàn cũ -> 0 (Trống), Bàn mới -> 1 (Có khách)
    updateTrangThaiBanSQL(currentTable.ma, 0);
    updateTrangThaiBanSQL(banDich.ma, 1);

    // 6. Làm mới giao diện
    JOptionPane.showMessageDialog(this, "Đã chuyển toàn bộ món từ " + currentTable.ten + " sang " + banDich.ten);
    
    currentTable = null; // Reset lựa chọn để nhân viên chọn lại bàn mới
    txtBanSo.setText("");
    txtTongTien.setText("0");
    tblBill.setModel(new DefaultTableModel(new String[]{"STT", "Tên món", "SL", "Giá", "Tổng"}, 0));
    
    loadBanTuSQL(); // Vẽ lại sơ đồ bàn để cập nhật màu sắc
}

    private void loadSanPhamFromSQL() {
        DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
        model.setRowCount(0);
        try (Connection c = db.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT Tensp, Dongia FROM sanpham WHERE trangthai = 'Đang bán'")) {
            int stt = 1;
            while (rs.next()) model.addRow(new Object[]{stt++, rs.getString("Tensp"), rs.getDouble("Dongia")});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSanPhamTheoLoai() {
        DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
        model.setRowCount(0);
        String sql = "SELECT Tensp, Dongia FROM sanpham WHERE Loaisanpham = ?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cbLoaiSP.getSelectedItem().toString());
            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) model.addRow(new Object[]{stt++, rs.getString("Tensp"), rs.getDouble("Dongia")});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void timSanPhamTheoTen() {
        DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
        model.setRowCount(0);
        String sql = "SELECT Tensp, Dongia FROM sanpham WHERE Tensp LIKE ?";
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + txtTimKiem.getText() + "%");
            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) model.addRow(new Object[]{stt++, rs.getString("Tensp"), rs.getDouble("Dongia")});
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg); btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false); btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void addLabelAndField(JPanel p, String text, JTextField tf, int y) {
        JLabel lb = new JLabel(text) {{ setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 13)); setBounds(10, y, 100, 25); }};
        tf.setBounds(110, y, 180, 25); p.add(lb); p.add(tf);
    }

    class TableInfo {
        String ma, ten; boolean hasCustomer;
        TableInfo(String m, String t, int tt) { ma = m; ten = t; hasCustomer = (tt != 0); }
    }

   
}