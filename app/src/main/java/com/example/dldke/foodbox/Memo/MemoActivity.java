package com.example.dldke.foodbox.Memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.R;

public class MemoActivity extends AppCompatActivity {

    ViewPager vp;
    TabLayout tabLayout;
    RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    String TAG = "MemoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        boolean isMemo = refrigeratorMainActivity.getIsMemo();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //toolbar 뒤로가기 생성

        tabLayout = (TabLayout)findViewById(R.id.tabLayout); //탭 레이아웃

        /**view pager**/
        vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(new MemoPagerAdapter(getSupportFragmentManager()));
        if(isMemo){
            vp.setCurrentItem(1);
        }
        else {
            vp.setCurrentItem(0);
        }

        tabLayout.setupWithViewPager(vp);

    }

    //뒤로 가기 버튼 눌렀을 시 메인화면으로 넘어가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                RefrigeratorMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(RefrigeratorMainActivity);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
