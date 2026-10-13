package cafe;

public class DoanhThu {
    private int thang;
    private int nam;
    private String tongTienBan;
    private String tongDoanhThu;

    public DoanhThu(int thang, int nam, String tongTienBan, String tongDoanhThu) {
        this.thang = thang;
        this.nam = nam;
        this.tongTienBan = tongTienBan;
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public String getTongTienBan() {
        return tongTienBan;
    }

    public void setTongTienBan(String tongTienBan) {
        this.tongTienBan = tongTienBan;
    }

    public String getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(String tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
    
}