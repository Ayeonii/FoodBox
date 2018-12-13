package com.example.dldke.foodbox.PencilRecipe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFoodListFragment extends android.support.v4.app.Fragment {
    private PencilRecipeActivity pencil = new PencilRecipeActivity();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static ArrayList<PencilItem> allfoodListInfo = new ArrayList<>();
    private static List<InfoDO> freshList, meatList, etcList;
    private String foodImg;
    private boolean isFrozen;

    public AllFoodListFragment(){}

    public List<PencilItem> getAllFoodListWithFrozen(){
        return allfoodListInfo;
    }

    public List<PencilItem> getMeatList(){ return makeFoodListString(meatList); }
    public List<PencilItem> getFreshList(){ return makeFoodListString(freshList); }
    public List<PencilItem> getEtcList(){ return makeFoodListString(etcList); }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pencilrecipe_fragment_all, container, false);


        //default 이미지 다운
        File defaultFile = new File("/storage/emulated/0/Download/" + "default" + ".jpg");
        if (!defaultFile.exists()) {
            Mapper.downLoadImage("default", "/storage/emulated/0/Download/", "sideDish");
        }
        
        if(pencil.getEnterTime() == 0) {
            freshList = getInfoDOList("fresh");
            meatList = getInfoDOList("meat");
            etcList = getInfoDOList("etc");

            makeFoodList(freshList);
            makeFoodList(meatList);
            makeFoodList(etcList);

            pencil.setEnterTime(1);
        }
        Context context = view.getContext();
        recyclerView = (RecyclerView)view.findViewById(R.id.allRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        adapter = new PencilRecyclerAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);

        setData();

        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {
            for(int i =0 ; i< foodList.size(); i++) {
                if(foodList.get(i).getKindOf() == "frozen"){
                    isFrozen = true;
                }else if(foodList.get(i).getKindOf() == null){
                    isFrozen = false;
                }
                else
                    isFrozen = false;
                allfoodListInfo.add(new PencilItem(foodList.get(i).getName(), foodList.get(i).getSection(), isFrozen));
                /**********이미지 추가후 주석 삭제**********/

                File file = new File("/storage/emulated/0/Download/" + foodList.get(i).getName() + ".jpg");
                if (!file.exists()) {
                    Mapper.downLoadImage(foodList.get(i).getName(), "/storage/emulated/0/Download/", foodList.get(i).getSection());
                }
            }
    }

    private List<PencilItem> makeFoodListString(List<InfoDO> foodList){
        List<PencilItem> foodListString = new ArrayList<>();
        for(int i =0 ; i< foodList.size(); i++) {
            if(foodList.get(i).getKindOf() == "frozen")
                isFrozen = true;
            else
                isFrozen = false;
            foodListString.add(new PencilItem(foodList.get(i).getName(), foodList.get(i).getSection(), isFrozen));
        }
        return foodListString;
    }

    private void setData(){
        for(int i =0 ; i<allfoodListInfo.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+allfoodListInfo.get(i).getFoodName()+".jpg";
            list.add(new PencilItem(allfoodListInfo.get(i).getFoodName(), Uri.parse(foodImg),allfoodListInfo.get(i).getFoodSection(), allfoodListInfo.get(i).getIsFrozen()));
        }
        adapter.notifyDataSetChanged();
    }
}