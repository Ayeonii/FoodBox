package com.example.dldke.foodbox.CloudVision;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ItemViewHolder> {

    private List<PencilItem> allFoodItems = new ArrayList<>();
    private String TAG="PopupAdapter";

    public PopupAdapter (List<PencilItem> allfoodList){
        this.allFoodItems = allfoodList;
        Log.e(TAG, "AllFoodItems"+allFoodItems);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private ImageView food_img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.vision_foodText);
            food_img = (ImageView) itemView.findViewById(R.id.vision_userImg);
        }
    }

    @Override
    public PopupAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vision_ingredient_item,parent,false);
        return new PopupAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final PopupAdapter.ItemViewHolder holder, final int position) {

        holder.food_name.setText(allFoodItems.get(position).getFoodName());
        holder.food_img.setImageURI(allFoodItems.get(position).getFoodImg());
        if (allFoodItems.get(position).getFoodName().length() > 6) {
            holder.food_name.setTextSize(12);
        }
    }


    @Override
    public int getItemCount() {
            return allFoodItems.size();
    }


}
