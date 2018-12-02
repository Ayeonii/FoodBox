package com.example.dldke.foodbox.HalfRecipe;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeIngAdapter extends RecyclerView.Adapter<HalfRecipeIngAdapter.ItemViewHolder> {

    private ArrayList<HalfRecipeRecipeItem> mItems;

    public HalfRecipeIngAdapter(ArrayList<HalfRecipeRecipeItem> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_ing_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        //재료의 이름 세팅
        holder.txtName.setText(mItems.get(position).getName());
        //재료 이미지 세팅
        holder.imgFood.setImageURI(mItems.get(position).getImage());
        //재료 필요 개수 세팅
        final Double iCount = mItems.get(position).getNeedCount();
        String strCount = Double.toString(iCount);
        holder.txtCountNeed.setText(strCount);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtCountNeed;
        private ImageView imgFood;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imgFood = (ImageView) itemView.findViewById(R.id.img_food);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtCountNeed = (TextView) itemView.findViewById(R.id.txt_count_need);
        }
    }
}


