package cafe;

public class Taikhoan {
    private int Ma;
    private String Matkhau;
    private String Ten;
    private String MaCCCD;
    private String QueQuan;
    private String DienThoai;
    private String DiaChi;
    private int Quyen;
    private int TrangThai;
    private int Luong;

    public Taikhoan(int Ma, String Matkhau, String Ten, String MaCCCD, String QueQuan, String DienThoai, String DiaChi, int Quyen, int TrangThai, int Luong) {
        this.Ma = Ma;
        this.Matkhau = Matkhau;
        this.Ten = Ten;
        this.MaCCCD = MaCCCD;
        this.QueQuan = QueQuan;
        this.DienThoai = DienThoai;
        this.DiaChi = DiaChi;
        this.Quyen = Quyen;
        this.TrangThai = TrangThai;
        this.Luong = Luong;
    }

    public int getMa() {
        return Ma;
    }

    public void setMa(int Ma) {
        this.Ma = Ma;
    }

    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String Matkhau) {
        this.Matkhau = Matkhau;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String Ten) {
        this.Ten = Ten;
    }

    public String getMaCCCD() {
        return MaCCCD;
    }

    public void setMaCCCD(String MaCCCD) {
        this.MaCCCD = MaCCCD;
    }

    public String getQueQuan() {
        return QueQuan;
    }

    public void setQueQuan(String QueQuan) {
        this.QueQuan = QueQuan;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String DienThoai) {
        this.DienThoai = DienThoai;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public int getQuyen() {
        return Quyen;
    }

    public void setQuyen(int Quyen) {
        this.Quyen = Quyen;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int TrangThai) {
        this.TrangThai = TrangThai;
    }

    public int getLuong() {
        return Luong;
    }

    public void setLuong(int Luong) {
        this.Luong = Luong;
    }
    public String toString() {
        return Ten;
    }
}
