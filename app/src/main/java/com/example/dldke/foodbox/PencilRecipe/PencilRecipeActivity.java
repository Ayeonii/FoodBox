package com.example.dldke.foodbox.PencilRecipe;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.FullRecipe.FullRecipeActivity;
import com.example.dldke.foodbox.R;



public class PencilRecipeActivity extends AppCompatActivity implements View.OnClickListener{
    FrameLayout frag;
    ViewPager vp;
    ImageButton deleteButton;
    EditText searchBar;
    TabLayout tabLayout;
    FloatingActionButton floating;

    private PencilRecyclerAdapter pencilRecyclerAdapter = new PencilRecyclerAdapter();
    private static int enterCnt = 0;
    private static boolean isFull = false;
    private static CartPopupDialog customDialog;

    public PencilRecipeActivity(){}


    public void setIsFull(boolean isFull){
        this.isFull = isFull;
    }

    public void setEnterTime(int enterCnt){
        this.enterCnt = enterCnt;
    }
    public int getEnterTime(){ return enterCnt;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencil_recipe);
        //searchText = "";

        pencilRecyclerAdapter.setIsRefri(false);
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs); //탭 레이아웃
        searchBar = (EditText)findViewById(R.id.searchBar); //서치 창
        deleteButton = (ImageButton)findViewById(R.id.delete_button); //x버튼
        floating = (FloatingActionButton)findViewById(R.id.floating); //플로팅
        frag = (FrameLayout)findViewById(R.id.child_fragment_container); //검색시 나오는 화면
        customDialog = new CartPopupDialog(PencilRecipeActivity.this);

        /**view pager**/
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(new PencilPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);
        frag.setVisibility(View.GONE);

        //클릭리스너
        searchBar.setOnClickListener(this);
        floating.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        //Mapper.createRefrigerator();

        if (Mapper.checkFirst()) {
            Mapper.createRefrigerator();
        }

        /****************search bar input *****************************/
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text;
                text = searchBar.getText().toString();
                if (text.length() == 0)
                    frag.setVisibility(View.GONE);
                else
                    frag.setVisibility(View.VISIBLE);
                SearchIngredientFragment.search(text, true,false,false,false);
            }

        });

    }

    @Override public void onBackPressed() {

        Intent refMain = new Intent(PencilRecipeActivity.this, RefrigeratorMainActivity.class);
        refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PencilRecipeActivity.this.startActivity(refMain);
        overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.searchBar:
                SearchIngredientFragment SearchFragment = new SearchIngredientFragment();
                transaction.replace(R.id.child_fragment_container, SearchFragment);
                transaction.commit();
                break;
            case R.id.floating:
//                if(!isFull)
//                    customDialog.callFunction(getApplicationContext());
//                else {
                    //Intent intent = new Intent(getApplicationContext(), FullRecipeActivity.class);
                    //startActivity(intent);
                    //customDialog.setisFull(isFull);
                    customDialog.callFunction(getApplicationContext());

                break;
            case R.id.delete_button:
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 재료명을 입력하세요.");
                    searchBar.setText(null);
                }
                break;

        }
    }
}