package com.example.dldke.foodbox.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dldke.foodbox.HalfIngredientsDialog;
import com.example.dldke.foodbox.R;

public class HalfRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnMeat, btnFruit;
    private FloatingActionButton fbtnRecipe;

    private String reqCategory, reqContent;

    private HalfIngredientsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFruit = (Button) findViewById(R.id.btn_fruit);
        fbtnRecipe = (FloatingActionButton) findViewById(R.id.floatingButtonRecipe);

        btnSidedish.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFruit.setOnClickListener(this);
        fbtnRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                reqCategory = "sideDish";
                reqContent = "sideDish칸 속 재료";
                dialog = new HalfIngredientsDialog(this, reqCategory);
                dialog.show();
                break;
            case R.id.btn_meat:
                reqCategory = "meat";
                reqContent = "meat칸 속 재료";
                dialog = new HalfIngredientsDialog(this, reqCategory);
                dialog.show();
                break;
            case R.id.btn_fruit:
                reqCategory = "fresh";
                reqContent = "fresh칸 속 재료";
                dialog = new HalfIngredientsDialog(this, reqCategory);
                dialog.show();
                break;
            case R.id.floatingButtonRecipe:
                break;
        }
    }
}
