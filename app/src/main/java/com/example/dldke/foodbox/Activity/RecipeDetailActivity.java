package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.MyRecipeBoxDetailAdapter;
import com.example.dldke.foodbox.Adapter.RecipeBoxAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    public RecipeDetailActivity(){}

    public String getRecipeId(){
        return recipe_id;
    }

    private String TAG = "RecipeDetailActivity";

    //간이레시피함에서 선택된 레시피 아이디 받아오기
    private RecipeBoxAdapter recipeBoxAdapter = new RecipeBoxAdapter();
    private String recipe_id = recipeBoxAdapter.getRecipeId();

    //레시피재료 데이터 보여주는 것
    private MyRecipeBoxDetailAdapter myRecipeBoxDetailAdapter;
    private List<RecipeDO.Ingredient> recipeItems = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView recipe_title = (TextView)findViewById(R.id.recipe_title);
        recipe_title.setText(recipe_id);

        recipeItems = Mapper.searchRecipe(recipe_id).getIngredient();

        RecyclerView recipe_detail_view = (RecyclerView)findViewById(R.id.ingredient_detail_view);
        recipe_detail_view.setLayoutManager(new LinearLayoutManager(this));
        myRecipeBoxDetailAdapter = new MyRecipeBoxDetailAdapter(recipeItems);
        recipe_detail_view.setAdapter(myRecipeBoxDetailAdapter);


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
