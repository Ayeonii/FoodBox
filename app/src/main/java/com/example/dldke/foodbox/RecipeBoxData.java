package com.example.dldke.foodbox;

public class RecipeBoxData {
    String name;
    Integer image;

    public RecipeBoxData(String name, Integer image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
