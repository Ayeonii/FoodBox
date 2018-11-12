package com.example.dldke.foodbox.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.dldke.foodbox.Adapter.RecipeBoxPagerAdapter;
import com.example.dldke.foodbox.R;

public class MyRecipeActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tablayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box);

        //Cutomized Toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //toolbar 뒤로가기 생성


        //탭 생성
        tablayout = (TabLayout)findViewById(R.id.tabLayout);
        tablayout.addTab(tablayout.newTab().setText("간이 레시피"));
        tablayout.addTab(tablayout.newTab().setText("풀레시피"));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //뷰 페이지 생성
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        //뷰 어댑터 연결
        viewPager.setAdapter(new RecipeBoxPagerAdapter(getSupportFragmentManager(), tablayout.getTabCount()));
        //viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
