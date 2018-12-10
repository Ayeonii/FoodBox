package com.example.dldke.foodbox.MyRecipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeIngredientAdapter;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.MyRecipe.CustomDialog;


import java.io.InputStream;
import java.util.List;


public class RecipeBoxFullRecipeDetailActivity extends AppCompatActivity {


    private MyRecipeBoxFullRecipeAdapter myRecipeBoxFullRecipeAdapter = new MyRecipeBoxFullRecipeAdapter();
    private MyRecipeBoxHalfRecipeAdapter myRecipeBoxHalfRecipeAdapter = new MyRecipeBoxHalfRecipeAdapter();
    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private String recipe_id;
    private boolean isCookingClass = refrigeratorMainActivity.getisCookingClass();
    private RecipeDO.Detail detail;
    private RecyclerView detail_recyclerview, detail_ingredient_recyclerview;
    private RecipeBoxFullRecipeDetailAdapter recipeDetailAdapter;
    private FullRecipeIngredientAdapter recipeIngredientAdapter;
    private List<RecipeDO.Ingredient> data;

    String TAG = "FullRecipeDetailActivity";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_fullrecipe_detail);

        boolean isPost = myRecipeBoxHalfRecipeAdapter.IsPost();

        if(isPost){
            recipe_id = myRecipeBoxHalfRecipeAdapter.getRecipeId();
        }
        else{
            recipe_id = myRecipeBoxFullRecipeAdapter.getRecipeId();
        }


        String imgUrl = Mapper.getImageUrlRecipe(recipe_id);
        boolean isShared = Mapper.searchRecipe(recipe_id).getIsShare();

        Toolbar toolbar = (Toolbar)findViewById(R.id.recipe_box_fullrecipe_detail_toolbar);
        ImageView mainImg = (ImageView)findViewById(R.id.fullrecipe_detail_foodimg);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.fullrecipe_detail_collasping_toolbar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        Button post_fullrecipe_write = (Button) findViewById(R.id.fullrecipe_write);


        new DownloadImageTask(mainImg).execute(imgUrl);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detail = Mapper.searchRecipe(recipe_id).getDetail();
        String foodName = detail.getFoodName();
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));


        detail_ingredient_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_ingredient_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detail_ingredient_recyclerview.setLayoutManager(mLayoutManager);
        data = Mapper.searchRecipe(recipe_id).getIngredient();
        recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
        detail_ingredient_recyclerview.setAdapter(recipeIngredientAdapter);


        detail_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_view);
        detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recipeDetailAdapter = new RecipeBoxFullRecipeDetailAdapter(recipe_id);
        detail_recyclerview.setAdapter(recipeDetailAdapter);


        if(isShared){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }

        if(isPost){
            post_fullrecipe_write.setVisibility(View.VISIBLE);
        }
        else{
            post_fullrecipe_write.setVisibility(View.INVISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlImg =urls[0];
            Bitmap foodImg = null;
            try {
                InputStream in = new java.net.URL(urlImg).openStream();
                foodImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodImg;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    void showDialog(){

        CustomDialog customDialog = new CustomDialog(this, isCookingClass);
        customDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = customDialog.getWindow();
        window.setAttributes(lp);

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
