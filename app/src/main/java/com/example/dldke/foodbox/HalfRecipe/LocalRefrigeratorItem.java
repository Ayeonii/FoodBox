package com.example.dldke.foodbox.HalfRecipe;

public class LocalRefrigeratorItem {
    private String name;
    private Double count;
    private String dueDate;

    public LocalRefrigeratorItem(String name) {
        this.name = name;
    }

    public LocalRefrigeratorItem(String name, Double count) {
        this.name = name;
        this.count = count;
    }

    public LocalRefrigeratorItem(String name, Double count, String dueDate) {
        this.name = name;
        this.count = count;
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((LocalRefrigeratorItem) obj).name);
    }
}
