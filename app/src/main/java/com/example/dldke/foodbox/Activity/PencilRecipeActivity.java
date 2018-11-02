package com.example.dldke.foodbox.Activity;

<<<<<<< HEAD
=======
import android.content.Intent;
import android.icu.text.IDNA;
>>>>>>> Ayeon
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
<<<<<<< HEAD
=======
import android.widget.Button;
>>>>>>> Ayeon
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

<<<<<<< HEAD
import com.example.dldke.foodbox.Adapter.PencilPagerAdapter;
import com.example.dldke.foodbox.Fragments.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

public class PencilRecipeActivity extends AppCompatActivity {

    static public FrameLayout frag;
=======

import com.example.dldke.foodbox.Adapter.PencilCartAdapter;
import com.example.dldke.foodbox.Adapter.PencilPagerAdapter;
import com.example.dldke.foodbox.CartCalendarDialog;
import com.example.dldke.foodbox.CartPopupDialog;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.Fragments.SearchIngredientFragment;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.PencilCartItem;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter.clickFood;
import static com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter.clickFoodOnly;


>>>>>>> Ayeon

public class PencilRecipeActivity extends AppCompatActivity implements View.OnClickListener{
    public static boolean isFirst = true;
    FrameLayout frag;
    ViewPager vp;
    ImageButton deleteButton;
  //  Button completeButton,getInsideButton;
    EditText searchBar;
    TabLayout tabLayout;
    //ConstraintLayout popup_layout;
    //RecyclerView.Adapter adapter;
    String searchText;
    FloatingActionButton floating;
    //RecyclerView popup_cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencil_recipe);

<<<<<<< HEAD
        transaction = getSupportFragmentManager().beginTransaction();

        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        searchBar = (EditText)findViewById(R.id.searchBar);
        deleteButton = (ImageButton)findViewById(R.id.delete_button);
        frag = (FrameLayout)findViewById(R.id.child_fragment_container);

=======
        searchText = "";
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs); //탭 레이아웃
        searchBar = (EditText)findViewById(R.id.searchBar); //서치 창
        deleteButton = (ImageButton)findViewById(R.id.delete_button); //x버튼
        //completeButton = (Button)findViewById(R.id.completeButton);//확인
       // getInsideButton = (Button)findViewById(R.id.getInsideButton); //내 냉장고에 넣기
        floating = (FloatingActionButton)findViewById(R.id.floating); //플로팅
        //popup_layout = (ConstraintLayout)findViewById(R.id.popup_layout); //카드 팝업 레이아웃
        frag = (FrameLayout)findViewById(R.id.child_fragment_container); //검색시 나오는 화면

        /**view pager**/
>>>>>>> Ayeon
        vp = (ViewPager)findViewById(R.id.pager);
        vp.setAdapter(new PencilPagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        //탭 레이아웃과 뷰페이저 연결
        tabLayout.setupWithViewPager(vp);
<<<<<<< HEAD

        SearchIngredientFragment SearchFragment = new SearchIngredientFragment();
        transaction.replace(R.id.child_fragment_container, SearchFragment);
        transaction.commit();
=======
>>>>>>> Ayeon
        frag.setVisibility(View.GONE);
        searchBar.setOnClickListener(this);
        //popup_layout.setOnClickListener(this);
        floating.setOnClickListener(this);
//        completeButton.setOnClickListener(this);
 //       getInsideButton.setOnClickListener(this);

<<<<<<< HEAD
=======
        if(Mapper.checkFirst()){
            Mapper.createRefrigerator();
        }

        /****************search bar input *****************************/
>>>>>>> Ayeon
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
                Log.e("다이얼 클릭 ","다이얼 클릭 먹힘");
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                CartPopupDialog customDialog = new CartPopupDialog(PencilRecipeActivity.this);
                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                customDialog.callFunction();
                break;
        }
    }
}