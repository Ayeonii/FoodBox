package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideItemAdapter extends RecyclerView.Adapter<InsideItemAdapter.ItemViewHolder> {

    ArrayList<DCItem> mItems;

    public InsideItemAdapter(ArrayList<DCItem> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigeratorinside_item_recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String dueDate = mItems.get(position).getStrDueDate();
        String count = Double.toString(mItems.get(position).getCount());
        holder.txtDueDate.setText(dueDate);
        holder.txtCount.setText(count);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDueDate, txtCount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtDueDate = (TextView) itemView.findViewById(R.id.txt_dueDate);
            txtCount = (TextView) itemView.findViewById(R.id.txt_count);
        }
    }
}


