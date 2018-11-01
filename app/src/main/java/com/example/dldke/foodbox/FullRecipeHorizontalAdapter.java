package com.example.dldke.foodbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.FullRecipeHorizontalData;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class FullRecipeHorizontalAdapter extends RecyclerView.Adapter<FullRecipeHorizontalAdapter.HorizontalViewHolder> {

    private ArrayList<FullRecipeHorizontalData> HorizontalDatas;

    public void setData(ArrayList<FullRecipeHorizontalData> list){
        HorizontalDatas = list;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView description;

        public HorizontalViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
            description = (TextView) itemView.findViewById(R.id.horizon_description);

        }
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fullrecipe_ingredient_list, parent, false);

        HorizontalViewHolder holder = new HorizontalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        FullRecipeHorizontalData data = HorizontalDatas.get(position);

        holder.description.setText(data.getText());
        holder.icon.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return HorizontalDatas.size();
    }
}

