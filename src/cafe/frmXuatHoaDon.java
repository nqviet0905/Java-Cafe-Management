package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class frmXuatHoaDon extends JDialog {


    private String ngayLap, maHD, tenBan;
    private String tongTien, giamGia, tienKhach, tienThua, thanhTien;

  public frmXuatHoaDon(
        Window parent,
        String ngayLap,
        String maHD,
        String tenBan,
        String tongTien,
        String giamGia,
        String tienKhach,
        String tienThua,
        String thanhTien
) {
    super(parent, ModalityType.APPLICATION_MODAL);
        this.ngayLap = ngayLap;
        this.maHD = maHD;
        this.tenBan = tenBan;
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        this.tienKhach = tienKhach;
        this.tienThua = tienThua;
        this.thanhTien = thanhTien;

        initUI();
    }

    private void initUI() {
        setTitle("Hóa Đơn Thanh Toán");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        headerPanel.add(lblTitle);
        headerPanel.add(createLabelRow("Ngày giờ:", ngayLap));
        headerPanel.add(createLabelRow("Số HĐ:", maHD));
        headerPanel.add(createLabelRow("Bàn:", tenBan));

        JPanel footerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        footerPanel.add(createLabelRow("Tổng tiền:", tongTien));
        footerPanel.add(createLabelRow("Giảm giá:", giamGia));
        footerPanel.add(createLabelRow("Tiền khách:", tienKhach));
        footerPanel.add(createLabelRow("Tiền thừa:", tienThua));

        JLabel lblFinal = createLabelRow("THÀNH TIỀN:", thanhTien);
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFinal.setForeground(Color.RED);
        footerPanel.add(lblFinal);

        JPanel paperPanel = new JPanel();
        paperPanel.setLayout(new BoxLayout(paperPanel, BoxLayout.Y_AXIS));
        paperPanel.setBackground(Color.WHITE);
        paperPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        paperPanel.add(headerPanel);
        paperPanel.add(footerPanel);

        mainPanel.add(paperPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabelRow(String left, String right) {
        JLabel label = new JLabel(
                "<html><div style='width:250px;'>" +
                "<span style='float:left;'>" + left + "</span>" +
                "<span style='float:right;'>" + right + "</span>" +
                "</div></html>"
        );
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }
}
