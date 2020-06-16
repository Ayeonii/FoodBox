package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.Activity.MainActivity.getPinpointManager;


public class RecipeBoxHalfRecipeDetailAdapter extends RecyclerView.Adapter<RecipeBoxHalfRecipeDetailAdapter.ViewHolder> {

    List<RecipeDO.Ingredient> items;
    List<HalfRecipeRecipeItem> recipeItems = new ArrayList<>();

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private Double refriCount[];
    private String recipeId;
    private int cnt;
    private int ing;
    Context context;
    private String foodName;
    private String TAG = "RecipeBoxHalfRecipeDetailAdapter";

    public RecipeBoxHalfRecipeDetailAdapter(Context context, List<RecipeDO.Ingredient> ingredientdata) {
        this.items = ingredientdata;
        this.context = context;
        RecipeBoxHalfRecipeDetailActivity activity = new RecipeBoxHalfRecipeDetailActivity();
        recipeId = activity.getRecipeId();
        AddIngredient(items);
        ing = Mapper.searchRecipe(recipeId).getIng();

        if (ing == 0 || ing == 1) {
            scanRefrigeratorAndRecipe();
        }
    }

    public int getCnt() {
        return cnt;
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
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        foodName = recipeItems.get(position).getName();
        String foodImgUri;
        File file = new File(context.getFilesDir() + foodName + ".jpg");

        if (!file.exists()) {
            foodImgUri = "file://"+context.getFilesDir()+"default.jpg";
        } else {
            foodImgUri = "file://"+context.getFilesDir()+foodName+".jpg";
        }

        holder.ingredientImage.setImageURI(Uri.parse(foodImgUri));
        holder.ingredientName.setText(foodName);

        double count = recipeItems.get(position).getCount();

        if ((count % 1) > 0) {
            holder.ingredientCount.setText(count + "개");
        } else {
            holder.ingredientCount.setText((int) count + "개");
        }

        if (ing == 0 || ing == 1) {
            if (refriCount[position] < count) {
                holder.ingredientCount.setTextColor(Color.RED);
                MemoUpdate(refriCount[position], count);
            } else
                holder.ingredientCount.setTextColor(Color.BLACK);
        } else if (ing == 2) {
            holder.ingredientCount.setTextColor(Color.GRAY);
        }
    }

    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList) {

        for (int i = 0; i < ingredientList.size(); i++) {
            String name = ingredientList.get(i).getIngredientName();
            Double count = ingredientList.get(i).getIngredientCount();
            String foodImg = "file:///storage/emulated/0/Download/" + ingredientList.get(i).getIngredientName() + ".jpg";
            recipeItems.add(new HalfRecipeRecipeItem(name, count, Uri.parse(foodImg)));
        }

    }

    public void scanRefrigeratorAndRecipe() {

        refrigeratorItem = Mapper.scanRefri();
        refriCount = new Double[recipeItems.size()];

        for (int i = 0; i < recipeItems.size(); i++) {
            refriCount[i] = 0.0;
            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(recipeItems.get(i).getName())) {
                    // 냉장고에 재료가 있으면 -> 가진 개수를 넣는다.(없으면 0.0으로 미리 되어있음)
                    refriCount[i] = refrigeratorItem.get(j).getCount();
                }
            }
        }

        cnt = 0;
        for (int i = 0; i < refriCount.length; i++) {
            if (refriCount[i] == 0.0 || refriCount[i] < recipeItems.get(i).getCount())
                cnt++;
        }

        if (cnt != 0)
            Mapper.updateIngInfo(1, recipeId);
        else
            Mapper.updateIngInfo(0, recipeId);

        PinpointManager tmp = getPinpointManager(context);
        try {
            Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());
        }catch (NullPointerException e){
            Log.e(TAG, "레시피함 비었음");
        }
    }

    private void MemoUpdate(Double refriCount, Double recipeCount) {
        List<RecipeDO.Ingredient> tobuyList = new ArrayList<>();
        List<RefrigeratorDO.Item> updateItem = new ArrayList<>();
        List<RecipeDO.Ingredient> appendItem = new ArrayList<>();

        try {
            tobuyList = Mapper.scanToBuyMemo();

            int cnt=0;
            for (int i=0; i<tobuyList.size(); i++)
                if (tobuyList.get(i).getIngredientName().equals(foodName))
                    cnt++;

            if (cnt==0) {
                RecipeDO.Ingredient setlist = new RecipeDO.Ingredient();

                setlist.setIngredientName(foodName);
                setlist.setIngredientCount(recipeCount - refriCount);

                appendItem.add(setlist);

                Mapper.appendToBuyMemo(appendItem);
            }

        } catch (NullPointerException e) {

        }
    }
}
