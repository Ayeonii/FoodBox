package com.example.dldke.foodbox.Community;
import android.net.Uri;

public class CommunityCommentItem {

    private int userImg;
    private String userId;
    private String comment;
    private String date;


    public CommunityCommentItem (String userId, int userImg, String comment, String date){
        this.userId = userId;
        this.userImg = userImg;
        this.comment = comment;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public int getUserImg() { return userImg; }

    public String getComment() {
        return comment;
    }

    public String getDate(){return date;}
}
