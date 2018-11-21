package com.example.dldke.foodbox.HalfRecipe;

import java.util.ArrayList;

public interface HalfRecipeDialogListener {
    public void onPositiveClicked(String type, Boolean[] check);
    public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray);
    public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> mItems);
}
