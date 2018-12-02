package com.example.dldke.foodbox.Memo;

import android.net.Uri;

public class MemoUrgentItem {
    Uri urgentImg;
    String urgentName;
    String urgentDate;

    public MemoUrgentItem(Uri urgentImg, String urgentName, String urgentDate){
        this.urgentImg = urgentImg;
        this.urgentName = urgentName;
        this.urgentDate = urgentDate;
    }

    public Uri getUrgentImg() {
        return urgentImg;
    }

    public String getUrgentText() {
        return urgentName;
    }

    public void setUrgentDate(String urgentDate){
        this.urgentDate = urgentDate;
    }
    public String getUrgentDate() {
        return urgentDate;
    }

}
