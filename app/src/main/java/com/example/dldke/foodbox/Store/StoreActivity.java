package com.example.dldke.foodbox.Store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {

    ArrayList<StoreData> items = new ArrayList<>();
    String TAG="StoreActivity";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        String theme = Mapper.searchUserInfo().getTheme();
        int user_point = Mapper.searchUserInfo().getPoint();

        Log.e(TAG, "사용자 테마 : "+theme+"사용자 포인트 : "+user_point);

        getData();

        TextView user_point_text = (TextView) findViewById(R.id.user_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView theme_recyclerview = (RecyclerView) findViewById(R.id.theme_list);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성

        String point = String.valueOf(Mapper.searchUserInfo().getPoint());
        user_point_text.setText(point);

        theme_recyclerview.setHasFixedSize(true);
        StoreAdapter adapter = new StoreAdapter(getApplicationContext(), items);
        theme_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        theme_recyclerview.setAdapter(adapter);
    }

    public void getData(){
        items.add(new StoreData(0, "기본 테마", 0));
        items.add(new StoreData(1, "블랙", 200));
        items.add(new StoreData(2, "베이지", 300));

    }

    @Override public void onBackPressed() {

        Intent refMain = new Intent(StoreActivity.this, RefrigeratorMainActivity.class);
        refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        StoreActivity.this.startActivity(refMain);
        overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.Activity.RefrigeratorMainActivity.class);
            RefrigeratorMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(RefrigeratorMainActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
