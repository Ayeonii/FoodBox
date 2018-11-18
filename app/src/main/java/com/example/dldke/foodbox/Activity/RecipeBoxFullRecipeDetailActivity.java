package com.example.dldke.foodbox.Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.dldke.foodbox.Adapter.RecipeBoxAdapter;
import com.example.dldke.foodbox.Adapter.RecipeBoxFullRecipeDetailAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.RecipeBoxData;


public class RecipeBoxFullRecipeDetailActivity extends AppCompatActivity {

    //풀레시피함에서 선택된 레시피 아이디 받아오기
    private RecipeBoxAdapter recipeBoxAdapter = new RecipeBoxAdapter();
    private String recipe_id = recipeBoxAdapter.getRecipeId();
    RecipeDO.Detail detail;

    RecyclerView detail_recyclerview;
    private RecipeBoxFullRecipeDetailAdapter recipeDetailAdapter;

    String TAG = "FullRecipeDetail";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_fullrecipe_detail);
        Log.e(TAG, "레시피 아이디 제대로 받아왔니니니니"+recipe_id);
        detail = Mapper.searchRecipe(recipe_id).getDetail();
        Log.e(TAG, "풀레시피 음식 이름"+detail.getFoodName());
        Toolbar toolbar = (Toolbar)findViewById(R.id.fullrecipe_detail_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.fullrecipe_detail_collasping_toolbar);
        String foodName = detail.getFoodName();
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        Log.e(TAG, "여기까지는 왔니니니니");

        detail_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_view);
        detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recipeDetailAdapter = new RecipeBoxFullRecipeDetailAdapter(recipe_id);
        detail_recyclerview.setAdapter(recipeDetailAdapter);

    }
}
