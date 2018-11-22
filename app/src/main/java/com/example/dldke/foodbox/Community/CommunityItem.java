package com.example.dldke.foodbox.Community;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class CommunityItem {
    private String userId;
    private String foodTitle;
    private String foodName;
    private String community_foodImg;
    private int community_profile;
    private boolean isFavorite;
    private String postId;

    public CommunityItem (String userId
            ,String foodTitle
            ,String foodName
            ,String community_foodImg
            ,int community_profile
            , boolean isFavorite
            , String postId){
        this.userId = userId;
        this.foodTitle = foodTitle;
        this.foodName = foodName;
        this.community_foodImg = community_foodImg;
        this.community_profile = community_profile;
        this.isFavorite = isFavorite;
        this.postId = postId;
    }

    public String getUserId() { return userId; }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodTitle() { return foodTitle; }

    public String getCommunity_foodImg(){return community_foodImg;}

    public int getCommunity_profile(){return community_profile;}

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }
    public boolean getFavorite() { return isFavorite; }

    public String getPostId(){return postId; }
}
