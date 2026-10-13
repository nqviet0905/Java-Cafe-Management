package cafe;

import java.sql.Timestamp;

public class HoaDonGT {
    private int Ma;
    private String TenBan;
    private Timestamp NgayLap; // Sử dụng Timestamp cho datetime
    private String TongTien;
    private String GiamGia;
    private String ThanhTien;
    private String TienKhach;
    private String TienThua;

    public HoaDonGT(int Ma, String TenBan, Timestamp NgayLap, String TongTien, String GiamGia, String ThanhTien, String TienKhach, String TienThua) {
        this.Ma = Ma;
        this.TenBan = TenBan;
        this.NgayLap = NgayLap;
        this.TongTien = TongTien;
        this.GiamGia = GiamGia;
        this.ThanhTien = ThanhTien;
        this.TienKhach = TienKhach;
        this.TienThua = TienThua;
    }

    public int getMa() {
        return Ma;
    }

    public void setMa(int Ma) {
        this.Ma = Ma;
    }

    public String getTenBan() {
        return TenBan;
    }

    public void setTenBan(String TenBan) {
        this.TenBan = TenBan;
    }

    public Timestamp getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Timestamp NgayLap) {
        this.NgayLap = NgayLap;
    }

    public String getTongTien() {
        return TongTien;
    }

    public void setTongTien(String TongTien) {
        this.TongTien = TongTien;
    }

    public String getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(String GiamGia) {
        this.GiamGia = GiamGia;
    }

    public String getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(String ThanhTien) {
        this.ThanhTien = ThanhTien;
    }

    public String getTienKhach() {
        return TienKhach;
    }

    public void setTienKhach(String TienKhach) {
        this.TienKhach = TienKhach;
    }

    public String getTienThua() {
        return TienThua;
    }

    public void setTienThua(String TienThua) {
        this.TienThua = TienThua;
    }

    
} 
    

   