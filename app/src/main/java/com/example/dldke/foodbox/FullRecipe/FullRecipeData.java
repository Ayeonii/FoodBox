package com.example.dldke.foodbox.FullRecipe;

import android.graphics.drawable.Drawable;

public class FullRecipeData {

    private String Method;
    private String Minute;
    private String Fire;
    private Drawable StepImage;

    public String getMethod() {
        return Method;
    }

    public String getMinute() {
        return Minute;
    }

    public String getFire() {
        return Fire;
    }

    public Drawable getStepImage(){
        return StepImage;
    }

    public FullRecipeData(String method, String minute, String fire) {
        this.Method = method;
        this.Minute = minute;
        this.Fire = fire;
    }
}
