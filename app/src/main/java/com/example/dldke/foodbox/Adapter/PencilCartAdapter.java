package com.example.dldke.foodbox.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.CartCalendarDialog;
import com.example.dldke.foodbox.PencilCartItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;


/*
 * 직접입력 장바구니 팝업 어댑터
 */
public class PencilCartAdapter extends RecyclerView.Adapter<PencilCartAdapter.ItemViewHolder> {
    private Context context;
    ArrayList<PencilCartItem> mItems;

    public PencilCartAdapter(ArrayList<PencilCartItem> cartItems){
        mItems = cartItems;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_cart_list,parent,false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder( final ItemViewHolder holder,   final int position) {
        holder.food_name.setText(mItems.get(position).getFoodName());
        holder.food_img.setImageURI(mItems.get(position).getFoodImg());
        holder.food_count.setText((int)mItems.get(position).getFoodCount()+"개");
        holder.food_date.setText("D-"+mItems.get(position).getFoodDate());
        if(mItems.get(position).getFoodName().length()>6) {
            holder.food_name.setTextSize(12);
        }
        //장바구니 목록 클릭시 커스텀 다이얼로그 팝업
        holder.food_date.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dialog","Calendar");
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                CartCalendarDialog customDialog = new CartCalendarDialog(context);
                customDialog.callFunction(holder.food_date);
            }
        });
    }

    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private TextView food_count;
        private TextView food_date;
        private ImageView food_img;
        private ConstraintLayout cart_Layout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.foodText);
            food_img = (ImageView) itemView.findViewById(R.id.foodImg);
            food_count = (TextView) itemView.findViewById(R.id.foodCount);
            food_date = (TextView) itemView.findViewById(R.id.dueDate);
            cart_Layout = (ConstraintLayout)itemView.findViewById(R.id.cart_ingredient);
        }
    }
}
