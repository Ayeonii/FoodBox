package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideItemAdapter extends RecyclerView.Adapter<InsideItemAdapter.ItemViewHolder> {

    ArrayList<DCItem> mItems;
    String mName;
    Context context;

    public InsideItemAdapter(ArrayList<DCItem> mItems, String mName) {
        this.mItems = mItems;
        this.mName = mName;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigeratorinside_item_recyclerview_item, parent, false);
        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final String dueDate = mItems.get(position).getStrDueDate();
        String count = Double.toString(mItems.get(position).getCount());
        holder.txtDueDate.setText(dueDate);
        holder.txtCount.setText(count);

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", position + "번째를 수정하기");
                DCEditDialog dcEditDialog = new DCEditDialog(context);
                dcEditDialog.show();
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("삭제")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mItems.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mItems.size());
                                //Mapper.deleteFood(mName, dueDate);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDueDate, txtCount;
        private ImageView imgEdit, imgDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtDueDate = (TextView) itemView.findViewById(R.id.txt_dueDate);
            txtCount = (TextView) itemView.findViewById(R.id.txt_count);
            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }
}


