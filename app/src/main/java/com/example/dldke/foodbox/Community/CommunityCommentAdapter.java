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

public class CommunityCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<CommunityCommentItem> mItems;
    Context context ;

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;


    public CommunityCommentAdapter(ArrayList<CommunityCommentItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_comment_list, parent, false);
            return new ViewHolderComment(view);
        } else if (viewType == TYPE_TWO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_fullrecipe_detail_card_layout, parent, false);
            return new ViewHolderDetail(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO");
        }
    }

    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        CommunityCommentItem item= mItems.get(position);
        if (item.getType() == CommunityCommentItem.ItemType.ONE_ITEM) {
            return TYPE_ONE;
        } else if (item.getType() == CommunityCommentItem.ItemType.TWO_ITEM) {
            return TYPE_TWO;
        } else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {
        switch (holder.getItemViewType()) {
            case TYPE_ONE:
                initLayoutOne((ViewHolderComment)holder, listPosition);
                break;
            case TYPE_TWO:
                initLayoutTwo((ViewHolderDetail) holder, listPosition);
                break;
            default:
                break;
        }
    }

    private void initLayoutOne(ViewHolderComment holder, int position) {
        holder.userId.setText(mItems.get(position).getUserId());
        holder.comment.setText(mItems.get(position).getComment());
        holder.userImg.setImageDrawable(context.getResources().getDrawable(mItems.get(position).getUserImg(), null));
        holder.date.setText(mItems.get(position).getDate());
    }

    private void initLayoutTwo(ViewHolderDetail holder, int position) {
        holder.stepImage.setImageResource(mItems.get(position).getStepImage());
        holder.stepDescrip.setText(mItems.get(position).getDescription());
    }


    // Static inner class to initialize the views of rows
    static class ViewHolderComment extends RecyclerView.ViewHolder {
        private TextView userId;
        private ImageView userImg;
        private TextView comment;
        private TextView date;
        public ViewHolderComment(View itemView) {
            super(itemView);
            userId = (TextView) itemView.findViewById(R.id.userId);
            userImg = (ImageView) itemView.findViewById(R.id.userImg);
            comment = (TextView) itemView.findViewById(R.id.commentText);
            date = (TextView)itemView.findViewById(R.id.commentDate);
        }
    }

    static class ViewHolderDetail extends RecyclerView.ViewHolder {
        public ImageView stepImage;
        public TextView stepDescrip;
        public ViewHolderDetail(View itemView) {
            super(itemView);
            stepImage = (ImageView) itemView.findViewById(R.id.fullrecipe_detail_stepimg);
            stepDescrip = (TextView) itemView.findViewById(R.id.fullrecipe_detail_stepdescrip);
        }
    }
}
