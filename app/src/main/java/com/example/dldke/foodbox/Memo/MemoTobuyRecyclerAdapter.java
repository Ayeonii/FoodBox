package com.example.dldke.foodbox.Memo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class MemoTobuyRecyclerAdapter extends RecyclerView.Adapter<MemoTobuyRecyclerAdapter.ItemViewHolder> {
    private ArrayList<MemoToBuyItem> mItems;
    private Context context;

    public MemoTobuyRecyclerAdapter(ArrayList<MemoToBuyItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    @Override
    public MemoTobuyRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_tobuy_list,parent,false);
        return new MemoTobuyRecyclerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemoTobuyRecyclerAdapter.ItemViewHolder holder, final int position) {
        holder.tobuyImg.setImageURI(mItems.get(position).getTobuyImg());
        holder.tobuyName.setText(mItems.get(position).getTobuyName());
        holder.tobuyCount.setText(mItems.get(position).getTobuyCount() + "개");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView tobuyImg;
        private TextView tobuyName;
        private TextView tobuyCount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tobuyImg = (ImageView)itemView.findViewById(R.id.tobuyImg);
            tobuyName = (TextView) itemView.findViewById(R.id.tobuyName);
            tobuyCount = (TextView) itemView.findViewById(R.id.tobuyCount);
        }
    }
}
