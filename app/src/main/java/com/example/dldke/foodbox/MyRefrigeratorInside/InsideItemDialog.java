package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideItemDialog extends Dialog implements View.OnClickListener {

    private TextView txtName;
    private Button btnOk;
    private RecyclerView recyclerView;

    private Context context;
    private String foodName;

    //private RecyclerView.Adapter adapter;
    private InsideItemAdapter adapter;
    private InsideDialogListener dialogListener;
    private ArrayList<DCItem> dcArray = new ArrayList<>();
    private ArrayList<DCItem> mItems = new ArrayList<>();

    public InsideItemDialog(@NonNull Context context, String name, ArrayList<DCItem> dcArray) {
        super(context);
        this.context = context;
        this.foodName = name;
        this.dcArray = dcArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refrigeratorinside_item_dialog);

        txtName = (TextView) findViewById(R.id.txt_name);
        btnOk = (Button) findViewById(R.id.btn_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnOk.setOnClickListener(this);
        txtName.setText(foodName);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new InsideItemAdapter(context, mItems, foodName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < dcArray.size(); i++) {
            mItems.add(new DCItem(dcArray.get(i).getStrDueDate(), dcArray.get(i).getCount()));
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(InsideDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:

                dcArray = adapter.getDcArray();
                dialogListener.onOkClicked(dcArray);
                dismiss();
                break;
        }
    }
}
