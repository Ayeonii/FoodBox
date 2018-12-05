package com.example.dldke.foodbox.HalfRecipe;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;

public class HalfRecipeRecipeDialog extends Dialog implements View.OnClickListener {

    private TextView txtEmpty;
    private Button btnBack, btnBackEmpty, btnComplete, btnAddMore;
    private EditText editRecipeName;
    private LinearLayout linearLayout2;
    private ConstraintLayout linearLayout1;
    private RecyclerView recyclerView;

    private Context context;

    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecipeItem> mItems = new ArrayList<>();

    private ArrayList<LocalRefrigeratorItem> selectedItem = new ArrayList<>();
    private HalfRecipeDialogListener dialogListener;
    private ArrayList<String> dupliArray = new ArrayList<>();
    private ArrayList<String> nameAll = new ArrayList<>();

    private HalfRecipeAddmoreDialog addmoreDialog;

    private Boolean[] checkAddFood;
    private int count = 0;

    public HalfRecipeRecipeDialog(@NonNull Context context, ArrayList arrayList, ArrayList dupliArray, ArrayList nameAll) {
        super(context);
        this.context = context;
        this.selectedItem = arrayList;
        this.dupliArray = dupliArray;
        this.nameAll = nameAll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_recipe_dialog);

        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        btnBack = (Button) findViewById(R.id.btn_cancel);
        btnBackEmpty = (Button) findViewById(R.id.btn_back_empty);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnAddMore = (Button) findViewById(R.id.btn_addmore);
        editRecipeName = (EditText) findViewById(R.id.recipe_name_edit);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (ConstraintLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        btnBack.setOnClickListener(this);
        btnBackEmpty.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);

        setCheckArray();

        if (selectedItem.size()==0) {
            editRecipeName.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        } else {
            editRecipeName.setVisibility(View.VISIBLE);
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
            if(!file.exists())
                foodImgUri = "file:///storage/emulated/0/Download/default.jpg";
            else
                foodImgUri = "file:///storage/emulated/0/Download/"+selectedItem.get(i).getName()+".jpg";

            mItems.add(new HalfRecipeRecipeItem(selectedItem.get(i).getName(), selectedItem.get(i).getCount(), Uri.parse(foodImgUri)));
        }

        if (count != 0) {
            for (int i = 0; i < nameAll.size(); i++) {
                if (checkAddFood[i]) {
                    String foodImgUri = "file:///storage/emulated/0/Download/" + nameAll.get(i) + ".jpg";
                    mItems.add(new HalfRecipeRecipeItem(nameAll.get(i), 0.0, Uri.parse(foodImgUri)));
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void setDialogListener(HalfRecipeDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void setCheckArray() {
        checkAddFood = new Boolean[nameAll.size()];

        for (int i=0; i<nameAll.size(); i++)
            checkAddFood[i] = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                cancel();
                break;
            case R.id.btn_back_empty:
                cancel();
                break;
            case R.id.btn_addmore:
                Log.d("test", "추가재료 버튼 클릭 직후!!");
                for (int i=0;i<nameAll.size();i++) {
                    if (checkAddFood[i])
                        Log.d("test", "checkAddFood["+i+"] is true");
                }
                // 추가재료 부분
                addmoreDialog = new HalfRecipeAddmoreDialog(context, nameAll, checkAddFood);
                addmoreDialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        for (int i = 0; i < nameAll.size(); i++) {
                            if (check[i] != checkAddFood[i]) {
                                if (check[i])
                                    checkAddFood[i] = true;
                                else
                                    checkAddFood[i] = false;
                            }
                        }

                        Log.d("test", "체크배열 받아오고 다이얼로그 객체 생성 후!!");
                        for (int i=0;i<nameAll.size();i++) {
                            if (checkAddFood[i])
                                Log.d("test", "checkAddFood["+i+"] is true");
                        }

                        for (int i=0; i<nameAll.size(); i++) {
                            if (checkAddFood[i]) {
                                Log.d("test", nameAll.get(i));
                                count++;
                                setRecyclerView();
                            }
                        }
                    }

                    @Override
                    public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {

                    }

                    @Override
                    public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> mItems) {

                    }

                    @Override
                    public void onIngOkClicked(int ok) {

                    }
                });
                addmoreDialog.show();
                break;
            case R.id.btn_complete:
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

                // 추가재로가 하나라도 껴있으면 result = 3
                for (int i=0; i<mItems.size(); i++)
                    if ( mItems.get(i).getEditCount() - mItems.get(i).getCount() > 0 )
                        result = 3;

                String simpleName = editRecipeName.getText().toString();
                if (simpleName.equals(""))
                    Toast.makeText(context, "레시피 이름을 입력하세요!", Toast.LENGTH_LONG).show();
                else {
                    // result = 1 : 유통기한이 여러개인게 없거나 있어도 보유개수 모두 사용했을때
                    // result = 2 : 유통기한이 여러개인게 있고 보유개수 보다 적게 사용했을때
                    //              그리고 그러한 재료의 명단(dueDateCheckArray)도 같이 보냄
                    // result = 3 : 냉장고에 없는 추가재료가 하나라도 껴있으면 완료Acitivy로 가지 않을 거임
                    Log.d("test", "result : " + result);
                    dialogListener.onCompleteClicked(result, simpleName, mItems, dueDateCheckArray);
                    dismiss();
                }
                break;
        }
    }
}
