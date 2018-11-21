package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.HalfRecipeIngreItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecyclerListener;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideDialog extends Dialog implements View.OnClickListener {

    private TextView txtType, txtEmpty, txtBackEmpty, txtCancel, txtOk;
    private LinearLayout linearLayout1, linearLayout2;
    private RecyclerView recyclerView;

    private Context context;
    private String ingreType;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<LocalRefrigeratorItem> localRefrigeratorItems = new ArrayList<>();
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();

    private InsideItemDialog itemDialog;

    public InsideDialog(@NonNull Context context, String type, boolean isEmpty) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
    }

    public InsideDialog(@NonNull Context context, String type, boolean isEmpty, ArrayList arrayList) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
        this.localRefrigeratorItems = arrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingredient_dialog);

        Log.d("test", "dialog onCreate");

        txtType = (TextView) findViewById(R.id.txt_type);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);
        txtBackEmpty = (TextView) findViewById(R.id.txt_back_empty);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (LinearLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        txtCancel.setVisibility(View.INVISIBLE);
        //txtCancel.setOnClickListener(this);
        txtBackEmpty.setOnClickListener(this);
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
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new InsideAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.addOnItemTouchListener(
                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        final String getName = localRefrigeratorItems.get(position).getName();
                        final Double getCount = localRefrigeratorItems.get(position).getCount();
                        final String getDueDate = localRefrigeratorItems.get(position).getDueDate();
                        Log.d("test", position + ", " + getName + ", " + getCount.toString() + ", " + getDueDate);
                        String foodUri = "file:///storage/emulated/0/Download/"+getName+".jpg";
                        Uri foodImgUri = Uri.parse(foodUri);
                        itemDialog = new InsideItemDialog(context, getName, getCount, getDueDate, foodImgUri  );
                        itemDialog.setDialogListener(new InsideDialogListener() {
                            @Override
                            public void onPositiveClicked(int delCheck, Double count, String dueDate) {
                                if (delCheck==1) {  
                                    localRefrigeratorItems.remove(position);
                                    setData();
                                    setRecyclerView();
                                }
                            }
                        });
                        itemDialog.show();
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
//            case R.id.txt_cancel:
//                cancel();
//                break;
            case R.id.txt_back_empty:
                cancel();
                break;
            case R.id.txt_ok:
                dismiss();
                break;
        }
    }
}
