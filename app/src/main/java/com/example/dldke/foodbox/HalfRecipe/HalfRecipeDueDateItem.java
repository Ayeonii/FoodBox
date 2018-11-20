package com.example.dldke.foodbox.HalfRecipe;

<<<<<<< Updated upstream
import android.net.Uri;

public class HalfRecipeRecipeItem {
    private String name;
    private Double count, editCount;
    private Uri image;

    public HalfRecipeRecipeItem(String name, Double count, Uri image) {
        this.name = name;
        this.count = count;
        this.image = image;
=======

public class HalfRecipeDueDateItem {
    private String name;
    private int which;
    private Double editCount;

    public HalfRecipeDueDateItem(String name) {
        this.name = name;
    }

    public HalfRecipeDueDateItem(String name, int which) {
        this.name = name;
        this.which = which;
    }

    public HalfRecipeDueDateItem(String name, int which, Double editCount) {
        this.name = name;
        this.which = which;
        this.editCount = editCount;
>>>>>>> Stashed changes
    }

    public String getName() {
        return name;
    }

<<<<<<< Updated upstream
    public Double getCount() {
        return count;
    }

    public Uri getImage(){
        return image;
=======
    public void setName(String name) {
        this.name = name;
    }

    public int getWhich() {
        return which;
    }

    public void setWhich(int which) {
        this.which = which;
>>>>>>> Stashed changes
    }

    public Double getEditCount() {
        return editCount;
    }

    public void setEditCount(Double editCount) {
        this.editCount = editCount;
    }
}
