package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amazonaws.mobile.api.id8z9a74jyqj.EchoTestMobileHubClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class AllFoodListFragment extends android.support.v4.app.Fragment {

    //디비에서 이미지 가져오기 전까진  static public으로
    static public Drawable Img;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private List<InfoDO> freshList, meatList, etcList;
    //검색할때 다시 쓰기 위해 static 선언.

    static public List<String> allfoodList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_all_ingredients, container, false);



            freshList = getInfoDOList("fresh");
            meatList = getInfoDOList("meat");
            etcList = getInfoDOList("etc");
            makeFoodList(freshList);
            makeFoodList(meatList);
            makeFoodList(etcList);

            Context context = view.getContext();

            recyclerView = (RecyclerView)view.findViewById(R.id.allRecycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context,5));

            // use a linear layout manager
            adapter = new PencilRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);

            Log.e("Frag", "All");

            setData();




        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanInfo(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
        for(int i =0 ; i< foodList.size(); i++)
        {
            allfoodList.add(foodList.get(i).getName());
        }
    }

    private void setData(){

        // 재료 이미지 db에서 불러올것
        Img = getResources().getDrawable( R.drawable.ic_circle_food,getContext().getTheme());//sdk 23이상일 때
        //Img = getResources().getDrawable( R.drawable.ic_circle_food);//sdk 22이하일 때

        // RecyclerView 에 들어갈 데이터를 추가한다.
        for(String name : allfoodList){
            list.add(new PencilItem(name, Img));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행한다.
        adapter.notifyDataSetChanged();
    }
}
