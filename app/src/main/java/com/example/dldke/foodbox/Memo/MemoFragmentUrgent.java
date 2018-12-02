package com.example.dldke.foodbox.Memo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.PencilRecipe.AllFoodListFragment;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class MemoFragmentUrgent extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<MemoUrgentItem> list = new ArrayList<>();
    private String foodImg;
    List<String[]> foodName = new ArrayList<String[]>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo_fragment_urgent, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.freshRecycler);
        recyclerView.setHasFixedSize(true);
        adapter = new MemoRecyclerAdapter(list,view.getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "fresh");
        setData();

        return view;
    }
    private void setData(){
        for(int i =0 ; i<foodName.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+foodName.get(i)[0]+".jpg";
            list.add(new MemoUrgentItem(foodName.get(i)[0], Uri.parse(foodImg),foodName.get(i)[1] ));
        }
        adapter.notifyDataSetChanged();
    }
}
