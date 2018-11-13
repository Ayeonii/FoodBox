package com.example.dldke.foodbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.RecipeBoxData;

import java.util.ArrayList;

public class RecipeBoxAdapter extends RecyclerView.Adapter<RecipeBoxAdapter.ViewHolder> {
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView image;
        public ViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.title);
            image = (ImageView)view.findViewById(R.id.picture);
        }
    }

    public RecipeBoxAdapter(ArrayList<RecipeBoxData> recipedata){
        this.recipedata = recipedata;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipebox_list_item, parent, false);
        return  new ViewHolder(view);
    }

    public void onBindViewHolder(RecipeBoxAdapter.ViewHolder holder,int position){
        holder.name.setText(recipedata.get(position).getName());
        holder.image.setImageResource(recipedata.get(position).getImage());
    }

    public int getItemCount(){
        return recipedata.size();
    }
}
