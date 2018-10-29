package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import com.example.dldke.foodbox.Adapter.PencilPagerAdapter;
import com.example.dldke.foodbox.Fragments.SearchIngredientFragment;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;


public class PencilRecipeActivity extends AppCompatActivity implements View.OnClickListener{

    public static boolean isFull = false;
    FrameLayout frag;

    ViewPager vp;
    ImageButton deleteButton, deletButton_cart;
    EditText searchBar;
    TabLayout tabLayout;
    RelativeLayout popup_layout;
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
        deletButton_cart =(ImageButton)findViewById(R.id.delete_cart_button);
        floating = (FloatingActionButton)findViewById(R.id.floating); //플로팅
        popup_layout = (RelativeLayout)findViewById(R.id.popup_layout); //카드 팝업 레이아웃
        frag = (FrameLayout)findViewById(R.id.child_fragment_container);

        /**cart popup**/
        popup_cart = (RecyclerView)findViewById(R.id.cart_recycler);
        popup_cart.setHasFixedSize(true);
        adapter = new PencilRecyclerAdapter(PencilRecyclerAdapter.clickFood);
        popup_cart.setLayoutManager(new GridLayoutManager(getApplicationContext(),5));
        popup_cart.setAdapter(adapter);






        /**view pager**/
        vp = (ViewPager)findViewById(R.id.pager);
        vp.setAdapter(new PencilPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);

        // transaction.commit();
        frag.setVisibility(View.GONE);

        searchBar.setOnClickListener(this);
        popup_layout.setOnClickListener(this);
        floating.setOnClickListener(this);
        deletButton_cart.setOnClickListener(this);



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
                // input창에 문자를 입력할때마다 호출됨.
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
            case R.id.delete_cart_button:
                floating.setVisibility(View.VISIBLE);
                popup_layout.setVisibility(View.GONE);
                popup_layout.setElevation(0);
                popup_cart.setElevation(0);

                break;

        }
    }

}