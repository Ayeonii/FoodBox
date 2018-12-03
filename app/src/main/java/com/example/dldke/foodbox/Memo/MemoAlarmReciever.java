package com.example.dldke.foodbox.Memo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;

import java.util.ArrayList;
import java.util.List;


public class MemoAlarmReciever extends BroadcastReceiver {

    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private static List<RecipeDO.Ingredient> urgentList = new ArrayList<>();

    public MemoAlarmReciever(){ }

    @Override
    public void onReceive(Context context, Intent intent) {
        urgentList = Mapper.scanUrgentMemo();
        refrigeratorMainActivity.setUrgentList(urgentList);
    }

}
