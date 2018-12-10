package com.example.dldke.foodbox.Memo;

import android.net.Uri;

public class MemoToBuyItem {
    Uri tobuyImg;
    String tobuyName;
    Double tobuyCount;

    public MemoToBuyItem(Uri tobuyImg, String tobuyName, Double tobuyCount){
        this.tobuyImg = tobuyImg;
        this.tobuyName = tobuyName;
        this.tobuyCount = tobuyCount;
    }

    public Uri getTobuyImg() {
        return tobuyImg;
    }

    public void setTobuyImg(Uri tobuyImg) {
        this.tobuyImg = tobuyImg;
    }

    public String getTobuyName() {
        return tobuyName;
    }

    public void setTobuyName(String tobuyName) {
        this.tobuyName = tobuyName;
    }

    public Double getTobuyCount() {
        return tobuyCount;
    }

    public void setTobuyCount(Double tobuyCount) {
        this.tobuyCount = tobuyCount;
    }
}
