package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeIngDialog extends Dialog implements View.OnClickListener {

    private Button btnOk;
    private RecyclerView recyclerView;

    private Context context;

    private RecyclerView.Adapter adapter;
    private HalfRecipeDialogListener dialogListener;
    private ArrayList<HalfRecipeRecipeItem> mItems = new ArrayList<>();
    private List<RecipeDO.Ingredient> needArray = new ArrayList<>();

    public HalfRecipeIngDialog(@NonNull Context context, List<RecipeDO.Ingredient> needItem) {
        super(context);
        this.context = context;
        this.needArray = needItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ing_dialog);

        btnOk = (Button) findViewById(R.id.btn_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnOk.setOnClickListener(this);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeIngAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < needArray.size(); i++) {
            String foodImgUri = "file://"+context.getFilesDir()+needArray.get(i).getIngredientName()+".jpg";

            mItems.add(new HalfRecipeRecipeItem(1, needArray.get(i).getIngredientName(), needArray.get(i).getIngredientCount(), Uri.parse(foodImgUri)));
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(HalfRecipeDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:   // 확인을 누르면 레시피함으로 간다.
                dialogListener.onIngOkClicked(1);
                dismiss();
                break;
        }
    }
}