package com.example.dldke.foodbox;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PencilItem {

    private Uri foodImg;
    private String foodName;


    public PencilItem (String foodName, Uri foodImg){
        this.foodImg = foodImg;
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public Uri getFoodImg() { return foodImg; }
}
