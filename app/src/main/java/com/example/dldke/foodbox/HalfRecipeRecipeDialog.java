package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.HalfRecipeIngreAdapter;
import com.example.dldke.foodbox.Adapter.HalfRecipeRecipeAdapter;

import java.util.ArrayList;

public class HalfRecipeRecipeDialog extends Dialog implements View.OnClickListener {

    private TextView txtEmpty, txtBack, txtComplete;
    private RecyclerView recyclerView;

    private Context context;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecipeItem> mItems = new ArrayList<>();

    public HalfRecipeRecipeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_recipe_dialog);

        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtBack = (TextView) findViewById(R.id.txt_back);
        txtComplete = (TextView) findViewById(R.id.txt_complete);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtBack.setOnClickListener(this);
        txtComplete.setOnClickListener(this);

        //true : txtEmpty, false : recyclerView
//        if (isEmpty) {
//            recyclerView.setVisibility(View.GONE);
//            txtEmpty.setVisibility(View.VISIBLE);
//        } else {
//            txtEmpty.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            setRecyclerView();
//        }

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeRecipeAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

//        recyclerView.addOnItemTouchListener(
//                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Log.d("test", position + ", " + localRefrigeratorItems.get(position).getName() + ", " + localRefrigeratorItems.get(position).getCount().toString());
//                    }
//                }
//                ));

        setData();
    }

    private void setData() {
        mItems.clear();

        mItems.add(new HalfRecipeRecipeItem("감자", 5));
        mItems.add(new HalfRecipeRecipeItem("양파", 3));
        mItems.add(new HalfRecipeRecipeItem("감자", 5));
        mItems.add(new HalfRecipeRecipeItem("양파", 3));
        mItems.add(new HalfRecipeRecipeItem("감자", 5));
        mItems.add(new HalfRecipeRecipeItem("양파", 3));

//        for (int i = 0; i < localRefrigeratorItems.size(); i++) {
//            mItems.add(new HalfRecipeIngreItem(localRefrigeratorItems.get(i).getName()));
//        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                cancel();
                break;
            case R.id.txt_complete:
                dismiss();
                break;
        }
    }
}
