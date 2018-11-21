package com.example.dldke.foodbox.HalfRecipe;

import java.util.ArrayList;

public interface HalfRecipeDialogListener {
    public void onPositiveClicked(String type, Boolean[] check);
    public void onCompleteClicked(int result, ArrayList<String> dueDateCheckArray);
}
