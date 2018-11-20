package com.example.dldke.foodbox.MyRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxAdapter;
import com.example.dldke.foodbox.R;

public class MyRecipeBoxActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tablayout;
    Toolbar toolbar;

    public static int position;

    public int getPosition(){
        return position;
    }

    private String TAG = "MyRecipeBoxActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box);

        //Cutomized Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //toolbar 뒤로가기 생성


        //탭 생성
        tablayout = (TabLayout) findViewById(R.id.tabLayout);
        tablayout.addTab(tablayout.newTab().setText("간이 레시피"));
        tablayout.addTab(tablayout.newTab().setText("풀레시피"));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //뷰 페이지 생성
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //뷰 어댑터 연결
        viewPager.setAdapter(new RecipeBoxPagerAdapter(getSupportFragmentManager(), tablayout.getTabCount()));
        //viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                position = tab.getPosition();
                RecipeBoxAdapter recipeBoxAdapter = new RecipeBoxAdapter();
                recipeBoxAdapter.setTabPosition(position);
                Log.e(TAG, "포지션을 얻었다 : "+position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //뒤로 가기 버튼 눌렀을 시 메인화면으로 넘어가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent RefrigMain = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
            RefrigMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(RefrigMain);
        }
        return super.onOptionsItemSelected(item);
    }
}
