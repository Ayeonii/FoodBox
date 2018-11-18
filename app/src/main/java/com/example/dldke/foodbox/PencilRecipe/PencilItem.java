package com.example.dldke.foodbox.PencilRecipe;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PencilItem {

    private Uri foodImg;
    private String foodName;
    private String foodSection;

    public PencilItem (String foodName, Uri foodImg, String foodSection){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodSection = foodSection;
    }

    public String getFoodName() {
        return foodName;
    }

    public Uri getFoodImg() { return foodImg; }

    public String getFoodSection() { return foodSection; }
}
