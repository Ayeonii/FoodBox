package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class HalfRecipeAddmoreDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private RecyclerView recyclerView;
    private Button btnCancel, btnOk;

//    private RecyclerView.Adapter adapter;
    private HalfRecipeAddmoreAdapter adapter;
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();
    private ArrayList<String> nameAll = new ArrayList<>();

    private HalfRecipeDialogListener dialogListener;
    private Boolean[] checkAddFood;

    public HalfRecipeAddmoreDialog(@NonNull Context context, ArrayList<String> nameAll, Boolean[] check) {
        super(context);
        this.context = context;
        this.nameAll = nameAll;
        checkAddFood = new Boolean[nameAll.size()];
        System.arraycopy(check, 0, this.checkAddFood, 0, nameAll.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_addmore_dialog);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeAddmoreAdapter(mItems, nameAll.size(), checkAddFood);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
//        recyclerView.addOnItemTouchListener(
//                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (checkAddFood[position])
//                            checkAddFood[position] = false;
//                        else
//                            checkAddFood[position] = true;
//
//                        //setRecyclerView();
//                    }
//                }
//                ));

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < nameAll.size(); i++) {
            mItems.add(new HalfRecipeIngreItem(nameAll.get(i)));
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(HalfRecipeDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_ok:
                Log.d("test", "원래 추가재료 다이얼로그에 있는 체크배열!!");
                for (int i=0;i<nameAll.size();i++) {
                    if (checkAddFood[i])
                        Log.d("test", "checkAddFood["+i+"] is true");
                }
                Log.d("test", "adapter에서 넘어온 체크배열!!");
                checkAddFood = adapter.getCheckAddFood();
                for (int i=0;i<nameAll.size();i++) {
                    if (checkAddFood[i])
                        Log.d("test", "checkAddFood["+i+"] is true");
                }
                dialogListener.onPositiveClicked("all", adapter.getCheckAddFood());
                dismiss();
                break;
        }
    }
}
