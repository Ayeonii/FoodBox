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

    private TextView txtType, txtEmpty, txtCancel, txtOk;
    private RecyclerView recyclerView;

    private Context context;
    private String ingreType;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<LocalRefrigeratorItem> localRefrigeratorItems = new ArrayList<>();
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();

    public HalfRecipeIngreDialog(@NonNull Context context, String type, boolean isEmpty) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
    }

    public HalfRecipeIngreDialog(@NonNull Context context, String type, boolean isEmpty, ArrayList arrayList) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
        this.localRefrigeratorItems = arrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingre_dialog);

        txtType = (TextView) findViewById(R.id.txt_type);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        switch (ingreType) {
            case "sideDish":
                txtType.setText("반찬칸");
                break;
            case "dairy":
                txtType.setText("계란,유제품칸");
                break;
            case "etc":
                txtType.setText("음료,소스칸");
                break;
            case "meat":
                txtType.setText("육류,생선칸");
                break;
            case "fresh":
                txtType.setText("과일,야채칸");
                break;
        }

        //true : txtEmpty, false : recyclerView
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeIngreAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        recyclerView.addOnItemTouchListener(
                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("test", position + ", " + localRefrigeratorItems.get(position).getName() + ", " + localRefrigeratorItems.get(position).getCount().toString());
                    }
                }
                ));

        setData();
    }

    private void setData() {
        mItems.clear();
        for (int i = 0; i < localRefrigeratorItems.size(); i++) {
            mItems.add(new HalfRecipeIngreItem(localRefrigeratorItems.get(i).getName()));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                cancel();
                break;
            case R.id.txt_ok:
                dismiss();
                break;
        }
    }
}
