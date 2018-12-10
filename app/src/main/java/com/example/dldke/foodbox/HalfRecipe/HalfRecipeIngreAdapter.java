package com.example.dldke.foodbox.HalfRecipe;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeIngreAdapter extends RecyclerView.Adapter<HalfRecipeIngreAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeIngreItem> mItems;
    private Boolean[] checkIngre;
    String ingreType;

    public HalfRecipeIngreAdapter(ArrayList<HalfRecipeIngreItem> mItems, int arraySize, Boolean[] check, String ingreType) {
        this.mItems = mItems;
        checkIngre = new Boolean[arraySize];
        System.arraycopy(check, 0, this.checkIngre, 0, arraySize);
        this.ingreType = ingreType;
    }

    public Boolean[] getCheckIngre() {
        return checkIngre;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_ingredient_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String foodName = mItems.get(position).getName();
        String foodImgUri;
        if (ingreType.equals("sideDish")) {
            foodImgUri = "file:///storage/emulated/0/Download/default.jpg";
        } else {
            foodImgUri = "file:///storage/emulated/0/Download/"+foodName+".jpg";
        }
        holder.mNameTv.setText(foodName);
        holder.food_Img.setImageURI(Uri.parse(foodImgUri));

        if (!checkIngre[position]) {
            holder.ivCheck.setVisibility(View.GONE);
        } else {
            holder.ivCheck.setVisibility(View.VISIBLE);
        }

        holder.food_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "food clicked!");
                holder.ivCheck.setVisibility(View.VISIBLE);
                checkIngre[position] = true;
            }
        });

        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "check clicked!");
                holder.ivCheck.setVisibility(View.GONE);
                checkIngre[position] = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private ImageView ivCheck;
        private ImageView food_Img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
            ivCheck = (ImageView) itemView.findViewById(R.id.img_check);
            food_Img = (ImageView) itemView.findViewById(R.id.img_food);
        }
    }
}

