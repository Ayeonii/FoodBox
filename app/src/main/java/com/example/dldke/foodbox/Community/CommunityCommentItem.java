package com.example.dldke.foodbox.Community;
import android.graphics.Bitmap;
import android.net.Uri;

public class CommunityCommentItem {

    private Bitmap userImg;
    private String userId;
    private String comment;
    private String date;
    String description;
    int stepImage;
    private ItemType type;

    public enum ItemType {
        ONE_ITEM, TWO_ITEM;
    }

    public CommunityCommentItem (String userId, Bitmap userImg, String comment, String date
            , int stepImage, String description, ItemType itemType){
        this.userId = userId;
        this.userImg = userImg;
        this.comment = comment;
        this.date = date;
        this.stepImage = stepImage;
        this.description = description;
        this.type = itemType;
    }

    public String getUserId() {
        return userId;
    }

    public Bitmap getUserImg() { return userImg; }

    public String getComment() {
        return comment;
    }

    public String getDate(){return date;}

    public String getDescription(){
        return description;
    }

    public int getStepImage(){
        return stepImage;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }
}
