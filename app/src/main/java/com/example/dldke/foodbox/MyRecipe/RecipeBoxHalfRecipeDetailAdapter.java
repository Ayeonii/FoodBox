package com.example.dldke.foodbox.MyRecipe;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecipeBoxHalfRecipeDetailAdapter extends RecyclerView.Adapter<RecipeBoxHalfRecipeDetailAdapter.ViewHolder> {

    List<RecipeDO.Ingredient> items;
    List<HalfRecipeRecipeItem> recipeItems = new ArrayList<>();

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private Double foodCount[];

    public RecipeBoxHalfRecipeDetailAdapter(List<RecipeDO.Ingredient> ingredientdata) {
        this.items = ingredientdata;
        AddIngredient(items);
        scanRefrigeratorAndRecipe();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ingredientImage;
        public TextView ingredientName, ingredientCount;

        ViewHolder(View view) {
            super(view);
            ingredientImage = (ImageView) view.findViewById(R.id.recipe_ingredient_detail_img);
            ingredientName = (TextView) view.findViewById(R.id.recipe_ingredient_detail_name);
            ingredientCount = (TextView) view.findViewById(R.id.recipe_ingredient_detail_count);
        }

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_halfrecipe_detail_item, parent, false);
//        AddIngredient(items);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        String foodName = recipeItems.get(position).getName();
        String foodImgUri;
        File file = new File("/storage/emulated/0/Download/" + foodName + ".jpg");

        if (!file.exists()) {
            foodImgUri = "file:///storage/emulated/0/Download/default.jpg";
        } else {
            foodImgUri = "file:///storage/emulated/0/Download/" + foodName + ".jpg";
        }

        holder.ingredientImage.setImageURI(Uri.parse(foodImgUri));
        holder.ingredientName.setText(foodName);

        double count = recipeItems.get(position).getCount();

        if ((count % 1) > 0) {
            holder.ingredientCount.setText(count + "개");
        } else {
            holder.ingredientCount.setText((int) count + "개");
        }

        Log.d("test", "foodCount[position] = " + foodCount[position] + ", count=" + count);
        if (foodCount[position] < count)
            holder.ingredientCount.setTextColor(Color.RED);
        else
            holder.ingredientCount.setTextColor(Color.BLACK);


    }

    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList) {
        Log.d("test", "AddIngredient 들어옴");

        for (int i = 0; i < ingredientList.size(); i++) {
            String name = ingredientList.get(i).getIngredientName();
            Double count = ingredientList.get(i).getIngredientCount();
            String foodImg = "file:///storage/emulated/0/Download/" + ingredientList.get(i).getIngredientName() + ".jpg";
            recipeItems.add(new HalfRecipeRecipeItem(name, count, Uri.parse(foodImg)));
            //Log.e("들어간다들어간다", ""+recipeItems.get(i).getName()+""+recipeItems.get(i).getCount());

        }

    }

    public void scanRefrigeratorAndRecipe() {
        Log.d("test", "scanRefrigeratorAndRecipe 들어옴");
        refrigeratorItem = Mapper.scanRefri();

        foodCount = new Double[recipeItems.size()];

        Log.d("test", "foodCount.length = " + foodCount.length);
        Log.d("test", "recipeItems.size() = " + recipeItems.size());
        Log.d("test", "refrigeratorItem.size() = " + refrigeratorItem.size());

        for (int i = 0; i < recipeItems.size(); i++) {
            foodCount[i] = 0.0;
            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(recipeItems.get(i).getName())) {
                    // 냉장고에 재료가 있으면 -> 가진 개수를 넣는다.(없으면 0.0으로 미리 되어있음)
                    foodCount[i] = recipeItems.get(i).getCount();
                }
            }
        }

        int cnt = 0;
        for (int i = 0; i < foodCount.length; i++) {
            Log.d("test", "foodCount[i] ==" + foodCount[i]);
            if (foodCount[i] == 0.0)
                cnt++;
        }

        RecipeBoxHalfRecipeDetailActivity activity = new RecipeBoxHalfRecipeDetailActivity();
        String recipeId = activity.getRecipeId();
        Log.d("test", recipeId);
        Log.d("test", "cnt = " + cnt);

        if (cnt != 0)
            Mapper.updateIngInfo(1, recipeId);
        else
            Mapper.updateIngInfo(0, recipeId);

    }
}
