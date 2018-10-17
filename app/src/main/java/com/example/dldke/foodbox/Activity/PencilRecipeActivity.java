package com.example.dldke.foodbox.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.dldke.foodbox.Adapter.PencilPagerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.Fragments.AllFoodListFragment;
import com.example.dldke.foodbox.Fragments.SearchIngredientFragment;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;
import com.amazonaws.mobile.api.id8z9a74jyqj.EchoTestMobileHubClient;

import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.Fragments.AllFoodListFragment.*;


public class PencilRecipeActivity extends AppCompatActivity {


    static public FrameLayout frag;

    ViewPager vp;
    ImageButton deleteButton;
    EditText searchBar;
    TabLayout tabLayout;
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencil_recipe);


        transaction = getSupportFragmentManager().beginTransaction();


        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        searchBar = (EditText)findViewById(R.id.searchBar);
        deleteButton = (ImageButton)findViewById(R.id.delete_button);
        frag = (FrameLayout)findViewById(R.id.child_fragment_container);


        vp = (ViewPager)findViewById(R.id.pager);
        vp.setAdapter(new PencilPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);


        SearchIngredientFragment SearchFragment = new SearchIngredientFragment();
        transaction.replace(R.id.child_fragment_container, SearchFragment);
        transaction.commit();
        frag.setVisibility(View.GONE);



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




        /****************delete button click *****************************/
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




}
