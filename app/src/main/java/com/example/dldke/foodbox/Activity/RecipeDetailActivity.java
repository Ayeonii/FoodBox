package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.RecipeBoxAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    public RecipeDetailActivity(){}

    public String getRecipeId(){
        return recipe_id;
    }

    private String TAG = "RecipeDetailActivity";

    private RecipeBoxAdapter recipeBoxAdapter = new RecipeBoxAdapter();
    private String recipe_id = recipeBoxAdapter.getRecipeId();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView recipe_title = (TextView)findViewById(R.id.recipe_title);
        TextView ingredient_name = (TextView)findViewById(R.id.ingredient_name);
        TextView ingredient_name2 = (TextView)findViewById(R.id.ingredient_name2);
        TextView ingredient_number = (TextView)findViewById(R.id.ingredient_number);
        TextView ingredient_number2 = (TextView)findViewById(R.id.ingredient_number2);

        recipe_title.setText(recipe_id+"상세 내용");

        List<RecipeDO.Ingredient> ingredientsList = Mapper.searchRecipe(recipe_id).getIngredient();
        for(int i = 0; i<ingredientsList.size(); i++){
            String ingredName = ingredientsList.get(i).getIngredientName();
            Double ingredNumber = ingredientsList.get(i).getIngredientCount();

            ingredient_name.setText(ingredName);

            Log.d(TAG, "재료 이름 : "+ingredName + "재료 개수 : "+ingredNumber);
        }

        Button ingredient_gain = (Button)findViewById(R.id.ingredient_gain);
        ingredient_gain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FullRecipeActivity = new Intent(getApplicationContext(), FullRecipeActivity.class);
                startActivity(FullRecipeActivity);
            }
        });

    }
}
