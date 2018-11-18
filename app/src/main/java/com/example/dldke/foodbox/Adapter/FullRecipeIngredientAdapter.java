package com.example.dldke.foodbox.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipeIngredientData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeIngredientAdapter extends RecyclerView.Adapter<FullRecipeIngredientAdapter.ViewHolder> {

    private String TAG = "FullRecipeIngredientAdapter";
    private Context context;
    String foodImg;
    private List<RecipeDO.Ingredient> ingredients;
    //재료 이름과 이미지 받아오기 위한 저장소
    private List<FullRecipeIngredientData> IngredientData = new ArrayList<>();


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
        holder.ingredientName.setText(IngredientData.get(position).getIngredientName());
        holder.ingredientImage.setImageURI(IngredientData.get(position).getIngredientImage());

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList){

        for(int i = 0; i<ingredientList.size(); i++){
            String name = ingredientList.get(i).getIngredientName();
            foodImg = "file:///storage/emulated/0/Download/"+ingredientList.get(i).getIngredientName()+".jpg";
            IngredientData.add(new FullRecipeIngredientData(name,  Uri.parse(foodImg)));
        }
    }
}
