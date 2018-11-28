package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.ItemViewHolder> {

    ArrayList<CommunityCommentItem> mItems;
    Context context ;

    public CommunityCommentAdapter(ArrayList<CommunityCommentItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public CommunityCommentAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_comment_list,parent,false);
        return new CommunityCommentAdapter.ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final CommunityCommentAdapter.ItemViewHolder holder, final int position) {
        holder.userId.setText(mItems.get(position).getUserId());
        holder.comment.setText(mItems.get(position).getComment());
        holder.userImg.setImageDrawable(context.getResources().getDrawable(mItems.get(position).getUserImg(), null));
        holder.date.setText(mItems.get(position).getDate());
    }


    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }


    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView userId;
        private ImageView userImg;
        private TextView comment;
        private TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            userId = (TextView) itemView.findViewById(R.id.userId);
            userImg = (ImageView) itemView.findViewById(R.id.userImg);
            comment = (TextView) itemView.findViewById(R.id.commentText);
            date = (TextView)itemView.findViewById(R.id.commentDate);
        }
    }


}
