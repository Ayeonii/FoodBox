package com.example.dldke.foodbox;

import android.net.Uri;

public class PencilCartItem {

    private Uri foodImg;
    private String foodName;
    private int foodDate;
    private double foodCount;


    public PencilCartItem (String foodName, Uri foodImg, int foodDate, double foodCount){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodDate = foodDate;
        this.foodCount = foodCount;
    }

    public String getFoodName() {
        return foodName;
    }

    public Uri getFoodImg() { return foodImg; }

    public double getFoodCount() {
        return foodCount;
    }
    public void setFoodCount(double cnt){
        foodCount = cnt;
    }

    public int getFoodDate() {
        return foodDate;
    }
}
