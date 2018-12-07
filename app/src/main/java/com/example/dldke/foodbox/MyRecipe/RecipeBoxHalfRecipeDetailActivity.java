package com.example.dldke.foodbox.MyRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeActivity;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeBoxHalfRecipeDetailActivity extends AppCompatActivity {
    public RecipeBoxHalfRecipeDetailActivity(){}

    public String getRecipeId(){
        return recipe_id;
    }

    private String TAG = "RecipeBoxHalfRecipeDetailActivity";

    private MyRecipeBoxHalfRecipeAdapter myRecipeBoxHalfRecipeAdapter = new MyRecipeBoxHalfRecipeAdapter();
    private String recipe_id = myRecipeBoxHalfRecipeAdapter.getRecipeId();
    private RecipeBoxHalfRecipeDetailAdapter recipeBoxHalfRecipeDetailAdapter;
    private List<RecipeDO.Ingredient> recipeItems = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_halfrecipe_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.recipe_box_halfrecipe_detail_toolbar);
        TextView recipe_title = (TextView)findViewById(R.id.recipe_title);
        RecyclerView recipe_detail_view = (RecyclerView)findViewById(R.id.ingredient_detail_view);
        Button ingredient_use = (Button)findViewById(R.id.ingredient_use);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeItems = Mapper.searchRecipe(recipe_id).getIngredient();
        String simplename = Mapper.searchRecipe(recipe_id).getSimpleName();
        recipe_title.setText(simplename);


        recipe_detail_view.setLayoutManager(new LinearLayoutManager(this));
        recipeBoxHalfRecipeDetailAdapter = new RecipeBoxHalfRecipeDetailAdapter(recipeItems);
        recipe_detail_view.setAdapter(recipeBoxHalfRecipeDetailAdapter);



        ingredient_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mapper.updateIngInfo(2, recipe_id);
                /********** 여기 부분에 유통기한 체크 추가 ************/

//                FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
//                fullRecipeActivity.setIsHalfRecipe(true);
//                Intent FullRecipeActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.FullRecipe.FullRecipeActivity.class);
//                startActivity(FullRecipeActivity);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent MyRecipeBoxActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
            MyRecipeBoxActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(MyRecipeBoxActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
