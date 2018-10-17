package com.example.dldke.foodbox.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;


/*
 * 직접입력 RecyclerAdapter
 */
public class PencilRecyclerAdapter extends RecyclerView.Adapter<PencilRecyclerAdapter.ItemViewHolder> {


        ArrayList<PencilItem> mItems;
        public PencilRecyclerAdapter(ArrayList<PencilItem> items){
            mItems = items;
        }

        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_ingredient_list,parent,false);
            return new ItemViewHolder(view);
        }


        // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            holder.food_name.setText(mItems.get(position).getFoodName());
            holder.food_img.setImageDrawable(mItems.get(position).getFoodImg());
            if(mItems.get(position).getFoodName().length()>6)
            {
                holder.food_name.setTextSize(12);
            }
        }

        // 데이터 셋의 크기를 리턴해줍니다.
        @Override
        public int getItemCount() {
            return mItems.size();
        }

        // 커스텀 뷰홀더
        // item layout 에 존재하는 위젯들을 바인딩합니다.
        class ItemViewHolder extends RecyclerView.ViewHolder{
            private TextView food_name;
            private ImageView food_img;
            public ItemViewHolder(View itemView) {
                super(itemView);
                food_name = (TextView) itemView.findViewById(R.id.foodText);
                food_img =  (ImageView) itemView.findViewById(R.id.foodImg);
            }
        }


}
