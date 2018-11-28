package com.example.dldke.foodbox.MyRecipe;

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

import java.util.ArrayList;
import java.util.List;

public class RecipeBoxFullRecipeDetailAdapter extends RecyclerView.Adapter<RecipeBoxFullRecipeDetailAdapter.ViewHolder> {

    String recipe_id;
    List<RecipeDO.Spec> specList;
    List<RecipeDO.Ingredient> specIngredientList;
    List<RecipeBoxFullRecipeDetailItem> stepList = new ArrayList<>();

    String TAG = "RecipeBoxFullRecipeDetail";

    public RecipeBoxFullRecipeDetailAdapter(String recipeId){
        this.recipe_id = recipeId;
        specList = Mapper.searchRecipe(recipe_id).getDetail().getSpecList();
        Log.e(TAG, ""+recipe_id);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_fullrecipe_detail_card_layout, parent, false);
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
            String result ="";
            specIngredientList = specList.get(i).getSpecIngredient();
            for(int j = 0; j<specIngredientList.size(); j++){
                String specingredientName = specIngredientList.get(j).getIngredientName();
                result = result.concat(specingredientName);
            }
            int number = i+1;
            String descrip = number+". "+result+" 을/를 "+specList.get(i).getSpecMinute()+"분 동안 "+specList.get(i).getSpecMethod()+".\r\n"+"불 세기는 "+specList.get(i).getSpecFire();
            Log.e(TAG, "레시피 설명  : "+descrip);
            stepList.add(new RecipeBoxFullRecipeDetailItem(R.drawable.strawberry, descrip));

            specIngredientList.clear();
        }
    }
}
