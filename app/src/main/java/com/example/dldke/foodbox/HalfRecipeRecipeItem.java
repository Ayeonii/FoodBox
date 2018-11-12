package com.example.dldke.foodbox;

public class HalfRecipeRecipeItem {
    private String name;
    private Double count;

    public HalfRecipeRecipeItem(String name, Double count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Double getCount() {
        return count;
    }
}
