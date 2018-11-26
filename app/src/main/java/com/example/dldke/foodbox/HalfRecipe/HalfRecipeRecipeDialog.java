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
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;

public class HalfRecipeRecipeDialog extends Dialog implements View.OnClickListener {

    private TextView txtEmpty, txtBack, txtBackEmpty, txtComplete;
    private EditText editRecipeName;
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

        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        txtBack = (TextView) findViewById(R.id.txt_back);
        txtBackEmpty = (TextView) findViewById(R.id.txt_back_empty);
        txtComplete = (TextView) findViewById(R.id.txt_complete);
        editRecipeName = (EditText) findViewById(R.id.recipe_name_edit);
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
            String foodImgUri ;
            File file = new File("/storage/emulated/0/Download/" + selectedItem.get(i).getName() + ".jpg");

            if(!file.exists()){
                foodImgUri = "file:///storage/emulated/0/Download/default.jpg";
            }
            else{
                foodImgUri = "file:///storage/emulated/0/Download/"+selectedItem.get(i).getName()+".jpg";
            }
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
                int result = 1;
                ArrayList<String> dueDateCheckArray = new ArrayList<>();
                for (int i=0; i<mItems.size(); i++) {
                    for(int j=0; j<dupliArray.size(); j++) {
                        if (mItems.get(i).getName().equals(dupliArray.get(j))) {    //중복명단에 있는 아이템이니?
                            if (mItems.get(i).getEditCount() < mItems.get(i).getCount()) {  //그게 보유개수보다 적게 사용한다햇니?
                                result = 2;
                                dueDateCheckArray.add(mItems.get(i).getName());
                            }
                        }
                    }
                }

                String simpleName = editRecipeName.getText().toString();

                // result = 1 : 유통기한이 여러개인게 없거나 있어도 보유개수 모두 사용했을때
                // result = 2 : 유통기한이 여러개인게 있고 보유개수 보다 적게 사용했을때
                //              그리고 그러한 재료의 명단(dueDateCheckArray)도 같이 보냄
                dialogListener.onCompleteClicked(result, simpleName, mItems, dueDateCheckArray);
                dismiss();
                break;
        }
    }
}
