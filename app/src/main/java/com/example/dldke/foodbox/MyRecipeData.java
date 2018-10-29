package com.example.dldke.foodbox;

/*
    MyRecipe List 하나의 행에 대한 정보를 담을 클래스
 */
public class MyRecipeData {

    public int myrecipe_img;
    public String myrecipe_name;
    public String myrecipe_subname;

    public MyRecipeData(int myrecipe_img, String myrecipe_name, String myrecipe_subname){
        this.myrecipe_img = myrecipe_img;
        this.myrecipe_name = myrecipe_name;
        this.myrecipe_subname = myrecipe_subname;
    }
}
