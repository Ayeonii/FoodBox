package com.example.dldke.foodbox;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import java.util.ArrayList;

public class PencilRecipeActivity extends AppCompatActivity {
    /*
   나중에 디비에서 불러올 것
    */
//    Context context;


    private String []food_name = {"tomato", "noodle","rice", "milk","chocolate"} ;
    private Drawable Img;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static ImageView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRecyclerView();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        EditText searchBar = (EditText) findViewById(R.id.searchBar);
        //키보드 포커스 없앰
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    private void setRecyclerView(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);

        Log.e("Frag", "직접입력");

        setData();
    }

    private void setData(){
        // 배열로 바꾼 후 db에서 불러올것
         Img = getResources().getDrawable( R.drawable.ic_circle_food,getApplicationContext().getTheme());

// RecyclerView 에 들어갈 데이터를 추가합니다.
        for(int i =0 ; i<food_name.length; i++){
            list.add(new PencilItem(food_name[i], Img));
        }

// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }

}
