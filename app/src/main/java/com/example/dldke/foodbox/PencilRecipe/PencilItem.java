package com.example.dldke.foodbox.PencilRecipe;

import android.net.Uri;

public class PencilItem {

    private Uri foodImg;
    private String foodName;
    private String foodSection;
    private Boolean isFrozen;

    public PencilItem (String foodName, Uri foodImg){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodSection = foodSection;
    }

    public PencilItem (String foodName, Uri foodImg, String foodSection){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodSection = foodSection;
    }


    public PencilItem (String foodName, Uri foodImg, String foodSection, Boolean isFrozen){
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodSection = foodSection;
        this.isFrozen = isFrozen;
    }

    public String getFoodName() {
        return foodName;
    }

    public Uri getFoodImg() { return foodImg; }

    public String getFoodSection() { return foodSection; }

    public Boolean getIsFrozen() { return isFrozen; }

    public void setIsFrozen(Boolean isFrozen) { this.isFrozen = isFrozen;}

}
