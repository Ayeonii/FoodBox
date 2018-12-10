package com.example.dldke.foodbox.FullRecipe;

import android.graphics.drawable.Drawable;

public class FullRecipeData {

    private Drawable StepImage;
    private String stepDescription;

    public Drawable getStepImage(){
        return StepImage;
    }

    public String getStepDescription(){
        return stepDescription;
    }

    public FullRecipeData(String stepDescription){
        this.stepDescription = stepDescription;
    }
}
