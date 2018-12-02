package com.example.dldke.foodbox.HalfRecipe;

import android.net.Uri;

public class HalfRecipeRecipeItem {
    private String name;
    private Double count, editCount, needCount;
    private Uri image;

    public HalfRecipeRecipeItem(String name, Double count, Uri image) {
        this.name = name;
        this.count = count;
        this.image = image;
    }

    public HalfRecipeRecipeItem(int n, String name, Double needCount, Uri image) {
        this.name = name;
        this.needCount = needCount;
        this.image = image;
    }

    public HalfRecipeRecipeItem(String name, Double needCount) {
        this.name = name;
        this.needCount = needCount;
    }

    public String getName() {
        return name;
    }

    public Double getCount() {
        return count;
    }

    public Uri getImage(){
        return image;
    }

    public Double getEditCount() {
        return editCount;
    }

    public void setEditCount(Double editCount) {
        this.editCount = editCount;
    }

    public Double getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Double needCount) {
        this.needCount = needCount;
    }
}
