package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class CommunityLoadingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ArrayList<CommunityItem> itemList;
    private Context context;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public CommunityLoadingAdapter(OnLoadMoreListener onLoadMoreListener, Context context) {
        this.onLoadMoreListener=onLoadMoreListener;
        itemList =new ArrayList<>();
        this.context = context;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                Log.e("total", totalItemCount + "");
                Log.e("visible", visibleItemCount + "");

                Log.e("first", firstVisibleItem + "");
                Log.e("last", lastVisibleItem + "");

                if (!isMoreLoading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.community_newsfeed_list_item, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_progress, parent, false));
        }

    }

    public void addAll(List<CommunityItem> lst){

            itemList.clear();
            itemList.addAll(lst);
            notifyDataSetChanged();

    }

    public void addItemMore(List<CommunityItem> lst){
        itemList.addAll(lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StudentViewHolder) {
            ((StudentViewHolder) holder).communityUserId.setText(itemList.get(position).getUserId()+" 님이 레시피를 공유했습니다.");
            ((StudentViewHolder) holder).communityFoodTitle.setText(itemList.get(position).getFoodTitle());
            ((StudentViewHolder) holder).communityFoodName.setText(itemList.get(position).getFoodName());


            ((StudentViewHolder) holder).star_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemList.get(position).getFavorite()) {
                        ((StudentViewHolder) holder).star_btn.setSelected(false);
                        Mapper.deleteFavorite(itemList.get(position).getPostId());
                    }
                    else if(!itemList.get(position).getFavorite()){
                        ((StudentViewHolder) holder).star_btn.setSelected(true);
                        Mapper.addFavoriteInMyCommunity(itemList.get(position).getPostId());
                    }
                }
            });
            if(itemList.get(position).getFavorite()){
                ((StudentViewHolder) holder).star_btn.setSelected(true);
            }
            else if(!itemList.get(position).getFavorite()){
                ((StudentViewHolder) holder).star_btn.setSelected(false);
            }
            Bitmap foodImgUrl = itemList.get(position).getCommunity_foodImg();
            if(foodImgUrl == null) {
                ((StudentViewHolder) holder).communityFoodImg.setImageDrawable(context.getResources().getDrawable(R.drawable.splash_background, null));
            } else {
                ((StudentViewHolder) holder).communityFoodImg.setImageBitmap(foodImgUrl);
                //new CommunityLoadingAdapter.DownloadImageTask( ((StudentViewHolder) holder).communityFoodImg).execute(foodImgUrl);
            }
            if(itemList.get(position).getCommunity_profile() ==-1)
                ((StudentViewHolder) holder).communityProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person,null));
            else
                ((StudentViewHolder) holder).communityProfile.setImageDrawable(context.getResources().getDrawable(itemList.get(position).getCommunity_profile(),null));

        }
    }


    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading=isMoreLoading;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    itemList.add(null);
                    notifyItemInserted(itemList.size() - 1);
                }
            });
        } else {
            itemList.remove(itemList.size() - 1);
            notifyItemRemoved(itemList.size());
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private ImageView communityFoodImg;
        private ImageView communityProfile;
        private TextView communityUserId;
        private TextView communityFoodTitle;
        private TextView communityFoodName;
        private ImageView star_btn;

        public StudentViewHolder(View v) {
            super(v);
            communityFoodImg = (ImageView) itemView.findViewById(R.id.community_food_image);
            communityProfile = (ImageView) itemView.findViewById(R.id.newsfeed_profile_image);
            communityUserId = (TextView) itemView.findViewById(R.id.user_id);
            communityFoodTitle = (TextView) itemView.findViewById(R.id.communityFoodTitle);
            communityFoodName = (TextView)itemView.findViewById(R.id.communityFoodName);
            star_btn = (ImageView)itemView.findViewById(R.id.star_btn);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;
        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.pBar);
        }
    }
}
