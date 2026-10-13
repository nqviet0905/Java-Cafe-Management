package cafe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.util.ArrayList;


public class FrmDoanhThu extends JFrame {
    // Thay thế JComboBox bằng bộ chọn Tháng/Năm
    private JMonthChooser monthFrom, monthTo;
    private JYearChooser yearFrom, yearTo;
    
    private JTable table;
    private DefaultTableModel model;
    private java.util.ArrayList<Double> chartData = new java.util.ArrayList<>();
    private ChartPanel chartPanel;
    private MyConnection mc = new MyConnection();
    


    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color BG_LIGHT = new Color(245, 246, 250);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);

    public FrmDoanhThu() {
        setTitle("HỆ THỐNG QUẢN LÝ CAFE - THỐNG KÊ DOANH THU");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        // Mở lại form quản trị hệ thống
        new FrmQuanLyHeThong().setVisible(true);
        dispose();
    }
});

        getContentPane().setBackground(BG_LIGHT);
        setLayout(new BorderLayout());

      

        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Báo Cáo Doanh Thu Chi Tiết");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(SIDEBAR_COLOR);
        mainContent.add(lblHeader, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);

        // --- BỘ LỌC (Cập nhật để chọn mọi tháng/năm) ---
        JPanel filterCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterCard.setBackground(Color.WHITE);
        filterCard.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        
        // Khởi tạo bộ chọn thời gian "Từ"
        monthFrom = new JMonthChooser();
        yearFrom = new JYearChooser();
        
        // Khởi tạo bộ chọn thời gian "Đến"
        monthTo = new JMonthChooser();
        yearTo = new JYearChooser();

        JButton btnXem = createStyledButton("Xem Báo Cáo", PRIMARY_COLOR);
        JButton btnXuat = createStyledButton("Xuất Excel", SUCCESS_COLOR);

        filterCard.add(new JLabel("Từ:"));
        filterCard.add(monthFrom);
        filterCard.add(yearFrom);
        filterCard.add(new JLabel("  Đến:"));
        filterCard.add(monthTo);
        filterCard.add(yearTo);
        filterCard.add(Box.createHorizontalStrut(20)); // Tạo khoảng cách
        filterCard.add(btnXem);
        filterCard.add(btnXuat);

        centerPanel.add(filterCard, BorderLayout.NORTH);

        JPanel dataPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        dataPanel.setOpaque(false);

        JPanel chartCard = createContentCard("Xu hướng doanh thu");
        chartPanel = new ChartPanel();
        chartCard.add(chartPanel, BorderLayout.CENTER);

        JPanel tableCard = createContentCard("Chi tiết số liệu");
        String[] cols = {"STT", "Tháng/Năm", "Doanh Thu", "Chi Phí", "Lợi Nhuận"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        configureTable();
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        dataPanel.add(chartCard);
        dataPanel.add(tableCard);
        centerPanel.add(dataPanel, BorderLayout.CENTER);

        mainContent.add(centerPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        btnXem.addActionListener(e -> updateData());
    }

   

    private JPanel createContentCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        card.add(lblTitle, BorderLayout.NORTH);
        return card;
    }

    private void configureTable() {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(232, 241, 249));
        table.setSelectionForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

   private void updateData() {

    chartData.clear();

    int fromMonth = monthFrom.getMonth() + 1;
    int fromYear = yearFrom.getYear();

    int toMonth = monthTo.getMonth() + 1;
    int toYear = yearTo.getYear();

    // Kiểm tra thời gian hợp lệ
    if (fromYear > toYear || (fromYear == toYear && fromMonth > toMonth)) {
        JOptionPane.showMessageDialog(
                this,
                "Khoảng thời gian không hợp lệ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    model.setRowCount(0);

    ArrayList<Object[]> list =
            mc.getDoanhThuTheoThang(fromMonth, fromYear, toMonth, toYear);

    int stt = 1;

    for (Object[] row : list) {

        double doanhThu = (double) row[1];

        // 👉 Chi phí = 30% (có thể thay bằng bảng chi phí sau này)
        double chiPhi = doanhThu * 0.30;
        double loiNhuan = doanhThu - chiPhi;

        model.addRow(new Object[]{
                stt++,
                row[0], // Tháng/Năm
                String.format("%,.0f 000", doanhThu),
                String.format("%,.0f 000", chiPhi),
                String.format("%,.0f 000", loiNhuan)
        });

        chartData.add(doanhThu);
    }

    chartPanel.repaint();
}



    class ChartPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (chartData.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int barWidth = 50;
        int gap = 30;
        int x = 50;

       double max = chartData.stream()
        .mapToDouble(v -> v)
        .max()
        .orElse(1);


        for (int i = 0; i < chartData.size(); i++) {
            int barHeight = (int) ((chartData.get(i) / max) * (height - 80));

            g2.setColor(PRIMARY_COLOR);
            g2.fillRoundRect(
                    x,
                    height - barHeight - 40,
                    barWidth,
                    barHeight,
                    10,
                    10
            );

            x += barWidth + gap;
        }
    }
}

  
}