package com.example.dldke.foodbox.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dldke.foodbox.Adapter.MyRecipeAdapter;
import com.example.dldke.foodbox.MyRecipeData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;


public class MyRecipeActivity extends AppCompatActivity {

    RecyclerView myrecipeRecyclerView;
    RecyclerView.LayoutManager myrecipeLayoutManager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecipe);

        myrecipeRecyclerView = findViewById(R.id.myrecipe_recyclerview);
        myrecipeRecyclerView.setHasFixedSize(true);
        myrecipeLayoutManager = new LinearLayoutManager(this);
        myrecipeRecyclerView.setLayoutManager(myrecipeLayoutManager);

        ArrayList<MyRecipeData> myrecipeArrayList = new ArrayList<>();
        myrecipeArrayList.add(new MyRecipeData(R.drawable.food, "Test Title", "Test Subtitle"));

        MyRecipeAdapter myrecipeAdapter = new MyRecipeAdapter(myrecipeArrayList);

        myrecipeRecyclerView.setAdapter(myrecipeAdapter);
    }
}
