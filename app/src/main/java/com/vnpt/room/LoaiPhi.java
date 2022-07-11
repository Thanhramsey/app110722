package com.vnpt.room;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LOAI_PHI_TABLE")
public class LoaiPhi implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private Integer ID;

    private String NAME;

    private Integer VAT_RATE;

    private Float VAT_AMOUNT;

    private Float TOTAL;

    private Float AMOUNT;

    private String SERIAL;

    private String PATTERN;

    private String MST;

    public LoaiPhi() {
    }

    public LoaiPhi(Integer ID, String NAME, Integer VAT_RATE, Float VAT_AMOUNT, Float TOTAL,
                   Float AMOUNT, String SERIAL, String PATTERN, String MST) {
        this.ID = ID;
        this.NAME = NAME;
        this.VAT_RATE = VAT_RATE;
        this.VAT_AMOUNT = VAT_AMOUNT;
        this.TOTAL = TOTAL;
        this.AMOUNT = AMOUNT;
        this.SERIAL = SERIAL;
        this.PATTERN = PATTERN;
        this.MST = MST;
    }

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

    public Integer getVAT_RATE() {
        return VAT_RATE;
    }

    public void setVAT_RATE(Integer VAT_RATE) {
        this.VAT_RATE = VAT_RATE;
    }

    public Float getVAT_AMOUNT() {
        return VAT_AMOUNT;
    }

    public void setVAT_AMOUNT(Float VAT_AMOUNT) {
        this.VAT_AMOUNT = VAT_AMOUNT;
    }

    public Float getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(Float TOTAL) {
        this.TOTAL = TOTAL;
    }

    public Float getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(Float AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getSERIAL() {
        return SERIAL;
    }

    public void setSERIAL(String SERIAL) {
        this.SERIAL = SERIAL;
    }

    public String getPATTERN() {
        return PATTERN;
    }

    public void setPATTERN(String PATTERN) {
        this.PATTERN = PATTERN;
    }

    public String getMST() {
        return MST;
    }

    public void setMST(String MST) {
        this.MST = MST;
    }

    @Override
    public String toString() {
        return this.AMOUNT + "-" + this.NAME;
    }
}
