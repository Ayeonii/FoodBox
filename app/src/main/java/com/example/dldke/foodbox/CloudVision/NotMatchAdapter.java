package com.example.dldke.foodbox.CloudVision;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.List;

public class NotMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String > notmatchItems;

    public NotMatchAdapter(List<String> items){
        this.notmatchItems = items;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notmatch;
        MyViewHolder(View view){
            super(view);
            notmatch = view.findViewById(R.id.notmatch_txt);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vision_notmatch_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.notmatch.setText(notmatchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return notmatchItems.size();
    }
}


