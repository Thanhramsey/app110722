package com.vnpt.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "TRUBOM_TABLE")
public class TruBom implements Serializable {

    @PrimaryKey
    private Integer Id;

    private String Name;

    private Integer IDLoaiXang;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getIDLoaiXang() {
        return IDLoaiXang;
    }

    public void setIDLoaiXang(Integer IDLoaiXang) {
        this.IDLoaiXang = IDLoaiXang;
    }
}
