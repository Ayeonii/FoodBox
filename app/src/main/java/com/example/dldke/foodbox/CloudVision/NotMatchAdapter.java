package com.example.dldke.foodbox.CloudVision;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.R;

import java.util.List;

public class NotMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String > notmatchItems;
    private String TAG="NotMatchAdapter";

    public NotMatchAdapter(List<String> items, Context activity){
        this.notmatchItems = items;
        this.context = activity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notmatch;
        Button delete;
        MyViewHolder(View view){
            super(view);
            notmatch = view.findViewById(R.id.notmatch_txt);
            delete = view.findViewById(R.id.notmatch_delete);
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
        myViewHolder.notmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, notmatchItems.get(position)+" 눌림");
                Intent PopupActivity = new Intent(context, PopupActivity.class);
                context.startActivity(PopupActivity);
            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, notmatchItems.get(position)+" 삭제");
                notmatchItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, notmatchItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notmatchItems.size();
    }
}


