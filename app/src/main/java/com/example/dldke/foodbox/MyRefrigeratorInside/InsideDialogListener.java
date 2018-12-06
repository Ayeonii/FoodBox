package com.example.dldke.foodbox.MyRefrigeratorInside;

import com.example.dldke.foodbox.HalfRecipe.DCItem;

import java.util.ArrayList;

public interface InsideDialogListener {
    public void onPositiveClicked(Double count, String dueDate);
    public void onOkClicked(ArrayList<DCItem> dcItems);
}
