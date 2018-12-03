package com.example.dldke.foodbox.Memo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.widget.EditText;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class MemoActivity extends AppCompatActivity {

    ViewPager vp;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout); //탭 레이아웃

        /**view pager**/
        vp = (ViewPager) findViewById(R.id.view_pager);
        vp.setAdapter(new MemoPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);

    }

}
