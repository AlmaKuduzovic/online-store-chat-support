package com.example.rma_2_alma_kuduzovic;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_cart")
public class UserCartModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double price;
    private String userId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
