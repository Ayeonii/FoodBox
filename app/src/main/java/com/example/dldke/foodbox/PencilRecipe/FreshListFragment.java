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

public class FreshListFragment extends android.support.v4.app.Fragment {
    private AllFoodListFragment allFoodListFragment = new AllFoodListFragment();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> list = new ArrayList<>();
    private String foodImg;
    List<String[]> foodName = new ArrayList<String[]>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pencilrecipe_fragment_fresh, container, false);
        foodName = allFoodListFragment.getFreshList();
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.freshRecycler);
        recyclerView.setHasFixedSize(true);
        adapter = new PencilRecyclerAdapter(list,view.getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "fresh");
        setData();

        return view;
    }

    private void setData(){
        for(int i =0 ; i<foodName.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+foodName.get(i)[0]+".jpg";
            list.add(new PencilItem(foodName.get(i)[0], Uri.parse(foodImg),foodName.get(i)[1] ));
        }
        adapter.notifyDataSetChanged();
    }
}
