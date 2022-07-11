package com.vnpt.dto;

import java.io.Serializable;

public class HDDTGLI implements Serializable {

    private String ten;
    private String coQuan;
    private String diaChi;
    private String mst;
    private String extra;

    public HDDTGLI(String ten, String coQuan, String diaChi, String mst, String extra) {
        this.ten = ten;
        this.coQuan = coQuan;
        this.diaChi = diaChi;
        this.mst = mst;
        this.extra = extra;
    }

    public HDDTGLI() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCoQuan() {
        return coQuan;
    }

    public void setCoQuan(String coQuan) {
        this.coQuan = coQuan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMst() {
        return mst;
    }

    public void setMst(String mst) {
        this.mst = mst;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
