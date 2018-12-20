package com.example.dldke.foodbox.FullRecipe;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeIngredientAdapter extends RecyclerView.Adapter<FullRecipeIngredientAdapter.ViewHolder> {

    private Context context;
    String foodImg;
    private List<RecipeDO.Ingredient> ingredients;
    private List<FullRecipeIngredientData> IngredientData = new ArrayList<>();


    public FullRecipeIngredientAdapter(Context context,List<RecipeDO.Ingredient> data){
        this.context = context;
        this.ingredients = data;
    }

    //ItemView의 내용을 담고 있음
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ingredientImage;
        public TextView ingredientName, ingredientCount;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ingredientImage = (ImageView) itemView.findViewById(R.id.ingredient_icon);
            this.ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
            this.ingredientCount = (TextView) itemView.findViewById(R.id.ingredient_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fullrecipe_ingredient_list_item, parent, false);
        AddIngredient(ingredients);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredientName.setText(IngredientData.get(position).getIngredientName());
        double count = IngredientData.get(position).getIngredient_count();
        holder.ingredientCount.setText(Double.toString(count)+"개");
        holder.ingredientImage.setImageURI(IngredientData.get(position).getIngredientImage());
    }

    @Override
    public int getItemCount() {
        return (null != ingredients ? ingredients.size() : 0);
    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList){

        for(int i = 0; i<ingredientList.size(); i++){
            String name = ingredientList.get(i).getIngredientName();
            double count = ingredientList.get(i).getIngredientCount();
            foodImg = "file://"+context.getFilesDir()+name+".jpg";
            IngredientData.add(new FullRecipeIngredientData(name,  Uri.parse(foodImg), count));
        }
    }
}
