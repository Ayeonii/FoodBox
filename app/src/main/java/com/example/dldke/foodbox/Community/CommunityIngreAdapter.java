package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class CommunityIngreAdapter extends RecyclerView.Adapter<CommunityIngreAdapter.ItemViewHolder> {

    private ArrayList<String> mItems;
    private Context context;

    public CommunityIngreAdapter(ArrayList<String> items , Context context){
        mItems = items;
        this.context = context;
    }

    @Override
    public CommunityIngreAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_list_ingredient,parent,false);
        return new CommunityIngreAdapter.ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final CommunityIngreAdapter.ItemViewHolder holder, final int position) {
        holder.food_name.setText(mItems.get(position));
        String foodImg = "file://"+context.getFilesDir()+mItems.get(position)+".jpg";
        holder.food_img.setImageURI(Uri.parse(foodImg));
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
