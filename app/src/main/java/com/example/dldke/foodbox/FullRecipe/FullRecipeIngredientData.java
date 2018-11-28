package com.example.dldke.foodbox.FullRecipe;

import android.net.Uri;

public class FullRecipeIngredientData {
    private Uri ingredient_img;
    private String ingredient_name;
    private boolean checked = false;

    public FullRecipeIngredientData(String name, Uri img){
        this.ingredient_img = img;
        this.ingredient_name = name;
    }

    public FullRecipeIngredientData(String name, boolean check){
        this.ingredient_name = name;
        this.checked = check;
    }

    public String getIngredientName() {
        return this.ingredient_name;
    }

    public Uri getIngredientImage() {
        return this.ingredient_img;
    }

    public boolean isChecked() {
        return checked;
    }
}
