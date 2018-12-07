package com.example.dldke.foodbox.HalfRecipe;

import android.net.Uri;

public class LocalRefrigeratorItem {
    private String name;
    private Double count;
    private String dueDate;
    private Uri img;
    private String section;

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

    public LocalRefrigeratorItem(String name, Double count, String dueDate, Uri img, String section) {
        this.name = name;
        this.count = count;
        this.dueDate = dueDate;
        this.img = img;
        this.section = section;
    }

    public LocalRefrigeratorItem(String name, Double count, String dueDate, String section) {
        this.name = name;
        this.count = count;
        this.dueDate = dueDate;
        this.section = section;
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

    public Uri getImg() {
        return img;
    }

    public String getSection() {
        return section;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((LocalRefrigeratorItem) obj).name);
    }
}
