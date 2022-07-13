package com.vnpt.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "XA_TABLE")
public class KhachHang implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private Integer ID;

    private String NAME;

    private String THON;

    private Integer SOTIEN;

    private String MST;

    private String DIACHI;

    private String CUSNAME;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTHON() {
        return THON;
    }

    public void setTHON(String THON) {
        this.THON = THON;
    }

    public Integer getSOTIEN() {
        return SOTIEN;
    }

    public void setSOTIEN(Integer SOTIEN) {
        this.SOTIEN = SOTIEN;
    }

    public String getMST() {
        return MST;
    }

    public void setMST(String MST) {
        this.MST = MST;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }

    public String getCUSNAME() {
        return CUSNAME;
    }

    public void setCUSNAME(String CUSNAME) {
        this.CUSNAME = CUSNAME;
    }

    public KhachHang(Integer ID, String NAME, String THON, Integer SOTIEN, String MST, String DIACHI, String CUSNAME) {
        this.ID = ID;
        this.NAME = NAME;
        this.THON = THON;
        this.SOTIEN = SOTIEN;
        this.MST = MST;
        this.DIACHI = DIACHI;
        this.CUSNAME = CUSNAME;
    }
    public KhachHang(KhachHang khachHang) {
        this.ID = khachHang.ID;
        this.NAME = khachHang.NAME;
        this.THON = khachHang.THON;
        this.SOTIEN = khachHang.SOTIEN;
        this.MST = khachHang.MST;
        this.DIACHI = khachHang.DIACHI;
        this.CUSNAME = khachHang.CUSNAME;
    }
}
