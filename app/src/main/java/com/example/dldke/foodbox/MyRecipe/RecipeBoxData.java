package com.example.dldke.foodbox.MyRecipe;

public class RecipeBoxData {
    String name, recipeId, simplename;
    Integer image;

    public RecipeBoxData(String name, Integer image, String recipeId){
        this.name = name;
        this.image = image;
        this.recipeId = recipeId;
    }

    public RecipeBoxData(String simplename, String recipeId){
        this.simplename = simplename;
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
