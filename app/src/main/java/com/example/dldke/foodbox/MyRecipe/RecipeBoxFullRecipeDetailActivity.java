package com.example.dldke.foodbox.MyRecipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;


import java.io.InputStream;


public class RecipeBoxFullRecipeDetailActivity extends AppCompatActivity {

    //풀레시피함에서 선택된 레시피 아이디 받아오기
    private MyRecipeBoxFullRecipeAdapter myRecipeBoxFullRecipeAdapter = new MyRecipeBoxFullRecipeAdapter();
    private String recipe_id;
    RecipeDO.Detail detail;

    RecyclerView detail_recyclerview;
    private RecipeBoxFullRecipeDetailAdapter recipeDetailAdapter;

    String TAG = "FullRecipeDetail";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_fullrecipe_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.recipe_box_fullrecipe_detail_toolbar);
        ImageView mainImg = (ImageView)findViewById(R.id.fullrecipe_detail_foodimg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe_id = myRecipeBoxFullRecipeAdapter.getRecipeId();
        detail = Mapper.searchRecipe(recipe_id).getDetail();


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.fullrecipe_detail_collasping_toolbar);
        String foodName = detail.getFoodName();
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        String imgUrl = Mapper.getImageUrlRecipe(recipe_id);

        new DownloadImageTask(mainImg).execute(imgUrl);


        detail_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_view);
        detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recipeDetailAdapter = new RecipeBoxFullRecipeDetailAdapter(recipe_id);
        detail_recyclerview.setAdapter(recipeDetailAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    //이미지 가져오기 비동기
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유하시겠습니까?");
        builder.setMessage("레시피 타이틀을 작성해주세요");
        builder.setView(edittext);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getApplicationContext(), "예를 선택했습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), edittext.getText().toString(), Toast.LENGTH_SHORT).show();
                String title = edittext.getText().toString();
                Mapper.createPost(" "+title, recipe_id);

                Intent MainActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.Activity.RefrigeratorMainActivity.class);
                MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(MainActivity);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
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
