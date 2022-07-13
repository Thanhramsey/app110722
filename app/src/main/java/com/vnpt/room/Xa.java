package com.vnpt.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "XA_TABLE")
public class Xa implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private Integer ID;

    private String NAME;

    private Integer DONVIID;

    private String USERNAME;

    public Xa(Integer ID, String NAME, Integer DONVIID, String USERNAME) {
        this.ID = ID;
        this.NAME = NAME;
        this.DONVIID = DONVIID;
        this.USERNAME = USERNAME;
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

    public Integer getDONVIID() {
        return DONVIID;
    }

    public void setDONVIID(Integer getDONVIID) {
        this.DONVIID = DONVIID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    @Override
    public String toString() {
        return this.NAME;
    }
}
