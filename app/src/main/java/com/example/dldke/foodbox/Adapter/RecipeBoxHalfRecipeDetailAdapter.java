package com.example.dldke.foodbox.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;


public class RecipeBoxHalfRecipeDetailAdapter extends RecyclerView.Adapter<RecipeBoxHalfRecipeDetailAdapter.ViewHolder> {

    List<RecipeDO.Ingredient> items;
    List<HalfRecipeRecipeItem> recipeItems = new ArrayList<>();

    public RecipeBoxHalfRecipeDetailAdapter(List<RecipeDO.Ingredient> ingredientdata){
        this.items = ingredientdata;
    }

     public class ViewHolder extends RecyclerView.ViewHolder{

         public ImageView  ingredientImage;
         public TextView ingredientName, ingredientCount;
        ViewHolder(View view){
            super(view);
            ingredientImage = (ImageView)view.findViewById(R.id.recipe_ingredient_detail_img);
            ingredientName = (TextView)view.findViewById(R.id.recipe_ingredient_detail_name);
            ingredientCount = (TextView)view.findViewById(R.id.recipe_ingredient_detail_count);
        }

     }

     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
         View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_item, parent, false);
         AddIngredient(items);
         return new ViewHolder(v);
     }

     public void onBindViewHolder(ViewHolder holder, int position){
            holder.ingredientImage.setImageURI(recipeItems.get(position).getImage());
            holder.ingredientName.setText(recipeItems.get(position).getName());
            Double count = recipeItems.get(position).getCount();
            String strCount = Double.toString(count);
            holder.ingredientCount.setText(strCount);
     }

     public int getItemCount(){
        return (null!= items ? items.size():0);
     }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList){

        for(int i = 0; i<ingredientList.size(); i++){
            String name = ingredientList.get(i).getIngredientName();
            Double count = ingredientList.get(i).getIngredientCount();
            String foodImg = "file:///storage/emulated/0/Download/"+ingredientList.get(i).getIngredientName()+".jpg";
            recipeItems.add(new HalfRecipeRecipeItem(name, count, Uri.parse(foodImg)));
            Log.e("들어간다들어간다", ""+recipeItems.get(i).getName()+""+recipeItems.get(i).getCount());
        }

    }
}
