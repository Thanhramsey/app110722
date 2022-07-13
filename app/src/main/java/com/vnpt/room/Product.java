package com.vnpt.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private int price;
    private int productID;


    public Product(int productID, String name, int price) {
        this.name = name;
        this.price = price;
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
