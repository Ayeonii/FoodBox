package com.example.dldke.foodbox.HalfRecipe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;

import static com.example.dldke.foodbox.DataBaseFiles.Mapper.createIngredient;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HalfRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh;
    private FloatingActionButton fbtnRecipe;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;
    private ArrayList<String> nameSideDish, nameDairy, nameEtc, nameMeat, nameFresh;
    private ArrayList<String> dupliArray;

    private Boolean[] checkSideDish, checkDairy, checkEtc, checkMeat, checkFresh;
    private ArrayList<LocalRefrigeratorItem> selectedItem;

    private HalfRecipeIngreDialog ingreDialog;
    private HalfRecipeRecipeDialog recipeDialog;
    private HalfRecipeDueDateDialog dueDateDialog;

    private String user_id;
    private String recipeSimpleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnDairy = (Button) findViewById(R.id.btn_dairy);
        btnEtc = (Button) findViewById(R.id.btn_etc);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFresh = (Button) findViewById(R.id.btn_fresh);
        fbtnRecipe = (FloatingActionButton) findViewById(R.id.floatingButtonRecipe);

        btnSidedish.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFresh.setOnClickListener(this);
        fbtnRecipe.setOnClickListener(this);

        scanToLocalRefrigerator();
        setDuplicateArray();
        setCheckArray();

        try {
            user_id = Mapper.searchMyCommunity().getUserId();
        } catch (NullPointerException e) {
            Mapper.createMyCommunity();
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

        Log.d("test", "refrigeratorItem.size : " + refrigeratorItem.size());
        for (int i = 0; i < refrigeratorItem.size(); i++) {
            try {
                if (refrigeratorItem.get(i).getSection().equals("sideDish"))
                    localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            } catch (NullPointerException e) {
                Log.d("test", "sideDish null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getKindOf().equals("dairy"))
                    localDairy.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            } catch (NullPointerException e) {
                Log.d("test", "dairy null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getKindOf().equals("beverage") || refrigeratorItem.get(i).getKindOf().equals("sauce"))
                    localEtc.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            } catch (NullPointerException e) {
                Log.d("test", "etc null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getSection().equals("meat"))
                    localMeat.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            } catch (NullPointerException e) {
                Log.d("test", "meat null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getSection().equals("fresh"))
                    localFresh.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
            } catch (NullPointerException e) {
                Log.d("test", "fresh null: " + e.getMessage());
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
    }

    public void setCheckArray() {
        checkSideDish = new Boolean[nameSideDish.size()];
        checkDairy = new Boolean[nameDairy.size()];
        checkEtc = new Boolean[nameEtc.size()];
        checkMeat = new Boolean[nameMeat.size()];
        checkFresh = new Boolean[nameFresh.size()];

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
            case R.id.floatingButtonRecipe:
                setSelectedItem();
                showRecipeDialog();

                break;
        }
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
        });
        ingreDialog.show();
    }

    public void showRecipeDialog() {
        recipeDialog = new HalfRecipeRecipeDialog(this, selectedItem, dupliArray);
        recipeDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) {

            }

            @Override
            public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {
                recipeSimpleName = recipeName;

                if (result == 1) {
                    goHalfRecipeMaking(mItems, dueDateCheckArray);
                } else if (result == 2) {
                    showDueDateDialog(mItems, dueDateCheckArray);
                }
            }

            @Override
            public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> mItems) {

            }
        });
        recipeDialog.setCancelable(false);
        recipeDialog.show();
    }

    public void showDueDateDialog(final ArrayList<HalfRecipeRecipeItem> selectedItems, final ArrayList<String> dueDateCheckArray) {
        dueDateDialog = new HalfRecipeDueDateDialog(this, dueDateCheckArray);
        dueDateDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) {

            }

            @Override
            public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {

            }

            @Override
            public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> radioCheckItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    for (int j = 0; j < radioCheckItems.size(); j++) {
                        if (selectedItems.get(i).getName().equals(radioCheckItems.get(j).getName())) {
                            radioCheckItems.get(j).setEditCount(selectedItems.get(i).getEditCount());
                        }
                    }
                }

                boolean result = updateCountByDueDate(radioCheckItems);
                if (result)
                    goHalfRecipeMaking(selectedItems, dueDateCheckArray);

            }
        });
        dueDateDialog.setCancelable(false);
        dueDateDialog.show();
    }

    private boolean updateCountByDueDate(ArrayList<HalfRecipeDueDateItem> radioCheckItems) {

        for (int i = 0; i < radioCheckItems.size(); i++) {

            // 1. 해당 이름의 유통기한과 보유개수 가져오기 배열로
            // 정수형태의 유통기한과 더블형의 보유개수를 저장할 배열 생성
            ArrayList<DCItem> dcArray = new ArrayList<>();

            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(radioCheckItems.get(i).getName())) {
                    Integer iDueDate = Integer.parseInt(refrigeratorItem.get(j).getDueDate());
                    dcArray.add(new DCItem(iDueDate, refrigeratorItem.get(j).getCount()));
                }
            }

            // 2. 유통기한 기준 정렬 which = 0 이면 오름차순 1 이면 내림차순
            switch (radioCheckItems.get(i).getWhich()) {
                case 0: //오름차순
                    Collections.sort(dcArray, new AscendingSort());
                    break;
                case 1: //내림차순
                    Collections.sort(dcArray, new DescendingSort());
                    break;
            }

            // 계산하고 바로 updatecount
            Double editCount = radioCheckItems.get(i).getEditCount();
            for (int k = 0; k < dcArray.size(); k++) {
                if (editCount - dcArray.get(k).getCount() < 0) {
                    Mapper.updateCountwithDueDate(radioCheckItems.get(i).getName(), Integer.toString(dcArray.get(k).getDueDate()), editCount);
                    break;
                } else {
                    editCount -= dcArray.get(k).getCount();
                    Mapper.updateCountwithDueDate(radioCheckItems.get(i).getName(), Integer.toString(dcArray.get(k).getDueDate()), dcArray.get(k).getCount());
                }
            }

        }

        return true;
    }

    private void goHalfRecipeMaking(ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) {

        if (dueDateCheckArray.size() == 0) {
            for (int i = 0; i < mItems.size(); i++) {
                Mapper.updateCount(mItems.get(i).getName(), mItems.get(i).getEditCount());
            }
        } else {
            // 중복 배열에 있는 거 제외하고 나머지에 대해 정상적으로 update
            for (int i = 0; i < mItems.size(); i++) {
                int has = 0;
                for (int j = 0; j < dueDateCheckArray.size(); j++) {
                    if (mItems.get(i).getName().equals(dueDateCheckArray.get(j)))
                        has = 1;
                }

                if (has == 0) {
                    Mapper.updateCount(mItems.get(i).getName(), mItems.get(i).getEditCount());
                }
            }
        }

        //recipe 테이블 접근
        List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            recipeIngredientList.add(createIngredient(mItems.get(i).getName(), mItems.get(i).getEditCount()));
        }

        String recipe_id = Mapper.createRecipe(recipeIngredientList, recipeSimpleName);
        Log.d("test", recipe_id);

        Mapper.addRecipeInMyCommunity(recipe_id);

        Intent halfRecipeCompleteActivity = new Intent(getApplicationContext(), HalfRecipeCompleteActivity.class);
        halfRecipeCompleteActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(halfRecipeCompleteActivity);
    }

}

class AscendingSort implements Comparator<DCItem> {

    @Override
    public int compare(DCItem t1, DCItem t2) {
        return t1.getDueDate().compareTo(t2.getDueDate());
    }
}

class DescendingSort implements Comparator<DCItem> {

    @Override
    public int compare(DCItem t1, DCItem t2) {
        return t2.getDueDate().compareTo(t1.getDueDate());
    }
}