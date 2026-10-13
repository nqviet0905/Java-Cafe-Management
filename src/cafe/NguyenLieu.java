
package cafe;

import java.time.LocalDate;

public class NguyenLieu {
    private String maNL;
    private String tenNL;
    private LocalDate ngayNhap;
    private int gia;
    private String ghiChu;

    public NguyenLieu(String maNL, String tenNL, LocalDate ngayNhap, int gia, String ghiChu) {
        this.maNL = maNL;
        this.tenNL = tenNL;
        this.ngayNhap = ngayNhap;
        this.gia = gia;
        this.ghiChu = ghiChu;
    }

    public String getMaNL() { return maNL; }
    public String getTenNL() { return tenNL; }
    public LocalDate getNgayNhap() { return ngayNhap; }
    public int getGia() { return gia; }
    public String getGhiChu() { return ghiChu; }
}
