package com.example.dldke.foodbox.Store;

import android.graphics.drawable.Drawable;

public class StoreData {

    int theme_version;
    String theme_title;
    int theme_point;
    int theme_image;

    public int getTheme_version() {
        return theme_version;
    }

    public void setTheme_version(int theme_version) {
        this.theme_version = theme_version;
    }

    public String getTheme_title() {
        return theme_title;
    }

    public void setTheme_title(String theme_title) {
        this.theme_title = theme_title;
    }

    public int getTheme_point() {
        return theme_point;
    }

    public void setTheme_point(int theme_point) {
        this.theme_point = theme_point;
    }

    public StoreData(int theme_version, int theme_image, String theme_title, int theme_point) {
        this.theme_version = theme_version;
        this.theme_title = theme_title;
        this.theme_point = theme_point;
        this.theme_image = theme_image;
    }
}
