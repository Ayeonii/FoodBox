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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFoodListFragment extends android.support.v4.app.Fragment {
    static public List<String> allfoodList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private List<InfoDO> freshList, meatList, etcList;
    private boolean isFirst = true;
    private String foodImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_ingredients, container, false);

        freshList = getInfoDOList("fresh");
        meatList = getInfoDOList("meat");
        etcList = getInfoDOList("etc");
        makeFoodList(freshList);
        /****meat랑 etc에 이미지 경로 추가 되면 주석 풀어주기*****/
        //makeFoodList(meatList);
        //makeFoodList(etcList);
        Context context = view.getContext();
        recyclerView = (RecyclerView)view.findViewById(R.id.allRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "All");
        setData();

        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
        for(int i =0 ; i< foodList.size(); i++) {
            if(isFirst) {
                allfoodList.add(foodList.get(i).getName());
                File file = new File("/storage/emulated/0/Download/"+foodList.get(i).getName()+".jpg");
                if(!file.exists()) {
                    Log.e("들어옴",""+file.getAbsolutePath());
                    //이미지 저장.
                    Mapper.downLoadImage(foodList.get(i).getName(), "/storage/emulated/0/Download/");
                }
            }
        }
    }

    private void setData(){
        // RecyclerView 에 들어갈 데이터를 추가한다.
        for(String name : allfoodList){
            foodImg = "file:///storage/emulated/0/Download/"+name+".jpg";
            list.add(new PencilItem(name, Uri.parse(foodImg)));
        }
        isFirst = false;
        // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행한다.
        adapter.notifyDataSetChanged();
    }
}