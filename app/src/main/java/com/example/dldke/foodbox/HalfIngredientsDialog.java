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

public class HalfIngredientsDialog extends Dialog implements View.OnClickListener {
    private String[] names = {"Charlie","Andrew","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecyclerItem> mItems = new ArrayList<>();

    private Context context;

    private TextView txtCategory, txtCancel, txtOk;
    private RecyclerView recyclerView;

    private String category, content;

    public HalfIngredientsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public HalfIngredientsDialog(@NonNull Context context, String category, String content) {
        super(context);
        this.context = context;
        this.category = category;
        this.content = content; //recyclerview로 대체할 예정 지금은 넘겨 받기만 하고 아무것도 하지 않는다.
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
        if(content != null) {
//            List<InfoDO> itemList = Mapper.scanInfo(category);
//            for(int i = 0; i < itemList.size(); i++)
//            {
//                Log.d("recipe", String.format("Refri Item: %s", itemList.get(i).getName()));
//            }
            //txtContent.setText(content);
            setRecyclerView();
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

        for(String name : names) {
            mItems.add(new HalfRecipeRecyclerItem(name));
            mItems.add(new HalfRecipeRecyclerItem(name));
            mItems.add(new HalfRecipeRecyclerItem(name));
            mItems.add(new HalfRecipeRecyclerItem(name));
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
