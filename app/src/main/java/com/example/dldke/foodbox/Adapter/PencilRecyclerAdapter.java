package com.example.dldke.foodbox.Adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


/*
 * 직접입력 RecyclerAdapter
 */
public class PencilRecyclerAdapter extends RecyclerView.Adapter<PencilRecyclerAdapter.ItemViewHolder> {




        /****임시임 나중에 clickFoodString 삭제****/
        public static ArrayList<String> clickFoodString = new ArrayList<>();
        public static ArrayList<PencilItem> clickFood = new ArrayList<>();


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
        public void onBindViewHolder( ItemViewHolder holder,   final int position) {
            holder.food_name.setText(mItems.get(position).getFoodName());
            holder.food_img.setImageDrawable(mItems.get(position).getFoodImg());

            if(mItems.get(position).getFoodName().length()>6)
            {
                holder.food_name.setTextSize(12);
            }

            holder.food_img.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("clicked",""+mItems.get(position).getFoodName());
                    clickFood.add(new PencilItem(mItems.get(position).getFoodName(),mItems.get(position).getFoodImg()));
                    clickFoodString.add(mItems.get(position).getFoodName()); //이거 나중에 이미지 디비에서 불러올때 삭제하고
                                                                            // 위에 clickFood 사용하기
                }
            });


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
