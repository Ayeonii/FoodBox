package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import com.example.dldke.foodbox.Adapter.PencilPagerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.Fragments.SearchIngredientFragment;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter.clickFood;
import static com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter.clickFoodString;


public class PencilRecipeActivity extends AppCompatActivity implements View.OnClickListener{
    public static boolean isFirst = true;
    FrameLayout frag;
    ViewPager vp;
    ImageButton deleteButton;
    Button completeButton,getInsideButton;
    EditText searchBar;
    TabLayout tabLayout;
    ConstraintLayout popup_layout;
    RecyclerView.Adapter adapter;
    String searchText;
    FloatingActionButton floating;
    RecyclerView popup_cart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencil_recipe);

        searchText = "";
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs); //탭 레이아웃
        searchBar = (EditText)findViewById(R.id.searchBar); //서치 창
        deleteButton = (ImageButton)findViewById(R.id.delete_button); //x버튼
        completeButton = (Button)findViewById(R.id.completeButton);//확인
        getInsideButton = (Button)findViewById(R.id.getInsideButton); //내 냉장고에 넣기
        floating = (FloatingActionButton)findViewById(R.id.floating); //플로팅
        popup_layout = (ConstraintLayout)findViewById(R.id.popup_layout); //카드 팝업 레이아웃
        frag = (FrameLayout)findViewById(R.id.child_fragment_container); //검색시 나오는 화면

        /**cart popup**/
        popup_cart = (RecyclerView)findViewById(R.id.cart_recycler);
        popup_cart.setHasFixedSize(true);
        adapter = new PencilRecyclerAdapter(clickFood);
        popup_cart.setLayoutManager(new GridLayoutManager(getApplicationContext(),5));
        popup_cart.setAdapter(adapter);

        /**view pager**/
        vp = (ViewPager)findViewById(R.id.pager);
        vp.setAdapter(new PencilPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);
        frag.setVisibility(View.GONE);
        searchBar.setOnClickListener(this);
        popup_layout.setOnClickListener(this);
        floating.setOnClickListener(this);
        completeButton.setOnClickListener(this);
        getInsideButton.setOnClickListener(this);

        if(Mapper.checkFirst()){
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
                if(text.length() == 0)
                    frag.setVisibility(View.GONE);
                else
                    frag.setVisibility(View.VISIBLE);
                SearchIngredientFragment.search(text);
            }
        });


        /****************delete button***********************/
        deleteButton = (ImageButton) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //텍스트가 존재 시, 모두 지운다.
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 재료명을 입력하세요.");
                    searchBar.setText(null);
                }
            }
        });
    }

    public void checkFirst(){
        //냉장고 테이블 있는지 확인 => 없으면 생성
            Log.e("냉장고 없음","생성 시작");
            Mapper.createRefrigerator();
            isFirst = false;
    }
    @Override public void onBackPressed() {
        Intent refMain = new Intent(PencilRecipeActivity.this, RefrigeratorMainActivity.class);
        refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PencilRecipeActivity.this.startActivity(refMain);
        overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);
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
                floating.setVisibility(View.GONE);
                popup_layout.setVisibility(View.VISIBLE);
                popup_layout.setElevation(8);
                popup_cart.setElevation(10);
                Log.e("floating","floating의 Elevation"+floating.getElevation());
                break;
            case R.id.popup_layout:
                floating.setVisibility(View.VISIBLE);
                popup_layout.setVisibility(View.GONE);
                popup_layout.setElevation(0);
                popup_cart.setElevation(0);
                break;
            case R.id.completeButton:
                floating.setVisibility(View.VISIBLE);
                popup_layout.setVisibility(View.GONE);
                popup_layout.setElevation(0);
                popup_cart.setElevation(0);
                break;
            case R.id.getInsideButton:
                List<RefrigeratorDO.Item> clickedList = new ArrayList<>();
                //사용자가 클릭한 음식만큼 리스트에 넣음
                for(String name : clickFoodString) {
                    clickedList.add(Mapper.createFood(Mapper.searchFood(name), 5.0));
                }
                Mapper.putFood(clickedList);

                break;
        }
    }
}