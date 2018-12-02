package com.example.dldke.foodbox.Memo;

import android.content.Context;
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
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.ItemViewHolder> {

    private ArrayList<MemoUrgentItem> mItems;
    private Context context;

    public MemoRecyclerAdapter(ArrayList<MemoUrgentItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public MemoRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_list_ingredient,parent,false);
        return new MemoRecyclerAdapter.ItemViewHolder(view);
    }
    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final MemoRecyclerAdapter.ItemViewHolder holder, final int position) {

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

}
