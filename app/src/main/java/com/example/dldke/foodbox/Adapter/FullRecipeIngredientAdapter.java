package com.example.dldke.foodbox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RecipeDetailActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipeIngredientData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeIngredientAdapter extends RecyclerView.Adapter<FullRecipeIngredientAdapter.ViewHolder> {

    private String TAG = "FullRecipeIngredientAdapter";
    private Context context;
    private static List<RecipeDO.Ingredient> ingredients;
    //재료 이름과 이미지 받아오기 위한 저장소
    private static List<FullRecipeIngredientData> IngredientData;

    //해당레시피 ID와 등록된 재료 받아오기
    //private RecipeDetailActivity recipeDetailActivity = new RecipeDetailActivity();
    //private String recipeId = recipeDetailActivity.getRecipeId();
    //private List<RecipeDO.Ingredient> ingredientList = Mapper.searchRecipe(recipeId).getIngredient();



    public FullRecipeIngredientAdapter(Context context,List<RecipeDO.Ingredient> data){
        this.context = context;
        this.ingredients = data;
    }


    //ItemView의 내용을 담고 있음
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ingredientImage;
        public TextView ingredientName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ingredientImage = (ImageView) itemView.findViewById(R.id.ingredient_icon);
            this.ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fullrecipe_ingredient_list, parent, false);
        AddIngredient(ingredients);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //FullRecipeIngredientData data = IngredientData.get(position);
        holder.ingredientName.setText(IngredientData.get(position).getIngredientName());
        holder.ingredientImage.setImageResource(IngredientData.get(position).getIngredientImage());

    }

    @Override
    public int getItemCount() {
        //Log.e(TAG, ""+recipeId);
        //AddIngredient(ingredientList);
       // return (null != ingredientList ? ingredientList.size() : 0);
        AddIngredient(ingredients);
        for(int i = 0;i<IngredientData.size();i++){
            Log.e("여기 찾아라라라", IngredientData.get(i).getIngredientName());
        }
        return (null != IngredientData ? IngredientData.size() : 0);
    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList){
        Log.e("여기여기여기여기",""+IngredientData.size());
        for(int i = 0; i<ingredientList.size(); i++){
            String name = ingredientList.get(i).getIngredientName();
            Log.e(TAG, "어떤 재료냐아아"+ingredientList.get(i).getIngredientName());

            IngredientData.add(new FullRecipeIngredientData(name, R.drawable.strawberry));
        }
    }
}
