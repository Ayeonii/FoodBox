package com.example.dldke.foodbox.HalfRecipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.R;

public class HalfRecipeCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGoRecipe, btnGoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe_complete);

        btnGoRecipe = (Button) findViewById(R.id.btn_go_recipe);
        btnGoMain = (Button) findViewById(R.id.btn_go_main);

        btnGoRecipe.setOnClickListener(this);
        btnGoMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_recipe:
                Intent myRecipeActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                myRecipeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myRecipeActivity);
                break;
            case R.id.btn_go_main:
                Intent refrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                refrigeratorMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refrigeratorMainActivity);
                break;
        }
    }
}
