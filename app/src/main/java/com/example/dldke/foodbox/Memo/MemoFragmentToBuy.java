package com.example.dldke.foodbox.Memo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class MemoFragmentToBuy extends android.support.v4.app.Fragment {

    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();

    private Context context;
    private TextView noneText;
    private RecyclerView recyclerView;

    private ArrayList<MemoToBuyItem> list = new ArrayList<>();
    private List<RecipeDO.Ingredient> tobuyList = new ArrayList<>();

    private RecyclerView.Adapter adapter;

    private String foodImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memo_fragment_tobuy, container, false);

        context = view.getContext();

        noneText = (TextView) view.findViewById(R.id.noneText);
        recyclerView = (RecyclerView) view.findViewById(R.id.tobuy_list);

        tobuyList = refrigeratorMainActivity.getTobuyList();

        Log.e("test", "프레그먼트에서 장보기 목록을 잘 가져오는지 확인");
        for (int i=0; i<tobuyList.size(); i++)
            Log.d("test", tobuyList.get(i).getIngredientName() + ", " + tobuyList.get(i).getIngredientCount());

        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new MemoTobuyRecyclerAdapter(list, context);
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        recyclerView.setAdapter(adapter);

        setData();
    }

    private void setData() {
        try {
            for (int i = 0; i < tobuyList.size(); i++) {
                foodImg = "file:///storage/emulated/0/Download/" + tobuyList.get(i).getIngredientName() + ".jpg";
                list.add(new MemoToBuyItem(Uri.parse(foodImg)
                        , tobuyList.get(i).getIngredientName()
                        , tobuyList.get(i).getIngredientCount()));
            }

            adapter.notifyDataSetChanged();
        } catch (NullPointerException e){
            noneText.setVisibility(View.VISIBLE);
        }
    }
}
