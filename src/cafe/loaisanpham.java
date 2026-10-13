package cafe;

public class loaisanpham {
    private int Ma;
    private String Tenloai;
    private int Trangthai;

    public loaisanpham(int Ma, String Tenloai, int Trangthai) {
        this.Ma = Ma;
        this.Tenloai = Tenloai;
        this.Trangthai = Trangthai;
    }

    public int getMa() {
        return Ma;
    }

    public String getTenloai() {
        return Tenloai;
    }

    public int getTrangthai() {
        return Trangthai;
    }

    public void setMa(int Ma) {
        this.Ma = Ma;
    }

    public void setTenloai(String Tenloai) {
        this.Tenloai = Tenloai;
    }

    public void setTrangthai(int Trangthai) {
        this.Trangthai = Trangthai;
    }
    
}