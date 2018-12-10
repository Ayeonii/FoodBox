package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class InsideItemAdapter extends RecyclerView.Adapter<InsideItemAdapter.ItemViewHolder> {

    private static ArrayList<DCItem> dcArray = new ArrayList<>();
    private String foodName, foodDueDate, foodCount;
    private Context context;
    private Double foodCount_double;

    public InsideItemAdapter(Context context, ArrayList<DCItem> dcArray, String foodName) {
        this.context = context;
        this.dcArray = dcArray;
        this.foodName = foodName;
    }

    public ArrayList<DCItem> getDcArray() {
        return dcArray;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigeratorinside_item_recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        //유통기한이랑 개수받아오는 부분
        foodDueDate = dcArray.get(position).getStrDueDate();
        foodCount_double = dcArray.get(position).getCount();
        foodCount = Double.toString(foodCount_double);
        holder.txtDueDate.setText(foodDueDate);
        holder.txtCount.setText(foodCount);

        //수정...이미지 클릭하는 부분 -> 유통기한이랑 개수 수정가능한 다이얼로그 뜸
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", position + "번째를 수정할거임 클릭됨");
                DCEditDialog dcEditDialog = new DCEditDialog(context, foodDueDate, dcArray.get(position).getCount());
                dcEditDialog.setDialogListener(new InsideDialogListener() {
                    @Override
                    public void onPositiveClicked(Double count, String dueDate) {
                        Mapper.updateCount(foodName, foodDueDate, count);       //변경 전 유통기한으로 개수 바꾸기
                        Mapper.updateDueDate(foodName, foodDueDate, dueDate);   //유통기한 변경
                        foodDueDate = dueDate;
                        foodCount = Double.toString(count);
                        holder.txtDueDate.setText(foodDueDate);
                        holder.txtCount.setText(foodCount);
                        dcArray.get(position).setStrDueDate(dueDate);
                        dcArray.get(position).setCount(count);

                        MemoUpdate(count);
                    }

                    @Override
                    public void onOkClicked(ArrayList<DCItem> dcItems) { }
                });
                dcEditDialog.show();
            }
        });

        //휴지통 이미지 클릭하는 부분 -> 해당 이름과 유통기한에 해당하는 재료가 삭제됨
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test", position + "번째를 삭제할거임 클릭됨");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("삭제")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dcArray.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, dcArray.size());
                                Mapper.deleteFood(foodName, foodDueDate);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    private void MemoUpdate(Double count) {
        // memo upadate
        List<RecipeDO.Ingredient> tobuyList = new ArrayList<>();
        List<RefrigeratorDO.Item> updateItem = new ArrayList<>();
        List<RecipeDO.Ingredient> appendItem = new ArrayList<>();
        try {
            tobuyList = Mapper.scanToBuyMemo();
            for (int i=0; i<tobuyList.size(); i++) {
                if (tobuyList.get(i).getIngredientName().equals(foodName)) {
                    // 수정전 : foodCount_double, 수정후 : count
                    if (foodCount_double < count) {
                        RefrigeratorDO.Item setlist = new RefrigeratorDO.Item();

                        setlist.setName(foodName);
                        setlist.setCount(count-foodCount_double);

                        updateItem.add(setlist);

                        Mapper.updateToBuyMemo(updateItem);

                    } else if ( foodCount_double > count) {
                        RecipeDO.Ingredient setlist = new RecipeDO.Ingredient();

                        setlist.setIngredientName(foodName);
                        setlist.setIngredientCount(foodCount_double-count);

                        appendItem.add(setlist);

                        Mapper.appendToBuyMemo(appendItem);
                    }
                }
            }

        } catch (NullPointerException e) {

        }
    }

    @Override
    public int getItemCount() {
        return dcArray.size();
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


