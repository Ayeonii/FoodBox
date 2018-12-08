package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/*
 * 직접입력 RecyclerAdapter
 */
public class RefrigeratorFrozenAdapter extends RecyclerView.Adapter<RefrigeratorFrozenAdapter.ItemViewHolder> {

    private static List<LocalRefrigeratorItem> mItems;
    Context context;

    public RefrigeratorFrozenAdapter(ArrayList<LocalRefrigeratorItem> items , Context context){
        mItems = items;
        this.context = context;
    }
    // 새로운 뷰 홀더 생성
    @Override
    public RefrigeratorFrozenAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_list_ingredient,parent,false);
        return new RefrigeratorFrozenAdapter.ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final RefrigeratorFrozenAdapter.ItemViewHolder holder, final int position) {
        String foodImg = "file:///storage/emulated/0/Download/" + mItems.get(position).getName() + ".jpg";
        holder.food_img.setImageURI(Uri.parse(foodImg));
        holder.food_name.setText(mItems.get(position).getName());
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
        private ImageView food_img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.foodText);
            food_img = (ImageView) itemView.findViewById(R.id.userImg);
        }
    }

}