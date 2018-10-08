package com.example.dldke.foodbox.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;
import com.amazonaws.mobile.api.id8z9a74jyqj.EchoTestMobileHubClient;

import java.util.ArrayList;
import java.util.List;

public class PencilRecipeActivity extends AppCompatActivity {
    /*
   나중에 디비에서 불러올 것
    */
    EchoTestMobileHubClient apiClient;

    //private String []food_name = {"tomato", "noodle","rice", "milk","chocolate"} ;
    private Drawable Img;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();

    private DynamoDBMapper dynamoMapper;
     List<String> foodName = new ArrayList<String>();

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
        setContentView(R.layout.activity_pencil_recipe);

        List<InfoDO> itemList = Mapper.scanInfo( "fresh");
        for(int i = 0; i < itemList.size(); i++)
        {
            foodName.add(itemList.get(i).getName());
        }

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
        Img = getResources().getDrawable( R.drawable.ic_circle_food,getApplicationContext().getTheme());//sdk 23이상일 때
        //Img = getResources().getDrawable( R.drawable.ic_circle_food);//sdk 22이하일 때


// RecyclerView 에 들어갈 데이터를 추가합니다.

        for(String name : foodName){
            list.add(new PencilItem(name, Img));
        }
        /*
        for(int i =0 ; i<foodName.size(); i++){

        }
        */

// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }

}
