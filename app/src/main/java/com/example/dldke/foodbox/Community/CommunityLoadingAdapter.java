package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.Toast;

public class CommunityLoadingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private ArrayList<CommunityItem> itemList;
    private static List<CommunityItem> favoriteList = new ArrayList<>();
    private Context context;
    private static String clicked_Recipe_id;
    private static String clicked_Post_id;
    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;

    private void setClickedRecipeId(String clicked_Recipe_id){
        this.clicked_Recipe_id = clicked_Recipe_id;
    }
    public String getClickedRecipeId(){
        return clicked_Recipe_id;
    }

    private void setClickedPostId(String clicked_Post_id){
        this.clicked_Post_id = clicked_Post_id;
    }
    public String getClickedPostId(){
        return clicked_Post_id;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public CommunityLoadingAdapter(){
    }

    public CommunityLoadingAdapter(OnLoadMoreListener onLoadMoreListener, Context context) {
        this.onLoadMoreListener=onLoadMoreListener;
        itemList =new ArrayList<>();
        favoriteList.clear();
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
            return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.community_card_list_item, parent, false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item_progress, parent, false));
        }

    }
    public void addAll(List<CommunityItem> lst){
            itemList.clear();
            itemList.addAll(lst);
            favoriteList.clear();
            notifyDataSetChanged();
    }

    public void addItemMore(List<CommunityItem> lst){
        itemList.addAll(lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.community_food_image:
                    case R.id.communityFoodTitle:
                    case R.id.community_cardview :
                    case R.id.communityFoodName:
                        setClickedRecipeId(itemList.get(position).getRecipeId());
                        setClickedPostId(itemList.get(position).getPostId());
                        Intent refMain = new Intent(context, CommunityDetailActivity.class);
                        refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(refMain);
                        break ;
                    case R.id.user_id:
                        Toast.makeText(context, "user_id click", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        } ;
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


            ((StudentViewHolder) holder).cardView.setOnClickListener(onClickListener);
            ((StudentViewHolder) holder).communityProfile.setOnClickListener(onClickListener);
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
        private CardView cardView;
        private ImageView communityFoodImg;
        private ImageView communityProfile;
        private TextView communityUserId;
        private TextView communityFoodTitle;
        private TextView communityFoodName;
        private ImageView star_btn;

        public StudentViewHolder(View v) {
            super(v);
            cardView = (CardView)itemView.findViewById(R.id.community_cardview);
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
