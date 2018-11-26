package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.dldke.foodbox.R;
import java.util.ArrayList;


public class HalfRecipeDueDateDialog extends Dialog implements View.OnClickListener {

    private TextView txtCancel, txtOk;
    private RecyclerView recyclerView;

    private Context context;

    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeDueDateItem> mItems = new ArrayList<>();
    private HalfRecipeDialogListener dialogListener;
    private ArrayList<String> dupliArray = new ArrayList<>();

    public HalfRecipeDueDateDialog(@NonNull Context context, ArrayList<String> dupliArray) {
        super(context);
        this.context = context;
        this.dupliArray = dupliArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_duedate_dialog);

        txtCancel = (TextView) findViewById(R.id.txt_cancel);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeDueDateAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < dupliArray.size(); i++) {
            mItems.add(new HalfRecipeDueDateItem(dupliArray.get(i)));
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
            case R.id.txt_ok:
                dialogListener.onDueDateOKClicked(mItems);
                //dismiss();
                break;
        }
    }
}
