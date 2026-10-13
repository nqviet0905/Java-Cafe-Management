package cafe;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import javax.swing.JComboBox;


public class MyConnection {

    private String user;
    private String password;
    private String port;
    private String host;
    private String dbname;
    private String url;
    private Connection conn;

    public MyConnection() {
        user = "root";
        password = "";
        port = "3306";
        host = "localhost";
        dbname = "javacafe";
        url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false&serverTimezone=UTC";
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==============================
    // LẤY TẤT CẢ TÀI KHOẢN
    // ==============================
    public ArrayList<Taikhoan> getAll() {
        ArrayList<Taikhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";

        try (Connection c = getConnection()) {
            if (c == null) throw new SQLException("Connection null!");

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Taikhoan(
                        rs.getInt("Ma"),
                        rs.getString("Matkhau"),
                        rs.getString("Ten"),
                        rs.getString("MaCCCD"),     // ✔ String
                        rs.getString("QueQuan"),
                        rs.getString("DienThoai"),  // ✔ String
                        rs.getString("DiaChi"),
                        rs.getInt("Quyen"),
                        rs.getInt("TrangThai"),
                        rs.getInt("Luong")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // INSERT
    // ==============================
    public void insertTaiKhoan(Taikhoan tk) {
        String sql = "INSERT INTO taikhoan(Ma, Matkhau, Ten, MaCCCD, QueQuan, DienThoai, DiaChi, Quyen, TrangThai, Luong) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, tk.getMa());
            ps.setString(2, tk.getMatkhau());
            ps.setString(3, tk.getTen());
            ps.setString(4, tk.getMaCCCD());    // ✔ String, không convert số
            ps.setString(5, tk.getQueQuan() == null ? "Unknown" : tk.getQueQuan());
            ps.setString(6, tk.getDienThoai()); // ✔ String
            ps.setString(7, tk.getDiaChi());
            ps.setInt(8, tk.getQuyen());
            ps.setInt(9, tk.getTrangThai());
            ps.setInt(10, tk.getLuong());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Lỗi INSERT: " + e.getMessage());
        }
    }

    // ==============================
    // UPDATE
    // ==============================
    public void updateTaiKhoan(Taikhoan tk) {
        String sql = "UPDATE taikhoan SET Ten=?, MaCCCD=?, QueQuan=?, DienThoai=?, DiaChi=?, Quyen=?, TrangThai=?, Luong=? WHERE Ma=?";

        try (Connection c = getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, tk.getTen());
            pst.setString(2, tk.getMaCCCD());     
            pst.setString(3, tk.getQueQuan() == null ? "Unknown" : tk.getQueQuan());
            pst.setString(4, tk.getDienThoai());  
            pst.setString(5, tk.getDiaChi());
            pst.setInt(6, tk.getQuyen());
            pst.setInt(7, tk.getTrangThai());
            pst.setInt(8, tk.getLuong());
            pst.setInt(9, tk.getMa());

            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Lỗi UPDATE: " + e.getMessage());
        }
    }
    public boolean changePassword(String username, String oldPass, String newPass) {
    String check = "SELECT * FROM taikhoan WHERE Ten = ? AND MatKhau = ?";
    String update = "UPDATE taikhoan SET MatKhau = ? WHERE Ten = ?";

    try (Connection c = getConnection()) {
        if (c == null) throw new SQLException("Connection null!");

        // kiểm tra mật khẩu cũ
        PreparedStatement psCheck = c.prepareStatement(check);
        psCheck.setString(1, username);
        psCheck.setString(2, oldPass);

        ResultSet rs = psCheck.executeQuery();
        if (!rs.next()) {
            return false; // mật khẩu cũ sai
        }

        // cập nhật mật khẩu mới
        PreparedStatement psUpdate = c.prepareStatement(update);
        psUpdate.setString(1, newPass);
        psUpdate.setString(2, username);
        psUpdate.executeUpdate();

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public void resetPassword(int maTK) {
    String sql = "UPDATE taikhoan SET Matkhau = '1234567' WHERE Ma = ?";

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, maTK);
        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println("Lỗi reset mật khẩu: " + e.getMessage());
    }
}
public void deleteTaiKhoan(int maTK) {
    String sql = "DELETE FROM taikhoan WHERE Ma = ?";
    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, maTK);
        int rows = ps.executeUpdate();
        if (rows == 0) {
            System.out.println("Không tìm thấy tài khoản để xóa!");
        }
    } catch (Exception e) {
        System.out.println("Lỗi xóa tài khoản: " + e.getMessage());
    }
}

// --- Thêm vào MyConnection.java ---

// 1. Hàm lưu bàn mới vào SQL
public void insertBan(String ma, String ten) {
    // Sửa lại cho đúng tên cột: ma, tenban, trangthai
    String sql = "INSERT INTO ban(ma, tenban, trangthai) VALUES(?,?,0)";
    try (Connection c = getConnection(); 
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, ma);
        ps.setString(2, ten);
        ps.executeUpdate();
        System.out.println("Lưu thành công vào Database!");
    } catch (Exception e) {
        System.out.println("Lỗi thêm bàn: " + e.getMessage());
    }
}

// 2. Hàm xóa bàn khỏi SQL
public void deleteBan(String ma) {
    // Sửa cột WHERE ma
    String sql = "DELETE FROM ban WHERE ma = ?";
    try (Connection c = getConnection(); 
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setString(1, ma);
        ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Lỗi xóa bàn: " + e.getMessage());
    }
}

// 3. Hàm Load dữ liệu lên bảng
// 3. Hàm Load dữ liệu lên bảng (Cập nhật để hiển thị bàn Mang về)
public void loadDataBan(javax.swing.table.DefaultTableModel model) {
    model.setRowCount(0);
    // Sắp xếp để bàn Mang về luôn hiện lên đầu danh sách nếu muốn (tùy chọn)
    String sql = "SELECT * FROM ban ORDER BY CASE WHEN ma LIKE 'MV%' THEN 0 ELSE 1 END, ma ASC"; 
    
    try (Connection c = getConnection(); 
         Statement st = c.createStatement(); 
         ResultSet rs = st.executeQuery(sql)) {
        
        int stt = 1;
        while (rs.next()) {
            String ma = rs.getString("ma");
            String ten = rs.getString("tenban");
            int tt = rs.getInt("trangthai");
            
            // Xử lý hiển thị tên có icon cho bàn mang về ngay tại bảng dữ liệu
            String tenHienThi = ten;
            if (ma.equalsIgnoreCase("MV1") ) {
                tenHienThi = "🛍️ " + ten;
            }

            model.addRow(new Object[]{
                stt++, 
                ma, 
                tenHienThi, 
                tt == 0 ? "Bàn trống" : "Có khách"
            });
        }
    } catch (Exception e) {
        System.out.println("Lỗi loadDataBan: " + e.getMessage());
    }
}
// Thêm hàm này vào MyConnection.java
public void updateTrangThaiBan(String ma, int trangThai) {
    String sql = "UPDATE ban SET trangthai = ? WHERE ma = ?";
    try (Connection c = getConnection(); 
         PreparedStatement ps = c.prepareStatement(sql)) {
        ps.setInt(1, trangThai);
        ps.setString(2, ma);
        ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Lỗi cập nhật trạng thái: " + e.getMessage());
    }
}
// 1. Load dữ liệu từ bảng sanpham
// LOAD SẢN PHẨM
    public void loadDataSanPham(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT * FROM sanpham";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int stt = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                        stt++,
                        rs.getString("Masp"),
                        rs.getString("Tensp"),
                        rs.getString("Loaisanpham"),
                        rs.getDouble("Dongia"),
                        rs.getString("khuyenmai"),
                        rs.getString("trangthai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// INSERT SẢN PHẨM
    public void insertSanPham(
            String ma, String ten, double gia,
            String khuyenMai, String trangThai, String loai) {

        String sql = """
            INSERT INTO sanpham
            (Masp, Tensp, Dongia, khuyenmai, trangthai, Loaisanpham)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ma);
            ps.setString(2, ten);
            ps.setDouble(3, gia);
            ps.setString(4, khuyenMai);
            ps.setString(5, trangThai);
            ps.setString(6, loai);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// DELETE SẢN PHẨM
    public boolean deleteSanPham(String ma) {
        String sql = "DELETE FROM sanpham WHERE Masp=?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ma);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

 // LOAD BẢNG LOẠI SP (FrmLoaisanpham)
    public void loadDataLoaiSP(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT * FROM loaisanpham";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int stt = 1;
            while (rs.next()) {
                int ma = rs.getInt("Ma");
                String ten = rs.getString("Tenloai");
                int tt = rs.getInt("Trangthai");

                model.addRow(new Object[]{
                        stt++,
                        String.format("LSP%03d", ma),
                        ten,
                        tt == 1 ? "Đang hoạt động" : "Khóa"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// INSERT LOẠI SP
    public void insertLoaiSP(String ten, int trangThai) {
        String sql = "INSERT INTO loaisanpham(Tenloai, Trangthai) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setInt(2, trangThai);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// DELETE LOẠI SP
    public void deleteLoaiSP(int ma) {
        String sql = "DELETE FROM loaisanpham WHERE Ma=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ma);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// UPDATE LOẠI SP
    public void updateLoaiSP(int ma, String ten, int trangThai) {
        String sql = "UPDATE loaisanpham SET Tenloai=?, Trangthai=? WHERE Ma=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setInt(2, trangThai);
            ps.setInt(3, ma);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public String getNextMaSanPham() {
    String sql = "SELECT MAX(Masp) FROM sanpham";
    try (Connection c = getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        if (rs.next() && rs.getString(1) != null) {
            String maxMa = rs.getString(1); // VD: SP007
            int so = Integer.parseInt(maxMa.replace("SP", ""));
            return "SP" + String.format("%03d", so + 1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "SP001"; // nếu bảng rỗng
}
public int getNextMaLoaiSP() {
    int next = 1;
    String sql = "SELECT MAX(Ma) FROM loaisanpham";

    try (
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql)
    ) {
        if (rs.next()) {
            next = rs.getInt(1) + 1;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return next;
}
// ===== LOAD LOẠI SẢN PHẨM CHO COMBOBOX =====
public void loadLoaiSPToCombo(JComboBox<String> cb) {
    cb.removeAllItems();
    String sql = "SELECT Tenloai FROM loaisanpham WHERE Trangthai = 1";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            cb.addItem(rs.getString("Tenloai"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void checkAndCreateMangVe() {
    String sqlCheck = "SELECT COUNT(*) FROM ban WHERE ma IN ('MV1')";
    try (Connection c = getConnection(); 
         Statement st = c.createStatement(); 
         ResultSet rs = st.executeQuery(sqlCheck)) {
        
        if (rs.next() && rs.getInt(1) == 0) {
            String sqlInsert = "INSERT IGNORE INTO ban (ma, tenban, trangthai) VALUES " +
                               "('MV1', 'Mang về 1', 0)";
                               
            st.executeUpdate(sqlInsert);
            System.out.println("Đã khởi tạo 2 bàn mang về thành công!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    //                  XỬ LÝ HÓA ĐƠN
    // ===================================================

    // 1. Thêm hóa đơn mới (INSERT)
   // ===============================
// THÊM HÓA ĐƠN MỚI
// ===============================
public void insertHoaDon(HoaDonGT hd) {

    String sql = """
        INSERT INTO hoadon
        (TenBan, NgayLap, TongTien, GiamGia,
         ThanhTien, TienKhach, TienThua, TrangThai)
        VALUES (?,?,?,?,?,?,?,?)
    """;

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        // ❗❗❗ THIẾU 2 DÒNG NÀY
        ps.setString(1, hd.getTenBan());
        ps.setTimestamp(2, hd.getNgayLap());

        ps.setBigDecimal(3, new BigDecimal(hd.getTongTien()));
        ps.setBigDecimal(4, new BigDecimal(
                hd.getGiamGia().isEmpty() ? "0" : hd.getGiamGia()
        ));
        ps.setBigDecimal(5, new BigDecimal(hd.getThanhTien()));
        ps.setBigDecimal(6, new BigDecimal(hd.getTienKhach()));
        ps.setBigDecimal(7, new BigDecimal(hd.getTienThua()));
        ps.setString(8, "Đã Thanh Toán");

        ps.executeUpdate();

        System.out.println("✔ Lưu hóa đơn thành công!");

    } catch (Exception e) {
        System.out.println("❌ Lỗi thêm hóa đơn: ");
        e.printStackTrace();
    }
    
}

    // 2. Lấy tất cả hóa đơn (SELECT ALL) - Trả về ArrayList để xử lý logic
    public ArrayList<HoaDonGT> getAllHoaDon() {
        ArrayList<HoaDonGT> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon ORDER BY NgayLap DESC"; // Sắp xếp mới nhất lên đầu

        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                HoaDonGT hd = new HoaDonGT(
                    rs.getInt("Ma"),
                    rs.getString("TenBan"),
                    rs.getTimestamp("NgayLap"),
                    rs.getString("TongTien"),
                    rs.getString("GiamGia"),
                    rs.getString("ThanhTien"),
                    rs.getString("TienKhach"),
                    rs.getString("TienThua")
                );
                list.add(hd);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return list;
    }

    // 3. Lọc hóa đơn theo ngày (Dùng cho chức năng thống kê)
    public ArrayList<HoaDonGT> getHoaDonByDate(java.util.Date fromDate, java.util.Date toDate) {
        ArrayList<HoaDonGT> list = new ArrayList<>();
        // SQL so sánh khoảng ngày
        String sql = "SELECT * FROM hoadon WHERE NgayLap >= ? AND NgayLap <= ?";

        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            // Chuyển đổi java.util.Date sang java.sql.Timestamp
            ps.setTimestamp(1, new Timestamp(fromDate.getTime()));
            
            // Cộng thêm 1 ngày vào toDate hoặc set giờ về cuối ngày để lấy đủ dữ liệu ngày cuối
            // Ở đây dùng cách đơn giản là convert
            ps.setTimestamp(2, new Timestamp(toDate.getTime()));

            ResultSet rs = ps.executeQuery();
           while (rs.next()) {
    // Tạo đúng đối tượng HoaDon và dùng đúng hàm get theo kiểu dữ liệu DB
    list.add(new HoaDonGT(
        rs.getInt("Ma"),             
        rs.getString("TenBan"),     
        rs.getTimestamp("NgayLap"),   
        rs.getString("TongTien"),  
        rs.getString("GiamGia"),   
        rs.getString("ThanhTien"),  
        rs.getString("TienKhach"),  
        rs.getString("TienThua")     
    ));
}
        } catch (Exception e) {
            System.out.println("Lỗi lọc hóa đơn: " + e.getMessage());
        }
        return list;
    }
    
    // ===================================================
    //              DOANH THU THEO THÁNG / NĂM
    // ===================================================

  public ArrayList<Object[]> getDoanhThuTheoThang(
        int fromMonth, int fromYear,
        int toMonth, int toYear) {

    ArrayList<Object[]> list = new ArrayList<>();

    String sql = """
        SELECT
            YEAR(NgayLap) AS Nam,
            MONTH(NgayLap) AS Thang,
            SUM(ThanhTien) AS DoanhThu
        FROM hoadon
        WHERE TrangThai = 'Đã Thanh Toán'
          AND (
                (YEAR(NgayLap) > ? OR (YEAR(NgayLap) = ? AND MONTH(NgayLap) >= ?))
            AND (YEAR(NgayLap) < ? OR (YEAR(NgayLap) = ? AND MONTH(NgayLap) <= ?))
          )
        GROUP BY YEAR(NgayLap), MONTH(NgayLap)
        ORDER BY Nam, Thang
    """;

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, fromYear);
        ps.setInt(2, fromYear);
        ps.setInt(3, fromMonth);

        ps.setInt(4, toYear);
        ps.setInt(5, toYear);
        ps.setInt(6, toMonth);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String thangNam = String.format(
                    "%02d/%d",
                    rs.getInt("Thang"),
                    rs.getInt("Nam")
            );

            double doanhThu = rs.getDouble("DoanhThu");

            list.add(new Object[]{
                    thangNam,
                    doanhThu
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

   // ===============================
// LOAD HÓA ĐƠN LÊN JTable
// ===============================
public void loadHoaDon(javax.swing.table.DefaultTableModel model) {
    model.setRowCount(0);

    String sql = """
        SELECT Ma, TenBan, NgayLap,
               TongTien, GiamGia, ThanhTien,
               TienKhach, TienThua, TrangThai
        FROM hoadon
        ORDER BY NgayLap DESC
    """;

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        int stt = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                stt++,
                rs.getInt("Ma"),
                rs.getString("TenBan"),
                rs.getTimestamp("NgayLap"),
                rs.getDouble("TongTien"),
                rs.getDouble("GiamGia"),
                rs.getDouble("ThanhTien"),
                rs.getDouble("TienKhach"),
                rs.getDouble("TienThua"),
                rs.getString("TrangThai")
            });
        }

    } catch (Exception e) {
        System.out.println("Lỗi load hóa đơn: " + e.getMessage());
    }
}
// ===============================
// XÓA HÓA ĐƠN
// ===============================
public boolean deleteHoaDon(int maHD) {

    String sql = "DELETE FROM hoadon WHERE Ma = ?";

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, maHD);
        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

// LẤY HÓA ĐƠN (KHÔNG NHÂN VIÊN)
// ==========================
public ResultSet getHoaDon(String maHD, String ngayLap) {

    StringBuilder sql = new StringBuilder("""
        SELECT Ma, ThanhTien, NgayLap
        FROM hoadon
        WHERE 1=1
    """);

    if (!maHD.isEmpty()) sql.append(" AND Ma = ?");
    if (!ngayLap.isEmpty()) sql.append(" AND DATE(NgayLap) = ?");

    try {
        Connection c = getConnection();
        PreparedStatement ps = c.prepareStatement(sql.toString());

        int i = 1;
        if (!maHD.isEmpty()) ps.setInt(i++, Integer.parseInt(maHD));
        if (!ngayLap.isEmpty()) ps.setDate(i++, Date.valueOf(ngayLap));

        return ps.executeQuery();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}




public void insertLuong(int maNV, String tenNV, String thang,
                        int luongCB, int soCa, int tongLuong, String ghiChu) {

    String sql = """
        INSERT INTO luong
        (MaNV, TenNV, Thang, Luong, SoCa, TongLuong, GhiChu)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    try (Connection c = getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, maNV);
        ps.setString(2, tenNV);
        ps.setString(3, thang);
        ps.setInt(4, luongCB);
        ps.setInt(5, soCa);
        ps.setInt(6, tongLuong);
        ps.setString(7, ghiChu);

        ps.executeUpdate();

      } catch (Exception e) {
        e.printStackTrace();
        
    }
}

}


    

