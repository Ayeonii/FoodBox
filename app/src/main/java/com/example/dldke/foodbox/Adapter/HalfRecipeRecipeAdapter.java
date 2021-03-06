package com.example.dldke.foodbox.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipeIngreItem;
import com.example.dldke.foodbox.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeRecipeAdapter extends RecyclerView.Adapter<HalfRecipeRecipeAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeRecipeItem> mItems;
    Double editCount;
    String strEditCount;

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
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final Double iCount = mItems.get(position).getCount();
        String strCount = Double.toString(iCount);

        editCount = 1.0;
        strEditCount = Double.toString(editCount);

        holder.txtName.setText(mItems.get(position).getName());
        holder.txtCount.setText(strCount);
        holder.txtCountEdit.setText(strEditCount);

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editCount < iCount) {
                    editCount += 0.5;
                    strEditCount = Double.toString(editCount);
                    holder.txtCountEdit.setText(strEditCount);
                    Log.d("test", "adapter plus : position : " + position + " : " + strEditCount);
                }
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editCount > 0) {
                    editCount -= 0.5;
                    strEditCount = Double.toString(editCount);
                    holder.txtCountEdit.setText(strEditCount);
                    Log.d("test", "adapter minus : position : " + position + " : " + strEditCount);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtCount, txtCountEdit;
        private Button btnPlus, btnMinus;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtCount = (TextView) itemView.findViewById(R.id.txt_count);
            txtCountEdit = (TextView) itemView.findViewById(R.id.txt_count_edit);
            btnPlus = (Button) itemView.findViewById(R.id.btn_plus);
            btnMinus = (Button) itemView.findViewById(R.id.btn_minus);
        }
    }
}


