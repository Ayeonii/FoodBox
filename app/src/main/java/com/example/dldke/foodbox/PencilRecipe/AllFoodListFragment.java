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
    private static List<String[]> allfoodList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static List<InfoDO> freshList, meatList, etcList;
    private String foodImg;

    public AllFoodListFragment(){}

    public List<String[]> getAllFoodList(){
        return allfoodList;
    }
    public List<String[]> getMeatList(){ return makeFoodListString(meatList, "meat"); }
    public List<String[]> getFreshList(){ return makeFoodListString(freshList,"fresh"); }
    public List<String[]> getEtcList(){ return makeFoodListString(etcList,"etc"); }


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

            makeFoodList(freshList, "fresh");
            makeFoodList(meatList,"meat");
            makeFoodList(etcList,"etc");

            pencil.setEnterTime(1);
        }
        Context context = view.getContext();
        recyclerView = (RecyclerView)view.findViewById(R.id.allRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        adapter = new PencilRecyclerAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "All");
        setData();

        return view;
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList, String section) {
            for(int i =0 ; i< foodList.size(); i++) {
                allfoodList.add(new String[]{foodList.get(i).getName(), section});
                /**********이미지 추가후 주석 삭제**********/

                File file = new File("/storage/emulated/0/Download/" + foodList.get(i).getName() + ".jpg");
                if (!file.exists()) {
                    Mapper.downLoadImage(foodList.get(i).getName(), "/storage/emulated/0/Download/", section);
                }
            }
    }

    private List<String[]> makeFoodListString(List<InfoDO> foodList, String section){
        List<String[]> foodListString = new ArrayList<String[]>();
        for(int i =0 ; i< foodList.size(); i++) {
            foodListString.add(new String[]{foodList.get(i).getName(), section});
        }
        return foodListString;
    }
    private void setData(){
        for(int i =0 ; i<allfoodList.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+allfoodList.get(i)[0]+".jpg";
            list.add(new PencilItem(allfoodList.get(i)[0], Uri.parse(foodImg),allfoodList.get(i)[1], false));
        }
        adapter.notifyDataSetChanged();
    }
}