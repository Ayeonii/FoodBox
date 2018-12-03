package com.example.dldke.foodbox.MyRecipe;

import android.net.Uri;

public class RecipeBoxData {
    String foodname, recipeId, simplename;
    Integer image;
    String food_image;
    boolean isShared, isIng;

    public RecipeBoxData(String recipeId, String imgUrl, String foodname, boolean isShared){
        this.foodname = foodname;
        //this.image = image;
        this.food_image = imgUrl;
        this.recipeId = recipeId;
        this.isShared = isShared;
    }

    public RecipeBoxData(String simplename, String recipeId, boolean isIng){
        this.simplename = simplename;
        this.recipeId = recipeId;
        this.isIng = isIng;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getSimpleName(){
        return simplename;
    }

    public void setSimplename(String simplename){
        this.simplename = simplename;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getRecipeId(){
        return recipeId;
    }

    public boolean isShared() {
        return isShared;
    }

    public boolean isIng() {
        return isIng;
    }
}
