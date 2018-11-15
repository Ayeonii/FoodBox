package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dldke.foodbox.Activity.RecipeDetailActivity;
import com.example.dldke.foodbox.Adapter.RecipeBoxAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.RecipeBoxData;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeBoxFragment extends Fragment {

    public HalfRecipeBoxFragment(){}

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private ArrayList<RecipeBoxData> data = new ArrayList<>();

    private String TAG = "HalfRecipeBox";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_half_recipe_box, container, false);

        recyclerview = (RecyclerView)view.findViewById(R.id.recycler_view2);
        recyclerview.setHasFixedSize(true);
        adapter = new RecipeBoxAdapter(data);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);

        return view;
    }

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void prepareData(){

        List<String> myrecipe = Mapper.searchMyCommunity().getMyRecipes();
        for(int i =0 ; i<myrecipe.size(); i++){
           try{

                String foodname = Mapper.searchRecipe(myrecipe.get(i)).getDetail().getFoodName();

            }catch(NullPointerException e){

               Log.d(TAG,"음식이름이 없습니다." );
               Log.e(TAG, ""+myrecipe.get(i));
               data.add(new RecipeBoxData(myrecipe.get(i), R.drawable.strawberry));
            }
        }
    }


}
