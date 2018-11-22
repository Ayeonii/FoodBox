package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ItemViewHolder> {
    private ArrayList<CommunityItem> mItems;
    private Context context;
    private static ArrayList<CommunityItem> favoriteList = new ArrayList<>();
    private boolean isAgain = false;


    public CommunityRecyclerAdapter(){}
    public CommunityRecyclerAdapter(ArrayList<CommunityItem> items , Context context){
        mItems = items;
        this.context = context;
    }
    public ArrayList<CommunityItem> getFavoriteList(){ return favoriteList; }

    @Override
    public CommunityRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_newsfeed_list_item,parent,false);
        return new CommunityRecyclerAdapter.ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final CommunityRecyclerAdapter.ItemViewHolder holder, final int position) {
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

        String foodImgUrl = mItems.get(position).getCommunity_foodImg();
        if(foodImgUrl == null) {
            holder.communityFoodImg.setBackground(context.getResources().getDrawable(R.drawable.splash_background, null));
        } else {
            new DownloadImageTask(holder.communityFoodImg).execute(foodImgUrl);
        }
        if(mItems.get(position).getCommunity_profile() ==-1)
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person,null));
        else
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(mItems.get(position).getCommunity_profile(),null));


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView communityFoodImg;
        private ImageView communityProfile;
        private TextView communityUserId;
        private TextView communityFoodTitle;
        private TextView communityFoodName;
        private ImageView star_btn;

        public ItemViewHolder(View itemView) {
            super(itemView);
            communityFoodImg = (ImageView) itemView.findViewById(R.id.community_food_image);
            communityProfile = (ImageView) itemView.findViewById(R.id.newsfeed_profile_image);
            communityUserId = (TextView) itemView.findViewById(R.id.user_id);
            communityFoodTitle = (TextView) itemView.findViewById(R.id.communityFoodTitle);
            communityFoodName = (TextView)itemView.findViewById(R.id.communityFoodName);
            star_btn = (ImageView)itemView.findViewById(R.id.star_btn);
        }
    }
}
