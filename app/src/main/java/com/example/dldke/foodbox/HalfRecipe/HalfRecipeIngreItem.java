package com.example.dldke.foodbox.HalfRecipe;

public class HalfRecipeIngreItem {
    private String name;
    private String section;

    public HalfRecipeIngreItem(String name) {
        this.name = name;
    }

    public HalfRecipeIngreItem(String name, String section) {
        this.name = name;
        this.section = section;
    }

    public String getName() {
        return name;
    }
    public String getSection(){return section;}
}
