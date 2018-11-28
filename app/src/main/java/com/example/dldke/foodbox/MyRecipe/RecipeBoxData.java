package com.example.dldke.foodbox.MyRecipe;

public class RecipeBoxData {
    String foodname, recipeId, simplename;
    Integer image;
    boolean isShared;

    public RecipeBoxData(String recipeId, Integer image, String foodname, boolean isShared){
        this.foodname = foodname;
        this.image = image;
        this.recipeId = recipeId;
        this.isShared = isShared;
    }

    public RecipeBoxData(String simplename, String recipeId){
        this.simplename = simplename;
        this.recipeId = recipeId;
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
}
