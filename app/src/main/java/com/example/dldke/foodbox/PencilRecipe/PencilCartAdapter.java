package com.example.dldke.foodbox.PencilRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;


/*
 * 직접입력 장바구니 팝업 어댑터
 */
public class PencilCartAdapter extends RecyclerView.Adapter<PencilCartAdapter.ItemViewHolder> {
    private PencilRecyclerAdapter pencilAdapter = new PencilRecyclerAdapter();
    private Context context;
    private static ArrayList<PencilCartItem> mItems;
    String TAG ="PencilCartAdapter";

    public PencilCartAdapter(ArrayList<PencilCartItem> cartItems){ this.mItems = cartItems;}
    public PencilCartAdapter(){}

    public ArrayList<PencilCartItem> getCartItems(){return mItems; }


    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_list_cart,parent,false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder( final ItemViewHolder holder,   final int position) {
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dueDate :
                        CartCalendarDialog customDialog = new CartCalendarDialog(context);
                        customDialog.callFunction(holder.food_date, mItems.get(position));
                        break ;
                    case R.id.plus_btn :
                        int plus = (int)mItems.get(position).getFoodCount()+1;
                        holder.food_count.setText(plus+"개");
                        mItems.get(position).setFoodCount(plus);
                        pencilAdapter.setClickCnt(pencilAdapter.getClickCnt()+1);
                        break ;
                    case R.id.minus_btn :
                        int minus = (int)mItems.get(position).getFoodCount()-1;
                        holder.food_count.setText(minus+"개");
                        mItems.get(position).setFoodCount(minus);
                        pencilAdapter.setClickCnt(pencilAdapter.getClickCnt()-1);
                        break ;
                    case R.id.deleteButton :
                        pencilAdapter.setClickCnt(pencilAdapter.getClickCnt()-(int)mItems.get(position).getFoodCount());
                        mItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mItems.size());
                        break;
                    case R.id.frozenCheck:
                        if(!holder.frozenCheck.isChecked()) {
                            mItems.get(position).setIsFrozen(false);
                            Log.e("isFozen: true 이어야함",""+ mItems.get(position).getIsFrozen());
                        }
                        else{
                            mItems.get(position).setIsFrozen(true);
                            Log.e("isFozen: false이어야함",""+ mItems.get(position).getIsFrozen());
                        }
                        break ;
                }
            }
        } ;
        holder.food_name.setText(mItems.get(position).getFoodName());
        holder.food_img.setImageURI(mItems.get(position).getFoodImg());
        holder.food_count.setText((int)mItems.get(position).getFoodCount()+"개");
        holder.food_date.setText("D-"+mItems.get(position).getFoodDueDays());
        if(mItems.get(position).getFoodName().length()>6) {
            holder.food_name.setTextSize(12);
        }
        //장바구니 목록 클릭시 커스텀 다이얼로그 팝업
        holder.food_date.setOnClickListener(onClickListener);
        holder.plus_btn.setOnClickListener(onClickListener);
        holder.minus_btn.setOnClickListener(onClickListener);
        holder.delete_btn.setOnClickListener(onClickListener);
        holder.frozenCheck.setOnClickListener(onClickListener);
        if(mItems.get(position).getIsFrozen()){
            Log.e("ㅇㄴㄻㅇㄹ","들어옴");
            holder.frozenCheck.setChecked(true);
        } else{
            Log.e("ㅇㄴㄻㅇㄹ","else 들어옴"+mItems.get(position).getFoodName());
            holder.frozenCheck.setChecked(false);
        }
//        try{
//            //Log.e("ㅇㄴㄻㅇㄹ","들어옴");
//            holder.frozenCheck.setChecked(true);
//        }catch(NullPointerException e){
//            //Log.e("ㅇㄴㄻㅇㄹ","else 들어옴"+mItems.get(position).getFoodName());
//            holder.frozenCheck.setChecked(false);
//        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name, food_count, food_date;
        private ImageView food_img, plus_btn, minus_btn;
        private Button delete_btn;
        private CheckBox frozenCheck;

        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.foodText);
            food_img = (ImageView) itemView.findViewById(R.id.userImg);
            food_count = (TextView) itemView.findViewById(R.id.foodCount);
            food_date = (TextView) itemView.findViewById(R.id.dueDate);
            plus_btn = (ImageView) itemView.findViewById(R.id.plus_btn);
            minus_btn = (ImageView) itemView.findViewById(R.id.minus_btn);
            delete_btn = (Button) itemView.findViewById(R.id.deleteButton);
            frozenCheck = (CheckBox) itemView.findViewById(R.id.frozenCheck);
        }
    }
}
