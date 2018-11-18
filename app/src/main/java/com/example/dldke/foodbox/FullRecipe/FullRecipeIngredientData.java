package com.example.dldke.foodbox.FullRecipe;

import android.net.Uri;

public class FullRecipeIngredientData {
    private Uri ingredient_img;
    private String ingredient_name;

    public FullRecipeIngredientData(String name, Uri img){
        this.ingredient_img = img;
        this.ingredient_name = name;
    }

    public String getIngredientName() {
        return this.ingredient_name;
    }

    public Uri getIngredientImage() {
        return this.ingredient_img;
    }
}
