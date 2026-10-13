package cafe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FrmThanhToan extends JDialog {

    private int maHD;
    private String tongTien;
    private String tenHD;
    public boolean ketQua = false;

    private JTextField txtNgayLap = new JTextField();
    private JTextField txtMaHD = new JTextField();
    private JTextField txtSoTienKhach = new JTextField();
    private JTextField txtTongTien = new JTextField();
    private JTextField txtTienThua = new JTextField();
    private JTextField txtGiamGia = new JTextField();
    private JTextField txtThanhTien = new JTextField();

    private JButton btnXuatHD = new JButton("Xuất hóa đơn");
    private JButton btnHuy = new JButton("Hủy");

    public FrmThanhToan(JFrame parent, String tenHD, String tenBan, int maHD, String tongTien) {
        super(parent, true);

        this.tenHD = tenHD;
        this.maHD = maHD;
        this.tongTien = tongTien.replace(",", "");

        setTitle("Thanh toán - " + tenBan);
        setSize(400, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Ngày lập:"));
        txtNgayLap.setText(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        txtNgayLap.setEditable(false);
        panel.add(txtNgayLap);

        panel.add(new JLabel("Mã hóa đơn:"));
        txtMaHD.setText(tenHD);
        txtMaHD.setEditable(false);
        panel.add(txtMaHD);

        panel.add(new JLabel("Số tiền khách:"));
        panel.add(txtSoTienKhach);

        panel.add(new JLabel("Tổng tiền:"));
        txtTongTien.setText(this.tongTien);
        txtTongTien.setEditable(false);
        panel.add(txtTongTien);

        panel.add(new JLabel("Giảm giá:"));
        panel.add(txtGiamGia);

        panel.add(new JLabel("Thành tiền:"));
        txtThanhTien.setText(this.tongTien);
        txtThanhTien.setEditable(false);
        panel.add(txtThanhTien);

        panel.add(new JLabel("Tiền thừa:"));
        txtTienThua.setText("0");
        txtTienThua.setEditable(false);
        panel.add(txtTienThua);

        JPanel pnlBtn = new JPanel();
        pnlBtn.add(btnXuatHD);
        pnlBtn.add(btnHuy);

        add(panel, BorderLayout.CENTER);
        add(pnlBtn, BorderLayout.SOUTH);

        txtSoTienKhach.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhTien();
            }
        });

        txtGiamGia.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                tinhTien();
            }
        });

        btnXuatHD.addActionListener(e -> xuatHoaDon());
        btnHuy.addActionListener(e -> dispose());
    }

    public FrmThanhToan(String tenHD, String tenBan, int maHD, String tongTien) {
        this(null, tenHD, tenBan, maHD, tongTien);
    }

    private void tinhTien() {
        double tong = Double.parseDouble(tongTien);
        double giam = parseMoney(txtGiamGia);
        if (giam > tong) giam = 0;

        double thanh = tong - giam;
        txtThanhTien.setText(format(thanh));

        double khach = parseMoney(txtSoTienKhach);
        txtTienThua.setText(format(Math.max(0, khach - thanh)));
    }

    private void xuatHoaDon() {

        double thanh = parseMoney(txtThanhTien);
        double khach = parseMoney(txtSoTienKhach);

        if (khach < thanh) {
            JOptionPane.showMessageDialog(this, "Khách chưa đưa đủ tiền!");
            return;
        }

        try {
            MyConnection cn = new MyConnection();

            HoaDonGT hd = new HoaDonGT(
                0,
                txtMaHD.getText(),
                new java.sql.Timestamp(System.currentTimeMillis()),
                txtTongTien.getText().replace(",", ""),
                txtGiamGia.getText().replace(",", ""),
                txtThanhTien.getText().replace(",", ""),
                txtSoTienKhach.getText().replace(",", ""),
                txtTienThua.getText().replace(",", "")
            );

            cn.insertHoaDon(hd);   // ✅ LƯU DB

            ketQua = true;         // ✅ ĐÁNH DẤU THÀNH CÔNG

            frmXuatHoaDon frm = new frmXuatHoaDon(
                SwingUtilities.getWindowAncestor(this),
                txtNgayLap.getText(),
                txtMaHD.getText(),
                getTitle().replace("Thanh toán - ", ""),
                txtTongTien.getText(),
                txtGiamGia.getText(),
                txtSoTienKhach.getText(),
                txtTienThua.getText(),
                txtThanhTien.getText()
            );

            frm.setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu hóa đơn!");
            ex.printStackTrace();
        }
    }

    private String format(double n) {
        return new DecimalFormat("#,###").format(n);
    }

    private double parseMoney(JTextField txt) {
        try {
            return Double.parseDouble(txt.getText().replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
