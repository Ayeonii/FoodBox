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

public class EtcListFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private String foodImg;
    List<String> foodName = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etc_ingredients, container, false);
        List<InfoDO> etcList = getInfoDOList("etc");
        makeFoodList(etcList);
        Context context = view.getContext();
        recyclerView = (RecyclerView)view.findViewById(R.id.etcRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        // use a linear layout manager
        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "Etc");
        setData();

        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
        for(int i =0 ; i< foodList.size(); i++) {
            foodName.add(foodList.get(i).getName());
        }
    }

    private void setData(){
        for(String name : foodName){
            foodImg = "file:///storage/emulated/0/Download/"+name+".jpg";
            list.add(new PencilItem(name, Uri.parse(foodImg)));
        }
        adapter.notifyDataSetChanged();
    }
}
