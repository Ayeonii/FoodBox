package com.example.dldke.foodbox.Community;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    String searchText;
    ImageButton deleteButton;
    EditText searchBar;
    ImageView homeBtn, favoriteBtn,recommendBtn;
    android.app.FragmentManager fm;
    android.app.FragmentTransaction tran;
    CommunityFragmentNewsfeed fragmentNewsfeed;
    CommunityFragmentFavorite fragmentFavorite;
    CommunityFragmentRecommend fragmentRecommend;
    CommunityFragmentSearch fragmentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        fragmentNewsfeed = new CommunityFragmentNewsfeed();
        fragmentFavorite = new CommunityFragmentFavorite();
        fragmentRecommend = new CommunityFragmentRecommend();
        fragmentSearch = new CommunityFragmentSearch();

        searchText = "";
        searchBar = (EditText)findViewById(R.id.community_searchBar);
        deleteButton = (ImageButton)findViewById(R.id.community_delete_button);

        homeBtn = (ImageView)findViewById(R.id.home_btn);
        favoriteBtn =(ImageView)findViewById(R.id.favorite_btn);
        recommendBtn = (ImageView)findViewById(R.id.recommend_btn);

        homeBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        recommendBtn.setOnClickListener(this);
        /****************search bar input *****************************/

        /*
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
                    fragmentSearch.setVisibility(View.GONE);
                else
                    fragmentSearch.setVisibility(View.VISIBLE);
                SearchIngredientFragment.search(text);
            }
        });
        */


        /****************delete button***********************/
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //텍스트가 존재 시, 모두 지운다.
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 검색어를 입력하세요.");
                    searchBar.setText(null);
                }
            }
        });

        setFrag(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_btn:
                setFrag(0);
                setBackeGroundAccent(homeBtn, favoriteBtn,recommendBtn);
                break;
            case R.id.favorite_btn:
                setFrag(1);
                setBackeGroundAccent(favoriteBtn, homeBtn,recommendBtn);
                break;
            case R.id.recommend_btn:
                setFrag(2);
                setBackeGroundAccent(recommendBtn, favoriteBtn,homeBtn);
                break;
        }
    }

    public void setBackeGroundAccent(ImageView firstBtn,ImageView secondBtn,ImageView thirdBtn){
        firstBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent, null));
        secondBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite, null));
        thirdBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite, null));
    }
    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.frag_layout, fragmentNewsfeed);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.frag_layout, fragmentFavorite);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.frag_layout, fragmentRecommend);
                tran.commit();
                break;
        }
    }

}
