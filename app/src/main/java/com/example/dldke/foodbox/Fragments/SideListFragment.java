package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class SideListFragment extends  android.support.v4.app.Fragment  {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private boolean isFirst = true;
    private String foodImg;
    List<String> foodName = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_ingredients, container, false);
        List<InfoDO> freshList = getInfoDOList("side");
        makeFoodList(freshList);
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.sideRecycler);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "sideDish");
        setData();

        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanInfo(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
        if(isFirst) {
            for (int i = 0; i < foodList.size(); i++) {
                foodName.add(foodList.get(i).getName());
            }
        }
    }

    private void setData(){
        // RecyclerView 에 들어갈 데이터를 추가한다.
        for(String name : foodName){
            foodImg = "file:///storage/emulated/0/Download/"+name+".jpg";
            list.add(new PencilItem(name, Uri.parse(foodImg)));
        }
        isFirst = false;
        adapter.notifyDataSetChanged();
    }
}
