package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.DataBaseFiles.Mapper.createIngredient;

public class HalfRecipeRecipeDialog extends Dialog implements View.OnClickListener {

    private EditText editRecipeName;
    private TextView txtEmpty, txtBack, txtBackEmpty, txtComplete;
    private LinearLayout linearLayout1, linearLayout2;
    private RecyclerView recyclerView;

    private Context context;

    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecipeItem> mItems = new ArrayList<>();

    private ArrayList<LocalRefrigeratorItem> selectedItem = new ArrayList<>();
    private HalfRecipeDialogListener dialogListener;
    private ArrayList<String> dupliArray = new ArrayList<>();

    public HalfRecipeRecipeDialog(@NonNull Context context, ArrayList arrayList, ArrayList dupliArray) {
        super(context);
        this.context = context;
        this.selectedItem = arrayList;
        this.dupliArray = dupliArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_recipe_dialog);

        editRecipeName = (EditText) findViewById(R.id.recipe_name_edit);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtBack = (TextView) findViewById(R.id.txt_back);
        txtBackEmpty = (TextView) findViewById(R.id.txt_back_empty);
        txtComplete = (TextView) findViewById(R.id.txt_complete);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (LinearLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        txtBack.setOnClickListener(this);
        txtBackEmpty.setOnClickListener(this);
        txtComplete.setOnClickListener(this);

        if (selectedItem.size()==0) {
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
        adapter = new HalfRecipeRecipeAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < selectedItem.size(); i++) {
            String foodImgUri = "file:///storage/emulated/0/Download/"+selectedItem.get(i).getName()+".jpg";
            mItems.add(new HalfRecipeRecipeItem(selectedItem.get(i).getName(), selectedItem.get(i).getCount(), Uri.parse(foodImgUri)));
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(HalfRecipeDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                cancel();
                break;
            case R.id.txt_back_empty:
                cancel();
                break;
            case R.id.txt_complete:
                String simpleName = editRecipeName.getText().toString();

                for(int i=0; i<mItems.size(); i++) {
                    //Log.d("test", mItems.get(i).getName() + ", " + mItems.get(i).getCount().toString() +  ", " + mItems.get(i).getEditCount().toString());
                }

//                    //refrigerator 테이블 접근 (재료 소진)
//                    for (int i = 0; i < mItems.size(); i++) {
//                        Mapper.updateCount(mItems.get(i).getName(), mItems.get(i).getEditCount());
//                    }
//
//                    //recipe 테이블 접근
//                    List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();
//                    for (int i = 0; i < mItems.size(); i++) {
//                        recipeIngredientList.add(createIngredient(mItems.get(i).getName(), mItems.get(i).getEditCount()));
//                    }
//                    String recipe_id = Mapper.createRecipe(recipeIngredientList);
//                    Log.d("test", recipe_id);
//
//                    //myCommunity 테이블 접근
//                    Mapper.addRecipeInMyCommunity(recipe_id);

                //recipe 테이블 접근
                List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();
                for(int i=0; i<mItems.size(); i++) {
                    recipeIngredientList.add(createIngredient(mItems.get(i).getName(), mItems.get(i).getEditCount()));
                }
                String recipe_id = Mapper.createRecipe(recipeIngredientList, simpleName);
                Log.d("test", recipe_id);

                //myCommunity 테이블 접근
                Mapper.addRecipeInMyCommunity(recipe_id);

                //Activity로 넘기기 위함 -> 나중에 수정할 수도 있음
                dialogListener.onCompleteClicked(1);
                dismiss();
                break;
        }
    }
}
