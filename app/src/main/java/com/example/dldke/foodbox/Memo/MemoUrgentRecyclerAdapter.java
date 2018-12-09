package com.example.dldke.foodbox.Memo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.PencilRecipe.CurrentDate;
import com.example.dldke.foodbox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemoUrgentRecyclerAdapter extends RecyclerView.Adapter<MemoUrgentRecyclerAdapter.ItemViewHolder> {
    private CurrentDate currentDate = new CurrentDate();
    private static long diffDays;
    private ArrayList<MemoUrgentItem> mItems;
    private Context context;

    public MemoUrgentRecyclerAdapter(ArrayList<MemoUrgentItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public MemoUrgentRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_urgent_list,parent,false);
        return new MemoUrgentRecyclerAdapter.ItemViewHolder(view);
    }
    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final MemoUrgentRecyclerAdapter.ItemViewHolder holder, final int position) {
        holder.urgentImg.setImageURI(mItems.get(position).getUrgentImg());
        holder.urgentName.setText(mItems.get(position).getUrgentName());
        CalculateDate(mItems.get(position).getUrgentDate());

        if (diffDays < 0) {
            holder.urgentDate.setTextColor(Color.RED);
            holder.urgentDate.setText(Math.abs(diffDays) + "일 지났습니다.");
        }
        else
            holder.urgentDate.setText(diffDays+"일 남았습니다.");

    }
    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView urgentImg;
        private TextView urgentName;
        private TextView urgentDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            urgentImg = (ImageView)itemView.findViewById(R.id.urgentImg);
            urgentName = (TextView) itemView.findViewById(R.id.urgentName);
            urgentDate = (TextView) itemView.findViewById(R.id.urgentDate);
        }
    }

    public void CalculateDate(String urgentFoodDate){
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            long diff;
            try {
                Date foodDate = formatter.parse(urgentFoodDate);
                Date curDay = formatter.parse(currentDate.getCurrenDate());
                diff = foodDate.getTime() - curDay.getTime();
                diffDays = diff / (24 * 60 * 60 * 1000);

            } catch (ParseException e){
                e.printStackTrace();
            }

    }

}
