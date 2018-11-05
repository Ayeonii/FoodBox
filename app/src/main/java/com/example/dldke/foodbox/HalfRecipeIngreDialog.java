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

import com.example.dldke.foodbox.Adapter.HalfRecipeIngreAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeIngreDialog extends Dialog implements View.OnClickListener {
    private List<RefrigeratorDO.Item> refrigeratorItem;
    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();

    private Context context;

    private TextView txtCategory, txtCancel, txtOk;
    private RecyclerView recyclerView;

    private String type;
    private ArrayList<LocalRefrigeratorItem> localRefrigeratorItems = new ArrayList<>();

    public HalfRecipeIngreDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public HalfRecipeIngreDialog(@NonNull Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    public HalfRecipeIngreDialog(@NonNull Context context, String type, ArrayList arrayList) {
        super(context);
        this.context = context;
        this.type = type;
        this.localRefrigeratorItems = arrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingre_dialog);

        txtCategory = (TextView) findViewById(R.id.textCategory);
        txtCancel = (TextView) findViewById(R.id.textCancel);
        txtOk = (TextView) findViewById(R.id.textOk);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        switch (type) {
            case "sideDish":
                txtCategory.setText("반찬칸");
                break;
            case "dairy":
                txtCategory.setText("계란,유제품칸");
                break;
            case "etc":
                txtCategory.setText("음료,소스칸");
                break;
            case "meat":
                txtCategory.setText("육류,생선칸");
                break;
            case "fresh":
                txtCategory.setText("과일,야채칸");
                break;
        }

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);

        adapter = new HalfRecipeIngreAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        setData();
    }

    private void setData() {
        mItems.clear();
        for(int i=0; i<localRefrigeratorItems.size(); i++) {
           mItems.add(new HalfRecipeIngreItem(localRefrigeratorItems.get(i).getName()));
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
