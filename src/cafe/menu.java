package cafe;

public class menu {
    private int Ma;
    private String Tensp;
    private int Soluong;
    private double Giacb;
    private double Tongtien;
    private String Masp;

    public menu(int Ma, String Tensp, int Soluong, double Giacb, double Tongtien, String Masp) {
        this.Ma = Ma;
        this.Tensp = Tensp;
        this.Soluong = Soluong;
        this.Giacb = Giacb;
        this.Tongtien = Tongtien;
        this.Masp = Masp;
    }

    public int getMa() {
        return Ma;
    }

    public String getTensp() {
        return Tensp;
    }

    public int getSoluong() {
        return Soluong;
    }

    public double getGiacb() {
        return Giacb;
    }

    public double getTongtien() {
        return Tongtien;
    }

    public String getMasp() {
        return Masp;
    }

    public void setMa(int Ma) {
        this.Ma = Ma;
    }

    public void setTensp(String Tensp) {
        this.Tensp = Tensp;
    }

    public void setSoluong(int Soluong) {
        this.Soluong = Soluong;
    }

    public void setGiacb(double Giacb) {
        this.Giacb = Giacb;
    }

    public void setTongtien(double Tongtien) {
        this.Tongtien = Tongtien;
    }

    public void setMasp(String Masp) {
        this.Masp = Masp;
    }
    
    
    }

