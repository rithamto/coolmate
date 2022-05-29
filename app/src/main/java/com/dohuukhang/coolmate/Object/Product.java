package com.dohuukhang.coolmate.Object;

public class Product {
    private String Ten, HinhAnh, DanhMuc, Mota, GiaTien;

    public Product() {
    }

    public Product(String ten, String hinhAnh, String giaTien, String danhMuc, String mota) {
        Ten = ten;
        HinhAnh = hinhAnh;
        DanhMuc = danhMuc;
        Mota = mota;
        GiaTien = giaTien;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getDanhMuc() {
        return DanhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        DanhMuc = danhMuc;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }

    public String getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(String giaTien) {
        GiaTien = giaTien;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Ten='" + Ten + '\'' +
                ", HinhAnh='" + HinhAnh + '\'' +
                ", DanhMuc='" + DanhMuc + '\'' +
                ", Mota='" + Mota + '\'' +
                ", GiaTien='" + GiaTien + '\'' +
                '}';
    }
}
