package cafe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmBan extends JFrame {

    private JTextField txtMaBan, txtTenBan;
    private JTable tblBan;
    private DefaultTableModel model;
    private JButton btnThem, btnXoa, btnLamMoi;
    
    private MyConnection db = new MyConnection();

    private final Color PRIMARY_COLOR = new Color(52, 152, 219); 
    private final Color SUCCESS_COLOR = new Color(46, 204, 113); 
    private final Color DANGER_COLOR = new Color(231, 76, 60);   
    private final Color PURPLE_COLOR = new Color(155, 89, 182);  
    private final Color BG_COLOR = new Color(236, 240, 241);     

    public FrmBan() {
        initComponents();
        refreshData(); 
    }

    private void refreshData() {
        db.loadDataBan(model); 
    }

    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ BÀN - ADMIN");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. MENU PHÍA TRÊN ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        String[] menuItems = {"BÀN", "THOÁT"};
        for (String item : menuItems) {
            JButton btnMenu = new JButton(item);
            btnMenu.setPreferredSize(new Dimension(130, 60));
            btnMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnMenu.setFocusPainted(false);
            btnMenu.setBorderPainted(false);
            btnMenu.setContentAreaFilled(false);
            btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (item.equals("BÀN")) {
                btnMenu.setForeground(PRIMARY_COLOR);
                btnMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, PRIMARY_COLOR));
            } else {
                btnMenu.setForeground(Color.GRAY);
            }
            
            // --- SỬA LẠI NÚT THOÁT ĐỂ VỀ QUẢN TRỊ HỆ THỐNG ---
            btnMenu.addActionListener(e -> {
                if (item.equals("THOÁT")) {
                    this.dispose(); // Đóng form Bàn
                    // CÚ PHÁP CHUẨN: Tạo đối tượng trước rồi mới setVisible
                    new FrmQuanLyHeThong().setVisible(true); 
                }
            });
            pnlHeader.add(btnMenu);
        }
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. PHẦN THÂN (BODY) ---
        JPanel pnlBody = new JPanel(new BorderLayout(25, 0));
        pnlBody.setBackground(BG_COLOR);
        pnlBody.setBorder(new EmptyBorder(25, 25, 25, 25));

        // BÊN TRÁI: NHẬP LIỆU
        JPanel pnlLeft = new JPanel(null);
        pnlLeft.setPreferredSize(new Dimension(320, 0));
        pnlLeft.setBackground(Color.WHITE);
        TitledBorder tb = BorderFactory.createTitledBorder(" THÔNG TIN BÀN ");
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlLeft.setBorder(tb);

        JLabel lblMa = new JLabel("Mã bàn:"); lblMa.setBounds(25, 40, 100, 25); pnlLeft.add(lblMa);
        txtMaBan = new JTextField(); txtMaBan.setBounds(25, 70, 270, 35); pnlLeft.add(txtMaBan);

        JLabel lblTen = new JLabel("Tên bàn:"); lblTen.setBounds(25, 120, 100, 25); pnlLeft.add(lblTen);
        txtTenBan = new JTextField(); txtTenBan.setBounds(25, 150, 270, 35); pnlLeft.add(txtTenBan);

        btnThem = createStyledButton("Thêm bàn", PURPLE_COLOR);
        btnThem.setBounds(25, 215, 130, 45);
        pnlLeft.add(btnThem);

        btnXoa = createStyledButton("Xóa bàn", DANGER_COLOR);
        btnXoa.setBounds(165, 215, 130, 45);
        pnlLeft.add(btnXoa);

        btnLamMoi = createStyledButton("Làm mới", SUCCESS_COLOR);
        btnLamMoi.setBounds(25, 275, 270, 45);
        pnlLeft.add(btnLamMoi);

        pnlBody.add(pnlLeft, BorderLayout.WEST);

        // BÊN PHẢI: BẢNG HIỂN THỊ
        String[] cols = {"STT", "Mã bàn", "Tên bàn", "Trạng thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblBan = new JTable(model);
        tblBan.setRowHeight(35);
        tblBan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlBody.add(new JScrollPane(tblBan), BorderLayout.CENTER);

        add(pnlBody, BorderLayout.CENTER);

        // --- CÁC SỰ KIỆN KHÁC ---
        btnThem.addActionListener(e -> {
            String ma = txtMaBan.getText().trim();
            String ten = txtTenBan.getText().trim();
            if (!ma.isEmpty() && !ten.isEmpty()) {
                db.insertBan(ma, ten);
                refreshData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Đã thêm bàn mới thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ mã và tên bàn!");
            }
        });

        btnXoa.addActionListener(e -> {
            int row = tblBan.getSelectedRow();
            if (row >= 0) {
                String ma = model.getValueAt(row, 1).toString();
                int conf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bàn " + ma + "?");
                if (conf == JOptionPane.YES_OPTION) {
                    db.deleteBan(ma);
                    refreshData();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần xóa!");
            }
        });

        btnLamMoi.addActionListener(e -> {
            refreshData();
            clearFields();
        });
        
        tblBan.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblBan.getSelectedRow();
                if (r >= 0) {
                    txtMaBan.setText(model.getValueAt(r, 1).toString());
                    txtTenBan.setText(model.getValueAt(r, 2).toString());
                    txtMaBan.setEditable(false);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void clearFields() {
        txtMaBan.setText("");
        txtTenBan.setText("");
        txtMaBan.setEditable(true);
        txtMaBan.requestFocus();
    }

   
}