package com.example.dldke.foodbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.MyRecipeData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyRecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView recipePic;
        TextView recipeName;
        TextView recipeSubname;

        MyRecipeViewHolder(View view){
            super(view);
            recipePic = view.findViewById(R.id.myrecipe_img);
            recipeName = view.findViewById(R.id.myrecipe_name);
            recipeSubname = view.findViewById(R.id.myrecipe_subname);
        }
    }

    private ArrayList<MyRecipeData> myRecipeDataArrayList;
    public MyRecipeAdapter(ArrayList<MyRecipeData> myRecipeDataArrayList){
        this.myRecipeDataArrayList = myRecipeDataArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrecipe_recycler_view_row, parent, false);
        return new MyRecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        MyRecipeViewHolder myRecipeViewHolder = (MyRecipeViewHolder) holder;

        myRecipeViewHolder.recipePic.setImageResource(myRecipeDataArrayList.get(position).myrecipe_img);
        myRecipeViewHolder.recipeName.setText(myRecipeDataArrayList.get(position).myrecipe_name);
        myRecipeViewHolder.recipeSubname.setText(myRecipeDataArrayList.get(position).myrecipe_subname);
    }

    @Override
    public int getItemCount(){
        return myRecipeDataArrayList.size();
    }
}
