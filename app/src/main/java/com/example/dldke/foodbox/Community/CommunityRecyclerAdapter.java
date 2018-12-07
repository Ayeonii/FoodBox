package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

import android.widget.Toast;


public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ItemViewHolder> {
    private ArrayList<CommunityItem> mItems;
    private Context context;
    private static ArrayList<CommunityItem> favoriteList = new ArrayList<>();
    private static String clicked_Recipe_id;


    public CommunityRecyclerAdapter(){}

    public CommunityRecyclerAdapter(ArrayList<CommunityItem> items , Context context){
        mItems = items;
        this.context = context;
    }
    public ArrayList<CommunityItem> getFavoriteList(){ return favoriteList; }

    private void setClickedRecipeId(String clicked_Recipe_id){
        this.clicked_Recipe_id = clicked_Recipe_id;
    }
    public String getClickedRecipeId(){
        return clicked_Recipe_id;
    }

    @Override
    public CommunityRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_card_list_item,parent,false);
        return new CommunityRecyclerAdapter.ItemViewHolder(view);
    }



    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final CommunityRecyclerAdapter.ItemViewHolder holder, final int position) {
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.community_cardview :
                        Toast.makeText(context, "카드뷰 클릭", Toast.LENGTH_SHORT).show();
                        setClickedRecipeId(mItems.get(position).getRecipeId());
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

        holder.communityUserId.setText(mItems.get(position).getUserId()+" 님이 레시피를 공유했습니다.");
        holder.communityFoodTitle.setText(mItems.get(position).getFoodTitle());
        holder.communityFoodName.setText(mItems.get(position).getFoodName());
        holder.star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItems.get(position).getFavorite()) {
                    holder.star_btn.setSelected(false);
                    Mapper.deleteFavorite(mItems.get(position).getPostId());
                }
                else if(!mItems.get(position).getFavorite()){
                    holder.star_btn.setSelected(true);
                    Mapper.addFavoriteInMyCommunity(mItems.get(position).getPostId());
                }
            }
        });

        if(mItems.get(position).getFavorite()){
            holder.star_btn.setSelected(true);
        }
        else if(!mItems.get(position).getFavorite()){
            holder.star_btn.setSelected(false);
        }

        final Bitmap foodImgUrl = mItems.get(position).getCommunity_foodImg();
        if(foodImgUrl == null) {
            holder.communityFoodImg.setBackground(context.getResources().getDrawable(R.drawable.splash_background, null));
        } else {
          holder.communityFoodImg.setImageBitmap(foodImgUrl);
           // new DownloadImageTask(holder.communityFoodImg).execute(foodImgUrl);
        }
        if(mItems.get(position).getCommunity_profile() ==-1)
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person,null));
        else
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(mItems.get(position).getCommunity_profile(),null));

        holder.cardView.setOnClickListener(onClickListener);
        holder.communityProfile.setOnClickListener(onClickListener);


    }


    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView communityFoodImg;
        private ImageView communityProfile;
        private TextView communityUserId;
        private TextView communityFoodTitle;
        private TextView communityFoodName;
        private ImageView star_btn;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.community_cardview);
            communityFoodImg = (ImageView) itemView.findViewById(R.id.community_food_image);
            communityProfile = (ImageView) itemView.findViewById(R.id.newsfeed_profile_image);
            communityUserId = (TextView) itemView.findViewById(R.id.user_id);
            communityFoodTitle = (TextView) itemView.findViewById(R.id.communityFoodTitle);
            communityFoodName = (TextView)itemView.findViewById(R.id.communityFoodName);
            star_btn = (ImageView)itemView.findViewById(R.id.star_btn);
        }
    }
}
