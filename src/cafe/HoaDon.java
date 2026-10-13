package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import java.text.DecimalFormat;

public class HoaDon extends JFrame {

    // ===== MÀU =====
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color BG_COLOR = new Color(245, 246, 250);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);

    DecimalFormat df = new DecimalFormat("#,##0");

    private JTable table;
    private DefaultTableModel tableModel;
    private JDateChooser d1, d2;

    // BIỂU ĐỒ
    private JPanel chartPanel;
    private java.util.ArrayList<String> chartValues = new java.util.ArrayList<>();
    private java.util.ArrayList<String> chartLabels = new java.util.ArrayList<>();

    public HoaDon() {
        setTitle("HỆ THỐNG QUẢN LÝ CAFE - CHI TIẾT HÓA ĐƠN");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new FrmQuanLyHeThong().setVisible(true);
                dispose();
            }
        });

        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout());

        JLabel lblHeader = new JLabel("Lịch Sử Giao Dịch & Thống Kê");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setBorder(new EmptyBorder(15, 20, 10, 20));
        add(lblHeader, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(480);
        splitPane.setDividerSize(2);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    // ================= LEFT =================
    private JPanel createLeftPanel() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setOpaque(false);

        JPanel filterCard = new JPanel(new GridBagLayout());
        filterCard.setBackground(Color.WHITE);
        filterCard.setBorder(new LineBorder(new Color(220, 220, 220)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        filterCard.add(new JLabel("Từ ngày:"), gbc);
        gbc.gridx = 1;
        d1 = new JDateChooser();
        filterCard.add(d1, gbc);

        gbc.gridx = 2;
        filterCard.add(new JLabel("Đến ngày:"), gbc);
        gbc.gridx = 3;
        d2 = new JDateChooser();
        filterCard.add(d2, gbc);

        JPanel btnRow = new JPanel();
        JButton btnThongKe = createButton("Xem Thống Kê", ACCENT_COLOR);
        JButton btnXoa = createButton("Xóa Hóa Đơn", DANGER_COLOR);
        JButton btnThoat = createButton("Thoát", DANGER_COLOR);

        btnThongKe.addActionListener(e -> loadData());

        // ✅ FIX: gán hành động cho nút XÓA
        btnXoa.addActionListener(e -> deleteSelectedInvoice());

        btnThoat.addActionListener(e -> {
            new FrmQuanLyHeThong().setVisible(true);
            dispose();
        });

        btnRow.add(btnXoa);
        btnRow.add(btnThongKe);
        btnRow.add(btnThoat);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        filterCard.add(btnRow, gbc);

        // ===== BIỂU ĐỒ =====
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new LineBorder(new Color(220, 220, 220)));

        JLabel lbl = new JLabel("Biểu đồ doanh thu theo hóa đơn", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chartPanel.add(lbl, BorderLayout.NORTH);

        root.add(filterCard, BorderLayout.NORTH);
        root.add(chartPanel, BorderLayout.CENTER);
        return root;
    }

    // ================= RIGHT =================
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] cols = {
                "STT", "Mã HD", "Bàn", "Tổng tiền", "Giảm giá",
                "Khách trả", "Tiền thừa", "Thành tiền", "Ngày lập"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, center);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // ================= LOAD DATA =================
    private void loadData() {
        tableModel.setRowCount(0);
        chartValues.clear();
        chartLabels.clear();

        if (d1.getDate() == null || d2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ ngày!");
            return;
        }

        MyConnection cn = new MyConnection();
        var list = cn.getHoaDonByDate(d1.getDate(), d2.getDate());
        int stt = 1;

        for (HoaDonGT hd : list) {
            tableModel.addRow(new Object[]{
                    stt++,
                    hd.getMa(),
                    hd.getTenBan(),
                    formatMoney(Double.parseDouble(hd.getTongTien())),
                    hd.getGiamGia(),
                    formatMoney(Double.parseDouble(hd.getTienKhach()) ),
                    formatMoney(Double.parseDouble(hd.getTienThua()) *1000),
                    formatMoney(Double.parseDouble(hd.getThanhTien()) *1000),
                    hd.getNgayLap()
            });

            chartLabels.add(hd.getMa() + "");
            chartValues.add(hd.getThanhTien());
        }

        chartPanel.removeAll();
        chartPanel.add(new StringBarChart(chartValues, chartLabels), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // ================= XÓA HÓA ĐƠN =================
    private void deleteSelectedInvoice() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa hóa đơn này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
           int maHD = Integer.parseInt(
        tableModel.getValueAt(selectedRow, 1).toString()
);

MyConnection cn = new MyConnection();
if (cn.deleteHoaDon(maHD)) {
 
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    // ================= BIỂU ĐỒ =================
    class StringBarChart extends JPanel {

        private java.util.List<String> values;
        private java.util.List<String> labels;

        public StringBarChart(java.util.List<String> values, java.util.List<String> labels) {
            this.values = values;
            this.labels = labels;
            setBackground(Color.WHITE);
        }

        private double parseMoney(String s) {
            try {
                return Double.parseDouble(
                        s.replace(",", "").replace("VNĐ", "").trim()
                );
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (values.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 50;

            double max = values.stream().mapToDouble(this::parseMoney).max().orElse(1);

            int barWidth = (width - 2 * padding) / values.size();

            for (int i = 0; i < values.size(); i++) {
                double v = parseMoney(values.get(i));
                int barHeight = (int) ((v / max) * (height - 2 * padding));

                int x = padding + i * barWidth;
                int y = height - padding - barHeight;

                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(x, y, barWidth - 10, barHeight, 10, 10);

                g2.setColor(Color.BLACK);
                g2.drawString(labels.get(i), x + 10, height - padding + 15);
            }
        }
    }

    // ================= UTIL =================
    private JButton createButton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

private String formatMoney(double v) {
    return String.format("%,.0f", v);
}


}
