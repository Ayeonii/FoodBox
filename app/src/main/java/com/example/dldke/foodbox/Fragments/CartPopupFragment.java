package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;


public class CartPopupFragment extends  android.support.v4.app.Fragment {
    private Drawable Img;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<PencilItem> list = new ArrayList<>();
    private String foodImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cart_popup, container, false);
        Context context = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.cartRecycler);
        recyclerView.setHasFixedSize(true);

        //어댑터 연결
        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        recyclerView.setAdapter(adapter);
        setData();
        Log.e("Frag", "cart_popup");
        return view;
    }

    private void setData(){

        // 재료 이미지 db에서 불러올것
        //Img = getResources().getDrawable( R.drawable.ic_circle_food,getContext().getTheme());//sdk 23이상일 때
        //Img = getResources().getDrawable( R.drawable.ic_circle_food);//sdk 22이하일 때

        // RecyclerView 에 들어갈 데이터를 추가한다.
        for(String name : PencilRecyclerAdapter.clickFoodString){
            foodImg = "/storage/emulated/0/Download/"+name+"jpg";
            list.add(new PencilItem(name, Uri.parse(foodImg)));
        }
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행한다.
        adapter.notifyDataSetChanged();
    }


}
