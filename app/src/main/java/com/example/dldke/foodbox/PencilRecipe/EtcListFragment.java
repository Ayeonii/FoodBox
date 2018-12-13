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

import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class EtcListFragment extends android.support.v4.app.Fragment {
    private AllFoodListFragment allFoodListFragment = new AllFoodListFragment();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private String foodImg;
    List<PencilItem> foodName = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pencilrecipe_fragment_etc, container, false);
        foodName = allFoodListFragment.getEtcList();
        Context context = view.getContext();
        recyclerView = (RecyclerView)view.findViewById(R.id.etcRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        adapter = new PencilRecyclerAdapter(list,view.getContext());
        recyclerView.setAdapter(adapter);

        setData();

        return view;
    }

    private void setData(){
        for(int i =0 ; i<foodName.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+foodName.get(i).getFoodName()+".jpg";
            list.add(new PencilItem(foodName.get(i).getFoodName(), Uri.parse(foodImg),foodName.get(i).getFoodSection(), foodName.get(i).getIsFrozen()));
        }
        adapter.notifyDataSetChanged();
    }
}
