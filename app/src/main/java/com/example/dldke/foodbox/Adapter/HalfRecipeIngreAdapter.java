package com.example.dldke.foodbox.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipeIngreItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeIngreAdapter extends RecyclerView.Adapter<HalfRecipeIngreAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeIngreItem> mItems;
    Boolean[] checkIngre = new Boolean[50];

    public HalfRecipeIngreAdapter(ArrayList<HalfRecipeIngreItem> mItems) {
        this.mItems = mItems;
    }

    public HalfRecipeIngreAdapter(ArrayList<HalfRecipeIngreItem> mItems, int arraySize, Boolean[] check) {
        Log.d("test", "HalfRecipeIngreAdapter");
        this.mItems = mItems;
        //int n = getItemCount();
        Log.d("test", "adapter constructor : " + arraySize);
//        for (int i=0; i<mItems.size(); i++) {
//            this.checkIngre[i] = check[i];
//        }
        System.arraycopy(check, 0, this.checkIngre, 0, arraySize);
        for (int i=0; i<arraySize; i++) {
            Log.d("test", i + " : adapter constructor: " + checkIngre[i]);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("test", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_ingre_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Log.d("test", "onBindViewHolder");
        holder.mNameTv.setText(mItems.get(position).getName());

        Log.d("test", position + " : adapter onBindViewHolder: " + checkIngre[position]);

        if (checkIngre[position])
            holder.ivCheck.setVisibility(View.VISIBLE);
        else
            holder.ivCheck.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private ImageView ivCheck;

        public ItemViewHolder(View itemView) {
            super(itemView);
            Log.d("test", "ItemViewHolder");
            mNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
            ivCheck = (ImageView) itemView.findViewById(R.id.img_check);
        }
    }
}


