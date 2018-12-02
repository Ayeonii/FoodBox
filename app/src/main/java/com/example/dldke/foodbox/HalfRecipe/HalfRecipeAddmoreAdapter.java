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

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.Arrays;

public class HalfRecipeAddmoreAdapter extends RecyclerView.Adapter<HalfRecipeAddmoreAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeIngreItem> mItems;
    private static Boolean isCheck[] = new Boolean[230];

    public  Boolean[] getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean[] isCheck) {
        HalfRecipeAddmoreAdapter.isCheck = isCheck;
    }

    //    public HalfRecipeAddmoreAdapter(ArrayList<HalfRecipeIngreItem> mItems, int arraySize, Boolean[] check) {
//        this.mItems = mItems;
//        System.arraycopy(check, 0, this.isCheck, 0, arraySize);
//    }

    public HalfRecipeAddmoreAdapter(ArrayList<HalfRecipeIngreItem> mItems){
        this.mItems = mItems;
        Arrays.fill(isCheck, false);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_addmore_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        String foodName = mItems.get(position).getName();
        String foodImgUri = "file:///storage/emulated/0/Download/"+foodName+".jpg";

        holder.mNameTv.setText(foodName);
        holder.food_Img.setImageURI(Uri.parse(foodImgUri));

        holder.food_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "food clicked!");
                if(!isCheck[position]) {
                    holder.ivCheck.setVisibility(View.VISIBLE);
                    isCheck[position] = true;
                }
            }
        });

        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "check clicked!");
                if(isCheck[position]){
                    holder.ivCheck.setVisibility(View.GONE);
                    isCheck[position] = false;
                }
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

