package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FreshListFragment extends android.support.v4.app.Fragment {

    private Drawable Img;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();


    List<String> foodName = new ArrayList<String>();

    public FreshListFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fresh_ingredients, container, false);

            List<InfoDO> freshList = getInfoDOList("fresh");

            makeFoodList(freshList);


            Context context = view.getContext();

            recyclerView = (RecyclerView) view.findViewById(R.id.freshRecycler);
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            adapter = new PencilRecyclerAdapter(list);
            recyclerView.setLayoutManager(new GridLayoutManager(context,5));
            recyclerView.setAdapter(adapter);

            Log.e("Frag", "fresh");

            setData();




        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanInfo(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
        for(int i =0 ; i< foodList.size(); i++)
        {
            foodName.add(foodList.get(i).getName());
        }
    }

    private void setData(){

        // 재료 이미지 db에서 불러올것
        Img = getResources().getDrawable( R.drawable.ic_circle_food,getContext().getTheme());//sdk 23이상일 때
        //Img = getResources().getDrawable( R.drawable.ic_circle_food);//sdk 22이하일 때

        // RecyclerView 에 들어갈 데이터를 추가한다.
        for(String name : foodName){
            list.add(new PencilItem(name, Img));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행한다.
        adapter.notifyDataSetChanged();
    }
}
