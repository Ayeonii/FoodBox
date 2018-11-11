package com.example.dldke.foodbox;

import android.net.Uri;

public class PencilCartItem {

    private Uri foodImg;
    private String foodName;
    private int foodDate;
    private double foodCount;
    private String foodSection;


    public PencilCartItem (String foodName, Uri foodImg, int foodDate, double foodCount, String foodSection){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodDate = foodDate;
        this.foodCount = foodCount;
        this.foodSection = foodSection;
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

    public String getFoodSection (){ return foodSection;}
}
