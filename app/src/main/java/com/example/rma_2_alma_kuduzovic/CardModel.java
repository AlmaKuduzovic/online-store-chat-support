package com.example.rma_2_alma_kuduzovic;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cards")
public class CardModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int imageResource;
    private String title;

    private double price;

    public CardModel(int imageResource, String title, double price) {
        this.imageResource = imageResource;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
