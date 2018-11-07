package com.example.dldke.foodbox;

public class HalfRecipeRecipeItem {
    private String name;
    private int count;

    public HalfRecipeRecipeItem(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
