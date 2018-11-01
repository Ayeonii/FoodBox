package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.HalfRecipeRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;

import java.util.ArrayList;
import java.util.List;

public class HalfIngredientsDialog extends Dialog implements View.OnClickListener {
    private List<InfoDO> itemList;
    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecyclerItem> mItems = new ArrayList<>();

    private Context context;

    private TextView txtCategory, txtCancel, txtOk;
    private RecyclerView recyclerView;

    private String category;

    public HalfIngredientsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public HalfIngredientsDialog(@NonNull Context context, String category) {
        super(context);
        this.context = context;
        this.category = category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_ingredients_dialog);

        txtCategory = (TextView) findViewById(R.id.textCategory);
        txtCancel = (TextView) findViewById(R.id.textCancel);
        txtOk = (TextView) findViewById(R.id.textOk);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        if(category != null) {
            txtCategory.setText(category);
        }

        itemList = Mapper.scanInfo(category);
        for(int i = 0; i < itemList.size(); i++) {
            Log.d("recipe", String.format("Refri Item: %s", itemList.get(i).getName()));
        }
        if(itemList.size() != 0) {
            setRecyclerView();
        } else {
            //그 칸에 들어있는게 없으면 어떻게 처리해야할지(예를들면 반찬칸)
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);

        adapter = new HalfRecipeRecyclerAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        setData();
    }

    private void setData() {
        mItems.clear();

        for(int i = 0; i < itemList.size(); i++) {
            mItems.add(new HalfRecipeRecyclerItem(itemList.get(i).getName()));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textCancel:
                cancel();
                break;
            case R.id.textOk:
                dismiss();
                break;
        }
    }
}
