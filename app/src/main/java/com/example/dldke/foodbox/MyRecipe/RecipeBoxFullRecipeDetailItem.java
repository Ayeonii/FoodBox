package com.example.dldke.foodbox.MyRecipe;


public class RecipeBoxFullRecipeDetailItem {
    String description;
    int stepImage;

    public RecipeBoxFullRecipeDetailItem(int stepImage, String description){
        this.description = description;
        this.stepImage = stepImage;
    }

    public String getDescription(){
        return description;
    }
}
