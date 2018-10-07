package com.example.dldke.foodbox;

import android.graphics.drawable.Drawable;

public class PencilItem {

    private Drawable foodImg;
    private String foodName;


    public PencilItem (String foodName, Drawable foodImg){
        this.foodImg = foodImg;
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public Drawable getFoodImg() { return foodImg; }
}
