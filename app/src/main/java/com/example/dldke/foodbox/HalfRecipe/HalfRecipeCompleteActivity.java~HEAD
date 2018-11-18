package com.example.dldke.foodbox.HalfRecipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.MyRecipeBoxActivity;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.R;

public class HalfRecipeCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtGoRecipe, txtGoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe_complete);

        txtGoRecipe = (TextView) findViewById(R.id.txt_go_recipe);
        txtGoMain = (TextView) findViewById(R.id.txt_go_main);

        txtGoRecipe.setOnClickListener(this);
        txtGoMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_go_recipe:
                Log.d("test", "txt_go_recipe clicked");
                Intent myRecipeActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                myRecipeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myRecipeActivity);
                break;
            case R.id.txt_go_main:
                Log.d("test", "txt_go_main clicked");
                Intent refrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                refrigeratorMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refrigeratorMainActivity);
                break;
        }
    }
}
