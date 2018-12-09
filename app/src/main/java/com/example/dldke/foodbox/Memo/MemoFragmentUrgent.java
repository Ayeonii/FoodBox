package com.example.dldke.foodbox.Memo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class MemoFragmentUrgent extends android.support.v4.app.Fragment {

    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<MemoUrgentItem> list = new ArrayList<>();
    private List<RecipeDO.Ingredient> urgentList = new ArrayList<>();
    private String foodImg;
    private TextView noneText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo_fragment_urgent, container, false);

        Context context = view.getContext();
        noneText = (TextView) view.findViewById(R.id.noneText);
        recyclerView = (RecyclerView) view.findViewById(R.id.urgent_list);
        recyclerView.setHasFixedSize(true);
        adapter = new MemoUrgentRecyclerAdapter(list, view.getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setAdapter(adapter);

        urgentList = refrigeratorMainActivity.getUrgentList();

        setData();

        return view;
    }

    private void setData() {

        try {
            for (int i = 0; i < urgentList.size(); i++) {
                foodImg = "file:///storage/emulated/0/Download/" + urgentList.get(i).getIngredientName() + ".jpg";
                list.add(new MemoUrgentItem(Uri.parse(foodImg)
                        , urgentList.get(i).getIngredientName()
                        , urgentList.get(i).getIngredientDuedate()));
            }
            adapter.notifyDataSetChanged();

        } catch (NullPointerException e) {
            noneText.setVisibility(View.VISIBLE);
        }


    }
}
