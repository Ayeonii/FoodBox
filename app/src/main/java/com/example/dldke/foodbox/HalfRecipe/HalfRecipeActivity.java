package com.example.dldke.foodbox.HalfRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.dldke.foodbox.Activity.MainActivity.getPinpointManager;
import static com.example.dldke.foodbox.DataBaseFiles.Mapper.createIngredient;

public class HalfRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh, btnFrozen;
    private FloatingActionButton fbtnRecipe;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh, localFrozen;
    private ArrayList<String> nameSideDish, nameDairy, nameEtc, nameMeat, nameFresh, nameAll, nameFrozen;
    private ArrayList<String> dupliArray;

    private Boolean[] checkSideDish, checkDairy, checkEtc, checkMeat, checkFresh, checkFrozen;
    private ArrayList<LocalRefrigeratorItem> selectedItem;

    private HalfRecipeIngreDialog ingreDialog;
    private HalfRecipeRecipeDialog recipeDialog;
    private HalfRecipeDueDateDialog dueDateDialog;
    private HalfRecipeIngDialog ingDialog;

    private String user_id;
    private String recipeSimpleName;
    private static String TAG = "HalfRecipeActivity";

    // 추가재료 부분================
    private List<InfoDO> infoFreshItem, infoMeatItem, infoEtcItem;
    //==============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnDairy = (Button) findViewById(R.id.btn_dairy);
        btnEtc = (Button) findViewById(R.id.btn_etc);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFresh = (Button) findViewById(R.id.btn_fresh);
        btnFrozen = (Button) findViewById(R.id.btn_frozen);
        fbtnRecipe = (FloatingActionButton) findViewById(R.id.floatingButtonRecipe);

        btnSidedish.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFresh.setOnClickListener(this);
        btnFrozen.setOnClickListener(this);
        fbtnRecipe.setOnClickListener(this);

        scanToLocalRefrigerator();
        setDuplicateArray();
        setCheckArray();
        setInfoDOList();

        try {
            user_id = Mapper.searchMyCommunity().getUserId();
        } catch (NullPointerException e) {
            Mapper.createMyCommunity();
        }

        //create하는거 (처음에 한번만 하는거) 나중에 하나로 합칠거임!!
        //Mapper.createMemo();
    }

    public void setInfoDOList() {

        //반찬을 제외한 나머지 재료 가져와서 섹션별로 따로따로 담기
        infoFreshItem = Mapper.scanSection("fresh");
        infoMeatItem = Mapper.scanSection("meat");
        infoEtcItem = Mapper.scanSection("etc");

        nameAll = new ArrayList<>();
        int[] check;

        // fresh======================
        check = new int[infoFreshItem.size()];
        Arrays.fill(check, 0);

        for (int i = 0; i < infoFreshItem.size(); i++) {        //기본 infoDO에 있는 신선칸 재료들
            for (int j = 0; j < nameFresh.size(); j++) {        //내가 가지고 있는 신선칸 재료들
                if (infoFreshItem.get(i).getName().equals(nameFresh.get(j))) {
                    check[i] = 1;
                    break;  // j for문을 나온다
                }
            }
        }

        for (int i = 0; i < infoFreshItem.size(); i++) {
            if (check[i] == 0)
                nameAll.add(infoFreshItem.get(i).getName());
        }

        // meat======================
        check = new int[infoMeatItem.size()];
        Arrays.fill(check, 0);

        for (int i = 0; i < infoMeatItem.size(); i++) {
            for (int j = 0; j < nameMeat.size(); j++) {
                if (infoMeatItem.get(i).getName().equals(nameMeat.get(j))) {
                    check[i] = 1;
                    break;
                }
            }
        }

        for (int i = 0; i < infoMeatItem.size(); i++) {
            if (check[i] == 0)
                nameAll.add(infoMeatItem.get(i).getName());
        }

        // etc======================
        check = new int[infoEtcItem.size()];
        Arrays.fill(check, 0);

        for (int i = 0; i < infoEtcItem.size(); i++) {
            for (int j = 0; j < nameDairy.size(); j++) {
                if (infoEtcItem.get(i).getName().equals(nameDairy.get(j))) {
                    check[i] = 1;
                    break;
                }
            }
        }

        for (int i = 0; i < infoEtcItem.size(); i++) {
            for (int j = 0; j < nameEtc.size(); j++) {
                if (infoEtcItem.get(i).getName().equals(nameEtc.get(j))) {
                    check[i] = 1;
                    break;
                }
            }
        }

        for (int i = 0; i < infoEtcItem.size(); i++) {
            if (check[i] == 0)
                nameAll.add(infoEtcItem.get(i).getName());
        }
    }

    public void scanToLocalRefrigerator() {

        try {
            refrigeratorItem = Mapper.scanRefri();
        } catch (NullPointerException e) {
            Mapper.createRefrigerator();
            refrigeratorItem = Mapper.scanRefri();
        }

        localSideDish = new ArrayList<>();
        localDairy = new ArrayList<>();
        localEtc = new ArrayList<>();
        localMeat = new ArrayList<>();
        localFresh = new ArrayList<>();
        localFrozen = new ArrayList<>();

        for (int i = 0; i < refrigeratorItem.size(); i++) {

            // frozen check 안한거
            if (!refrigeratorItem.get(i).getIsFrozen()) {
                try {
                    if (refrigeratorItem.get(i).getSection().equals("sideDish") || refrigeratorItem.get(i).getKindOf().equals("frozen"))
                        localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));

                } catch (NullPointerException e) {
                    Log.d(TAG, "sideDish or frozen null: " + e.getMessage());
                }

                try {
                    if (refrigeratorItem.get(i).getKindOf().equals("dairy"))
                        localDairy.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));

                } catch (NullPointerException e) {
                    Log.d(TAG, "dairy null: " + e.getMessage());
                }

                try {
                    if (refrigeratorItem.get(i).getKindOf().equals("beverage") || refrigeratorItem.get(i).getKindOf().equals("sauce"))
                        localEtc.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));

                } catch (NullPointerException e) {
                    Log.d(TAG, "etc null: " + e.getMessage());
                }

                try {
                    if (refrigeratorItem.get(i).getSection().equals("meat"))
                        localMeat.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));

                } catch (NullPointerException e) {
                    Log.d(TAG, "meat null: " + e.getMessage());
                }

                try {
                    if (refrigeratorItem.get(i).getSection().equals("fresh"))
                        localFresh.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));

                } catch (NullPointerException e) {
                    Log.d(TAG, "fresh null: " + e.getMessage());
                }
            }

            // frozen check 한거
            else {
                localFrozen.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            }
        }
    }

    private void setDuplicateArray() {
        dupliArray = new ArrayList<>();

        nameSideDish = new ArrayList<>();
        nameDairy = new ArrayList<>();
        nameEtc = new ArrayList<>();
        nameMeat = new ArrayList<>();
        nameFresh = new ArrayList<>();
        nameFrozen = new ArrayList<>();

        for (int i = 0; i < localSideDish.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameSideDish.size(); j++) {
                if (nameSideDish.get(j).equals(localSideDish.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameSideDish.add(localSideDish.get(i).getName());
            else if (check == 1)
                dupliArray.add(localSideDish.get(i).getName());
        }

        for (int i = 0; i < localDairy.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameDairy.size(); j++) {
                if (nameDairy.get(j).equals(localDairy.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameDairy.add(localDairy.get(i).getName());
            else if (check == 1)
                dupliArray.add(localDairy.get(i).getName());
        }

        for (int i = 0; i < localEtc.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameEtc.size(); j++) {
                if (nameEtc.get(j).equals(localEtc.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameEtc.add(localEtc.get(i).getName());
            else if (check == 1)
                dupliArray.add(localEtc.get(i).getName());
        }

        for (int i = 0; i < localMeat.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameMeat.size(); j++) {
                if (nameMeat.get(j).equals(localMeat.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameMeat.add(localMeat.get(i).getName());
            else if (check == 1)
                dupliArray.add(localMeat.get(i).getName());
        }

        for (int i = 0; i < localFresh.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameFresh.size(); j++) {
                if (nameFresh.get(j).equals(localFresh.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameFresh.add(localFresh.get(i).getName());
            else if (check == 1)
                dupliArray.add(localFresh.get(i).getName());
        }

        for (int i = 0; i < localFrozen.size(); i++) {
            int check = 0;
            for (int j = 0; j < nameFrozen.size(); j++) {
                if (nameFrozen.get(j).equals(localFrozen.get(i).getName())) {
                    check = 1;
                    break;
                } else
                    check = 0;
            }

            if (check == 0)
                nameFrozen.add(localFrozen.get(i).getName());
            else if (check == 1)
                dupliArray.add(localFrozen.get(i).getName());
        }
    }

    public void setCheckArray() {
        checkSideDish = new Boolean[nameSideDish.size()];
        checkDairy = new Boolean[nameDairy.size()];
        checkEtc = new Boolean[nameEtc.size()];
        checkMeat = new Boolean[nameMeat.size()];
        checkFresh = new Boolean[nameFresh.size()];
        checkFrozen = new Boolean[nameFrozen.size()];

        for (int i = 0; i < nameSideDish.size(); i++)
            checkSideDish[i] = false;
        for (int i = 0; i < nameDairy.size(); i++)
            checkDairy[i] = false;
        for (int i = 0; i < nameEtc.size(); i++)
            checkEtc[i] = false;
        for (int i = 0; i < nameMeat.size(); i++)
            checkMeat[i] = false;
        for (int i = 0; i < nameFresh.size(); i++)
            checkFresh[i] = false;
        for (int i = 0; i < nameFrozen.size(); i++)
            checkFrozen[i] = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                showIngredientDialog(nameSideDish, checkSideDish, "sideDish");
                break;
            case R.id.btn_dairy:
                showIngredientDialog(nameDairy, checkDairy, "dairy");
                break;
            case R.id.btn_etc:
                showIngredientDialog(nameEtc, checkEtc, "etc");
                break;
            case R.id.btn_meat:
                showIngredientDialog(nameMeat, checkMeat, "meat");
                break;
            case R.id.btn_fresh:
                showIngredientDialog(nameFresh, checkFresh, "fresh");
                break;
            case R.id.btn_frozen:
                showIngredientDialog(nameFrozen, checkFrozen, "frozen");
                break;
            case R.id.floatingButtonRecipe:
                setSelectedItem();
                showRecipeDialog();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent refMain = new Intent(HalfRecipeActivity.this, RefrigeratorMainActivity.class);
        refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HalfRecipeActivity.this.startActivity(refMain);
        overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
    }

    private void setResult(String type, Boolean[] check) {
        switch (type) {
            case "sideDish":
                for (int i = 0; i < nameSideDish.size(); i++) {
                    if (check[i] != checkSideDish[i]) {
                        if (check[i])
                            checkSideDish[i] = true;
                        else
                            checkSideDish[i] = false;
                    }
                }

                break;
            case "dairy":
                for (int i = 0; i < nameDairy.size(); i++) {
                    if (check[i] != checkDairy[i]) {
                        if (check[i])
                            checkDairy[i] = true;
                        else
                            checkDairy[i] = false;
                    }
                }

                break;
            case "etc":
                for (int i = 0; i < nameEtc.size(); i++) {
                    if (check[i] != checkEtc[i]) {
                        if (check[i])
                            checkEtc[i] = true;
                        else
                            checkEtc[i] = false;
                    }
                }

                break;
            case "meat":
                for (int i = 0; i < nameMeat.size(); i++) {
                    if (check[i] != checkMeat[i]) {
                        if (check[i])
                            checkMeat[i] = true;
                        else
                            checkMeat[i] = false;
                    }
                }

                break;
            case "fresh":
                for (int i = 0; i < nameFresh.size(); i++) {
                    if (check[i] != checkFresh[i]) {
                        if (check[i])
                            checkFresh[i] = true;
                        else
                            checkFresh[i] = false;
                    }
                }

                break;
            case "frozen":
                for (int i = 0; i < nameFrozen.size(); i++) {
                    if (check[i] != checkFrozen[i]) {
                        if (check[i])
                            checkFrozen[i] = true;
                        else
                            checkFrozen[i] = false;
                    }
                }

                break;
        }
    }

    private void setSelectedItem() {
        selectedItem = new ArrayList<>();

        Double totalCount;

        for (int i = 0; i < nameSideDish.size(); i++) {
            if (checkSideDish[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localSideDish.size(); j++) {
                    if (localSideDish.get(j).getName().equals(nameSideDish.get(i))) {
                        totalCount += localSideDish.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameSideDish.get(i), totalCount));
            }
        }

        for (int i = 0; i < nameDairy.size(); i++) {
            if (checkDairy[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localDairy.size(); j++) {
                    if (localDairy.get(j).getName().equals(nameDairy.get(i))) {
                        totalCount += localDairy.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameDairy.get(i), totalCount));
            }
        }

        for (int i = 0; i < nameEtc.size(); i++) {
            if (checkEtc[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localEtc.size(); j++) {
                    if (localEtc.get(j).getName().equals(nameEtc.get(i))) {
                        totalCount += localEtc.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameEtc.get(i), totalCount));
            }
        }

        for (int i = 0; i < nameMeat.size(); i++) {
            if (checkMeat[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localMeat.size(); j++) {
                    if (localMeat.get(j).getName().equals(nameMeat.get(i))) {
                        totalCount += localMeat.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameMeat.get(i), totalCount));
            }
        }

        for (int i = 0; i < nameFresh.size(); i++) {
            if (checkFresh[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localFresh.size(); j++) {
                    if (localFresh.get(j).getName().equals(nameFresh.get(i))) {
                        totalCount += localFresh.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameFresh.get(i), totalCount));
            }
        }

        for (int i = 0; i < nameFrozen.size(); i++) {
            if (checkFrozen[i]) {
                totalCount = 0.0;
                for (int j = 0; j < localFrozen.size(); j++) {
                    if (localFrozen.get(j).getName().equals(nameFrozen.get(i))) {
                        totalCount += localFrozen.get(j).getCount();
                    }
                }
                selectedItem.add(new LocalRefrigeratorItem(nameFrozen.get(i), totalCount));
            }
        }
    }

    public void showIngredientDialog(ArrayList<String> nameArray, Boolean[] checkArray, String type) {
        if (nameArray.size() == 0)
            ingreDialog = new HalfRecipeIngreDialog(this, type, true);
        else
            ingreDialog = new HalfRecipeIngreDialog(this, type, false, nameArray, checkArray);

        ingreDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) {
                setResult(type, check);
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
        ingreDialog.show();
    }

    public void showRecipeDialog() {
        recipeDialog = new HalfRecipeRecipeDialog(this, selectedItem, dupliArray, nameAll);
        recipeDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) {

            }

            @Override
            public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {
                recipeSimpleName = recipeName;

                // result = 1 : 유통기한이 여러개인게 없거나 있어도 보유개수 모두 사용했을때
                // result = 2 : 유통기한이 여러개인게 있고 보유개수 보다 적게 사용했을때
                //              그리고 그러한 재료의 명단(dueDateCheckArray)도 같이 보냄
                // result = 3 : 냉장고에 없는 추가재료가 하나라도 껴있으면 완료Activity로 가지 않을 거임

                if (result == 1 || result == 2) {
                    registerHalfRecipe(mItems);
                } else if (result == 3) {
                    goIngHalfRecipeMaking(mItems);
                }
            }

            @Override
            public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> mItems) {

            }

            @Override
            public void onIngOkClicked(int ok) {

            }
        });
        recipeDialog.setCancelable(false);
        recipeDialog.show();
    }

    public void showRecipeIngDialog(List<RecipeDO.Ingredient> needItem) {
        ingDialog = new HalfRecipeIngDialog(this, needItem);
        ingDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) {

            }

            @Override
            public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {

            }

            @Override
            public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> mItems) {

            }

            @Override
            public void onIngOkClicked(int ok) {
                if (ok == 1) {
                    Intent myRecipeActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                    myRecipeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myRecipeActivity);
                }
            }
        });
        ingDialog.setCancelable(false);
        ingDialog.show();
    }

    public void registerHalfRecipe(ArrayList<HalfRecipeRecipeItem> mItems) {
        //recipe 테이블 접근
        List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            recipeIngredientList.add(createIngredient(mItems.get(i).getName(), mItems.get(i).getEditCount()));
        }

        String recipe_id = Mapper.createRecipe(recipeIngredientList, recipeSimpleName);

        Mapper.addRecipeInMyCommunity(recipe_id);

        PinpointManager tmp = getPinpointManager(getApplicationContext());
        Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());

        Intent halfRecipeCompleteActivity = new Intent(getApplicationContext(), HalfRecipeCompleteActivity.class);
        halfRecipeCompleteActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        halfRecipeCompleteActivity.putExtra("complete", 0);
        startActivity(halfRecipeCompleteActivity);
    }

    private void goIngHalfRecipeMaking(ArrayList<HalfRecipeRecipeItem> mItems) {
        List<RecipeDO.Ingredient> needItem = new ArrayList<>();

        for (int i = 0; i < mItems.size(); i++) {

            if (mItems.get(i).getEditCount() - mItems.get(i).getCount() > 0) {
                RecipeDO.Ingredient setlist = new RecipeDO.Ingredient();

                setlist.setIngredientName(mItems.get(i).getName());
                setlist.setIngredientCount(mItems.get(i).getEditCount() - mItems.get(i).getCount());

                needItem.add(setlist);
            }
        }

        // 필요한 재료를 담은 array : needItem

        //recipe 테이블 접근
        List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            recipeIngredientList.add(createIngredient(mItems.get(i).getName(), mItems.get(i).getEditCount()));
        }
        final String recipe_id = Mapper.createRecipe(recipeIngredientList, recipeSimpleName);

        Mapper.updateIngInfo(1, recipe_id);

        Mapper.addRecipeInMyCommunity(recipe_id);

        // memo table
        Mapper.appendToBuyMemo(needItem);

        PinpointManager tmp = getPinpointManager(getApplicationContext());
        Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());

        //사용자에게 필요한재료 확인다이얼로그
        showRecipeIngDialog(needItem);
    }
}