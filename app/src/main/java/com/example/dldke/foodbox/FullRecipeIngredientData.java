package com.example.dldke.foodbox;

public class FullRecipeIngredientData {
    private int ingredient_img;
    private String ingredient_name;

    public FullRecipeIngredientData(String name, int img){
        this.ingredient_img = img;
        this.ingredient_name = name;
    }

    public String getIngredientName() {
        return this.ingredient_name;
    }

    public int getIngredientImage() {
        return this.ingredient_img;
    }
}
