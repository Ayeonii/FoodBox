package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.graphics.Bitmap;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

import static com.example.dldke.foodbox.Community.CommunityFragmentNewsfeed.list;

public class CommunityRecyclerAdapter extends RecyclerView.Adapter<CommunityRecyclerAdapter.ItemViewHolder> {
    private ArrayList<CommunityItem> mItems;
    private Context context;
    private static ArrayList<CommunityItem> favoriteList = new ArrayList<>();
    private static boolean isFavorite = false;
    private boolean isAgain = false;
    private static boolean isFirst = false;
    public static int cnt =0;

    public CommunityRecyclerAdapter(){}
    public CommunityRecyclerAdapter(ArrayList<CommunityItem> items , Context context){
        mItems = items;
        this.context = context;
    }

    public ArrayList<CommunityItem> getFavoriteList(){ return favoriteList; }

    public void setStarBtn(boolean isFavorite){
        this.isFavorite = isFavorite;
    }

    public void setFirst(boolean isFirst){
        this.isFirst = isFirst;
    }
    public boolean getFirst(){
        return isFirst;
    }

    @Override
    public CommunityRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_newsfeed_list_item,parent,false);
        cnt++;
        return new CommunityRecyclerAdapter.ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿈.
    @Override
    public void onBindViewHolder(final CommunityRecyclerAdapter.ItemViewHolder holder, final int position) {
        holder.communityUserId.setText(mItems.get(position).getUserId()+" 님이 레시피를 공유했습니다.");
        holder.communityFoodTitle.setText(mItems.get(position).getFoodTitle());
        holder.communityFoodName.setText(mItems.get(position).getFoodName());

        /********추후 list = > mItems로 변경********/
        if(mItems.get(position).getFavorite()){
            holder.star_btn.setSelected(true);
        }
        else{
            holder.star_btn.setSelected(false);
        }

        holder.star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItems.get(position).getFavorite()) {
                    holder.star_btn.setSelected(false);
                    favoriteList.remove(new CommunityItem(list.get(position).getUserId()
                            ,mItems.get(position).getFoodTitle()
                            ,mItems.get(position).getFoodName()
                            ,mItems.get(position).getCommunity_foodImg()
                            ,mItems.get(position).getCommunity_profile(),true));
                    mItems.get(position).setFavorite(false);
                    //notifyDataSetChanged();
                }
                else if(!mItems.get(position).getFavorite()){
                    holder.star_btn.setSelected(true);
                    //중복처리
                    //추후, postid 비교로 중복확인 해야함.
                    for(int i =0; i<favoriteList.size(); i++){
                        if(mItems.get(position).getFoodName().equals(favoriteList.get(i).getFoodName())){
                            Log.e("communityRecyclerAdapter", "들어옴?");
                            isAgain=true;
                        }
                    }
                    if(!isAgain){
                        favoriteList.add(new CommunityItem( list.get(position).getUserId()
                                ,mItems.get(position).getFoodTitle()
                                ,mItems.get(position).getFoodName()
                                ,mItems.get(position).getCommunity_foodImg()
                                ,mItems.get(position).getCommunity_profile(),false));

                        mItems.get(position).setFavorite(true);
                    }
                    //notifyDataSetChanged();
                }
            }
        });
        /**********************/
        if(mItems.get(position).getCommunity_foodImg() == -1) {
            holder.communityFoodImg.setBackground(context.getResources().getDrawable(R.drawable.splash_background, null));
        } else{
            holder.communityFoodImg.setBackground(context.getResources().getDrawable(mItems.get(position).getCommunity_foodImg(), null));
            //holder.communityFoodImg.setImageResource(mItems.get(position).getCommunity_foodImg());
        }
        if(mItems.get(position).getCommunity_profile() ==-1)
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person,null));
        else
            holder.communityProfile.setImageDrawable(context.getResources().getDrawable(mItems.get(position).getCommunity_profile(),null));

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
