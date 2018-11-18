package com.example.dldke.foodbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.RecipeBoxFullRecipeDetailItem;

import java.util.List;

public class RecipeBoxFullRecipeDetailAdapter extends RecyclerView.Adapter<RecipeBoxFullRecipeDetailAdapter.ViewHolder> {

    String recipe_id;
    List<RecipeDO.Spec> specList = Mapper.searchRecipe(recipe_id).getDetail().getSpecList();
    List<RecipeBoxFullRecipeDetailItem> stepList;

    public RecipeBoxFullRecipeDetailAdapter(String recipeId){
        this.recipe_id = recipeId;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView stepImage;
        public TextView stepDescrip;

        public ViewHolder(View view){
            super(view);
            stepImage = (ImageView) view.findViewById(R.id.fullrecipe_detail_stepimg);
            stepDescrip = (TextView) view.findViewById(R.id.fullrecipe_detail_stepdescrip);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipebox_fullrecipe_detail_content_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        AddStep(specList);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        //holder.stepImage.setImageURI(stepList.get(position).getStepImage());
        holder.stepImage.setImageResource(stepList.get(position).getStepImage());
        holder.stepDescrip.setText(stepList.get(position).getDescription());
    }

    public int getItemCount(){
        return specList.size();
    }

    public void AddStep(List<RecipeDO.Spec> specList){
        for(int i = 0; i<specList.size(); i++){
            String descrip = "'"+specList.get(i).getSpecIngredient()+"'"+" 을/를 "+specList.get(i).getSpecMethod()+" "+specList.get(i).getSpecMinute()+" 동안 "+"불 세기는"+specList.get(i).getSpecFire();
            stepList.add(new RecipeBoxFullRecipeDetailItem(R.drawable.strawberry, descrip));
        }
    }
}
