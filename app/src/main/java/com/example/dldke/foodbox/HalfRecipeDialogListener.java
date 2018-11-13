package com.example.dldke.foodbox;

public interface HalfRecipeDialogListener {
    public void onPositiveClicked(String type, Boolean[] check);
    public void onCompleteClicked(int result);
}
