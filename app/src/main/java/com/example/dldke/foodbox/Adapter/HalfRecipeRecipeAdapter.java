package com.example.dldke.foodbox.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipeIngreItem;
import com.example.dldke.foodbox.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeRecipeAdapter extends RecyclerView.Adapter<HalfRecipeRecipeAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeRecipeItem> mItems;

    public HalfRecipeRecipeAdapter(ArrayList<HalfRecipeRecipeItem> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_recipe_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mName.setText(mItems.get(position).getName());
        int iCount = mItems.get(position).getCount();
        String strCount = Integer.toString(iCount);

        holder.mCount.setText(strCount);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mCount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mCount = (TextView) itemView.findViewById(R.id.txt_count);
        }
    }
}


