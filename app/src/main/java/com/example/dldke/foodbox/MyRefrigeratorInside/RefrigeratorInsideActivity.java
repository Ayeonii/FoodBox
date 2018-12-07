package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorInsideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;
    private ArrayList<String> nameSideDish, nameDairy, nameEtc, nameMeat, nameFresh;
    private ArrayList<String> dupliArray = new ArrayList<>();

    private InsideDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnDairy = (Button) findViewById(R.id.btn_dairy);
        btnEtc = (Button) findViewById(R.id.btn_etc);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFresh = (Button) findViewById(R.id.btn_fresh);

        btnSidedish.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFresh.setOnClickListener(this);

        scanToLocalRefrigerator();
        setDuplicateArray();
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

    @Override
    public void onClick(View view) {
        scanToLocalRefrigerator();
        setDuplicateArray();

        switch (view.getId()) {
            case R.id.btn_sidedish:
                showIngredientDialog("sideDish", localSideDish, nameSideDish);
                break;
            case R.id.btn_dairy:
                showIngredientDialog("dairy", localDairy, nameDairy);
                break;
            case R.id.btn_etc:
                showIngredientDialog("etc", localEtc, nameEtc);
                break;
            case R.id.btn_meat:
                showIngredientDialog("meat", localMeat, nameMeat);
                break;
            case R.id.btn_fresh:
                showIngredientDialog("fresh", localFresh, nameFresh);
                break;
        }
    }

    public void showIngredientDialog(String type, ArrayList<LocalRefrigeratorItem> localArray, ArrayList<String> nameArray) {

        if (nameArray.size() == 0)
            dialog = new InsideDialog(this, type, true);
        else
            dialog = new InsideDialog(this, type, false, localArray, nameArray);

        dialog.show();
    }
}