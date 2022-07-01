package com.dohuukhang.coolmate.Object;

public class Notification {
    private String Tieude, Noidung, Anh;
    public Notification(){

    }

    public Notification(String tieude, String noidung, String anh) {
        Tieude = tieude;
        Noidung = noidung;
        Anh = anh;
    }

    public String getTieude() {
        return Tieude;
    }

    public void setTieude(String tieude) {
        Tieude = tieude;
    }

    public String getNoidung() {
        return Noidung;
    }

    public void setNoidung(String noidung) {
        Noidung = noidung;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String anh) {
        Anh = anh;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "Tieude='" + Tieude + '\'' +
                ", Noidung='" + Noidung + '\'' +
                ", Anh='" + Anh + '\'' +
                '}';
    }
}
