package com.example.dldke.foodbox.Community;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class CommunityItem {
    private String userId;
    private String foodTitle;
    private String foodName;
    private int community_foodImg;
    private int community_profile;
    private boolean isFavorite;
    public CommunityItem (String userId
            ,String foodTitle
            ,String foodName
            ,int community_foodImg
            ,int community_profile, boolean isFavorite){
        this.userId = userId;
        this.foodTitle = foodTitle;
        this.foodName = foodName;
        this.community_foodImg = community_foodImg;
        this.community_profile = community_profile;
        this.isFavorite = isFavorite;
    }

    public String getUserId() { return userId; }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodTitle() { return foodTitle; }

    public int getCommunity_foodImg(){return community_foodImg;}

    public int getCommunity_profile(){return community_profile;}

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }
    public boolean getFavorite() { return isFavorite; }
}
