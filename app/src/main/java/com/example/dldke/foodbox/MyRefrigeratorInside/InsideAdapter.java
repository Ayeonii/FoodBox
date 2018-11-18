package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.HalfRecipeIngreItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideAdapter extends RecyclerView.Adapter<InsideAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeIngreItem> mItems;

    public InsideAdapter(ArrayList<HalfRecipeIngreItem> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_ingre_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String foodName = mItems.get(position).getName();
        holder.mNameTv.setText(foodName);
        String foodUri = "file:///storage/emulated/0/Download/"+foodName+".jpg";
        holder.food_Img.setImageURI(Uri.parse(foodUri));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private ImageView food_Img;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
            food_Img = (ImageView) itemView.findViewById(R.id.img_food);
        }
    }
}

