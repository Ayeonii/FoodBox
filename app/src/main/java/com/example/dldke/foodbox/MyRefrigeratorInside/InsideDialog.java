package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeIngreItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecyclerListener;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideDialog extends Dialog implements View.OnClickListener {

    private TextView txtType, txtEmpty;
    private Button btnBackEmpty, btnCancel, btnOk;
    private LinearLayout linearLayout1, linearLayout2;
    private RecyclerView recyclerView;

    private Context context;
    private String ingreType;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<LocalRefrigeratorItem> localArray = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();
    private ArrayList<DCItem> dcArray = new ArrayList<>();

    private InsideItemDialog itemDialog;


    public InsideDialog(@NonNull Context context, String type, boolean isEmpty) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
    }

    public InsideDialog(@NonNull Context context, String type, boolean isEmpty, ArrayList<LocalRefrigeratorItem> localArray, ArrayList<String> nameArray) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
        this.localArray = localArray;
        this.nameArray = nameArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingredient_dialog);

        txtType = (TextView) findViewById(R.id.txt_type);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        btnBackEmpty = (Button) findViewById(R.id.btn_back_empty);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (LinearLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        btnCancel.setVisibility(View.INVISIBLE);
        btnBackEmpty.setOnClickListener(this);
        btnOk.setOnClickListener(this);

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
        adapter = new InsideAdapter(mItems, ingreType);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.addOnItemTouchListener(
                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        showItemDialog(position);
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

    public void showItemDialog(int position) {
        //각자의 유통기한과 개수가 들어있는 배열 초기화 왜냐면 각자 배열에 들어있는게 달라야하니까!! 추가되면 안되니까!!
        dcArray.clear();

        //해당포지션의 name을 받아와서 localArray에 그 name을 가진 유통기한과 개수를 다 받아온다.
        final String name = nameArray.get(position);
        for (int i=0; i<localArray.size(); i++) {
            if (localArray.get(i).getName().equals(name)) {
                dcArray.add(new DCItem(localArray.get(i).getDueDate(), localArray.get(i).getCount()));
            }
        }

        //해당 name의 유통기한과 개수를 보여주는 다이얼로그
        itemDialog = new InsideItemDialog(context, name, dcArray);
        itemDialog.setDialogListener(new InsideDialogListener() {
            @Override
            public void onPositiveClicked(int delCheck, Double count, String dueDate) {

            }

            @Override
            public void onOkClicked(ArrayList<DCItem> dcItems) {
                //수정 또는 삭제로 인해 변경된 dcItems
                //이걸로 localArray를 업데이트해야됨 -> 삭제 후 다시 insert
//                for (int i=0; i<localArray.size(); i++) {
//                    if (localArray.get(i).getName().equals(name)) {
//                        localArray.remove(i);
//                    }
//                }
                localArray.remove(name);

                Log.d("test","잘 삭제되었는지 확인");
                for (int i=0; i<localArray.size(); i++) {
                    Log.d("test", localArray.get(i).getName());
                }

                Log.d("test", "dcItems.size() : " +dcItems.size());
                for (int i=0;i<dcItems.size(); i++) {
                    localArray.add(new LocalRefrigeratorItem(name, dcItems.get(i).getCount(), dcItems.get(i).getStrDueDate()));
                }

                setRecyclerView();
            }
        });
        itemDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_empty:
                cancel();
                break;
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }
}
