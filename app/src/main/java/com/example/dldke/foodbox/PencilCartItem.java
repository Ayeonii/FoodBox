package com.example.dldke.foodbox;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class PencilCartItem {

    private Uri foodImg;
    private String foodName;
    private String foodDate;
    private double foodCount;
    private String foodSection;
    private int foodDueDays ;


    public PencilCartItem (String foodName, Uri foodImg, String foodDate,
                           double foodCount, String foodSection,
                           int foodDueDays){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodDate = foodDate;
        this.foodCount = foodCount;
        this.foodSection = foodSection;
        this.foodDueDays = foodDueDays;
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

    public String getFoodDate() {
        return foodDate;
    }
    public void setFoodDate(String date){ foodDate = date;}

    public String getFoodSection (){ return foodSection;}

    public int getFoodDueDays() {
        return foodDueDays;
    }
}
