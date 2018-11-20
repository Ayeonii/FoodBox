package com.example.dldke.foodbox.HalfRecipe;



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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWhich() {
        return which;
    }

    public void setWhich(int which) {
        this.which = which;
    }

    public Double getEditCount() {
        return editCount;
    }

    public void setEditCount(Double editCount) {
        this.editCount = editCount;
    }
}
