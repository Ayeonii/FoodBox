package com.example.dldke.foodbox.HalfRecipe;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;


public class HalfRecipeDueDateAdapter extends RecyclerView.Adapter<HalfRecipeDueDateAdapter.ItemViewHolder> {

    private ArrayList<HalfRecipeDueDateItem> mItems;

    public HalfRecipeDueDateAdapter(ArrayList<HalfRecipeDueDateItem> mItems) {
        this.mItems = mItems;
        for(int i=0; i<mItems.size(); i++) {
            mItems.get(i).setWhich(0);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_duedate_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        String foodName = mItems.get(position).getName();
        String foodImgUri = "file:///storage/emulated/0/Download/"+foodName+".jpg";
        holder.txtName.setText(foodName);
        holder.imgFood.setImageURI(Uri.parse(foodImgUri));

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getId() == R.id.radiogroup) {
                    switch (i) {
                        case R.id.radio1:
                            Log.d("test", position + " : radio1");
                            mItems.get(position).setWhich(0);
                            break;
                        case R.id.radio2:
                            Log.d("test", position + " : radio2");
                            mItems.get(position).setWhich(1);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgFood;
        private RadioGroup radioGroup;
        private RadioButton radio1, radio2;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            imgFood = (ImageView) itemView.findViewById(R.id.img_food);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radiogroup);
            radio1 = (RadioButton) itemView.findViewById(R.id.radio1);
            radio2 = (RadioButton) itemView.findViewById(R.id.radio2);
        }
    }
}


