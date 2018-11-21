package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeIngreDialog extends Dialog implements View.OnClickListener {

    private TextView txtType, txtEmpty, txtBackEmpty, txtCancel, txtOk;
    private LinearLayout linearLayout1, linearLayout2;
    private RecyclerView recyclerView;

    private Context context;
    private String ingreType;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<LocalRefrigeratorItem> localArray = new ArrayList<>();
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();

    private HalfRecipeDialogListener dialogListener;
    private Boolean[] checkIngre = new Boolean[50];

    private ArrayList<String> nameArray = new ArrayList<>();

    public HalfRecipeIngreDialog(@NonNull Context context, String type, boolean isEmpty) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
    }

    public HalfRecipeIngreDialog(@NonNull Context context, String type, boolean isEmpty, ArrayList<String> arrayList, Boolean[] check) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
        this.nameArray = arrayList;
        System.arraycopy(check, 0, this.checkIngre, 0, arrayList.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingredient_dialog);

        Log.d("test", ingreType + " dialog onCreate");

        txtType = (TextView) findViewById(R.id.txt_type);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);
        txtBackEmpty = (TextView) findViewById(R.id.txt_back_empty);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (LinearLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        txtCancel.setOnClickListener(this);
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
        adapter = new HalfRecipeIngreAdapter(mItems, nameArray.size(), checkIngre);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.addOnItemTouchListener(
                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (checkIngre[position])
                            checkIngre[position] = false;
                        else
                            checkIngre[position] = true;

                        setRecyclerView();
                    }
                }
                ));

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < nameArray.size(); i++) {
            mItems.add(new HalfRecipeIngreItem(nameArray.get(i)));
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(HalfRecipeDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                cancel();
                break;
            case R.id.txt_back_empty:
                cancel();
                break;
            case R.id.txt_ok:
                dialogListener.onPositiveClicked(ingreType, checkIngre);
                dismiss();
                break;
        }
    }
}
