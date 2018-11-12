package com.example.dldke.foodbox.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipeDialogListener;
import com.example.dldke.foodbox.HalfRecipeIngreDialog;
import com.example.dldke.foodbox.HalfRecipeRecipeDialog;
import com.example.dldke.foodbox.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh;
    private FloatingActionButton fbtnRecipe;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;

    private HalfRecipeIngreDialog dialog;
    private HalfRecipeRecipeDialog recipeDialog;

    private Boolean[] checkSideDish, checkDairy, checkEtc, checkMeat, checkFresh;
    private ArrayList<LocalRefrigeratorItem> selectedItem;


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

        Log.d("test", "onCreate");
        scanToLocalRefrigerator();
        setCheckArray();
    }

    public void setCheckArray() {
        checkSideDish = new Boolean[localSideDish.size()];
        checkDairy = new Boolean[localDairy.size()];
        checkEtc = new Boolean[localEtc.size()];
        checkMeat = new Boolean[localMeat.size()];
        checkFresh = new Boolean[localFresh.size()];

        for (int i=0; i<localSideDish.size(); i++)
            checkSideDish[i] = false;
        for (int i=0; i<localDairy.size(); i++)
            checkDairy[i] = false;
        for (int i=0; i<localEtc.size(); i++)
            checkEtc[i] = false;
        for (int i=0; i<localMeat.size(); i++)
            checkMeat[i] = false;
        for (int i=0; i<localFresh.size(); i++)
            checkFresh[i] = false;

    }

    public void scanToLocalRefrigerator() {

        refrigeratorItem = Mapper.scanRefri();

        localSideDish = new ArrayList<>();
        localDairy = new ArrayList<>();
        localEtc = new ArrayList<>();
        localMeat = new ArrayList<>();
        localFresh = new ArrayList<>();

        Log.d("test", "refrigeratorItem.size : " + refrigeratorItem.size());
        for (int i = 0; i < refrigeratorItem.size(); i++) {
            Log.d("test", "index : " + i);
            try {
                if (refrigeratorItem.get(i).getSection().equals("sideDish")) {
                    Log.d("test", "sideDish : " + refrigeratorItem.get(i).getName());
                    localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
                }
            } catch (NullPointerException e) {
                Log.d("test", "sideDish null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getKindOf().equals("dairy")) {
                    Log.d("test", "dairy : " + refrigeratorItem.get(i).getName());
                    localDairy.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
                }
            } catch (NullPointerException e) {
                Log.d("test", "dairy null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getKindOf().equals("beverage") || refrigeratorItem.get(i).getKindOf().equals("sauce")) {
                    Log.d("test", "etc : " + refrigeratorItem.get(i).getName());
                    localEtc.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
                }
            } catch (NullPointerException e) {
                Log.d("test", "etc null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getSection().equals("meat")) {
                    Log.d("test", "meat : " + refrigeratorItem.get(i).getName());
                    localMeat.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
                }
            } catch (NullPointerException e) {
                Log.d("test", "meat null: " + e.getMessage());
            }

            try {
                if (refrigeratorItem.get(i).getSection().equals("fresh")) {
                    Log.d("test", "fresh : " + refrigeratorItem.get(i).getName());
                    localFresh.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
                }
            } catch (NullPointerException e) {
                Log.d("test", "fresh null: " + e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                Toast.makeText(getApplicationContext(), "sideDish", Toast.LENGTH_SHORT).show();
                Log.d("test", "======sideDish======");

                if(localSideDish.size()==0) {
                    Log.d("test", "empty");
                    dialog = new HalfRecipeIngreDialog(this, "sideDish", true);
                } else {
                    for (int i = 0; i < localSideDish.size(); i++) {
                        Log.d("test", localSideDish.get(i).getName());
                    }
                    dialog = new HalfRecipeIngreDialog(this, "sideDish", false, localSideDish, checkSideDish);
                }

                dialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        setResult(type, check);
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();

                break;
            case R.id.btn_dairy:
                Toast.makeText(getApplicationContext(), "dairy", Toast.LENGTH_SHORT).show();
                Log.d("test", "======dairy======");

                if(localDairy.size()==0) {
                    Log.d("test", "empty");
                    dialog = new HalfRecipeIngreDialog(this, "dairy", true);
                } else {
                    for (int i = 0; i < localDairy.size(); i++) {
                        Log.d("test", localDairy.get(i).getName());
                    }
                    dialog = new HalfRecipeIngreDialog(this, "dairy", false, localDairy, checkDairy);
                }

                dialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        setResult(type, check);
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();

                break;
            case R.id.btn_etc:
                Toast.makeText(getApplicationContext(), "etc", Toast.LENGTH_SHORT).show();
                Log.d("test", "======etc======");

                if(localEtc.size()==0) {
                    Log.d("test", "empty");
                    dialog = new HalfRecipeIngreDialog(this, "etc", true);
                } else {
                    for (int i = 0; i < localEtc.size(); i++) {
                        Log.d("test", localEtc.get(i).getName());
                    }
                    dialog = new HalfRecipeIngreDialog(this, "etc", false, localEtc, checkEtc);
                }

                dialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        setResult(type, check);
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();

                break;
            case R.id.btn_meat:
                Toast.makeText(getApplicationContext(), "meat", Toast.LENGTH_SHORT).show();
                Log.d("test", "======meat======");

                if(localMeat.size()==0) {
                    Log.d("test", "empty");
                    dialog = new HalfRecipeIngreDialog(this, "meat", true);
                } else {
                    for (int i = 0; i < localMeat.size(); i++) {
                        Log.d("test", localMeat.get(i).getName());
                    }
                    dialog = new HalfRecipeIngreDialog(this, "meat", false, localMeat, checkMeat);
                }

                dialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        setResult(type, check);
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();

                break;
            case R.id.btn_fresh:
                Toast.makeText(getApplicationContext(), "fresh", Toast.LENGTH_SHORT).show();
                Log.d("test", "======fresh======");

                if(localFresh.size()==0) {
                    Log.d("test", "empty");
                    dialog = new HalfRecipeIngreDialog(this, "fresh", true);
                } else {
                    for (int i = 0; i < localFresh.size(); i++) {
                        Log.d("test", localFresh.get(i).getName());
                    }
                    dialog = new HalfRecipeIngreDialog(this, "fresh", false, localFresh, checkFresh);
                }

                dialog.setDialogListener(new HalfRecipeDialogListener() {
                    @Override
                    public void onPositiveClicked(String type, Boolean[] check) {
                        setResult(type, check);
                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();

                break;
            case R.id.floatingButtonRecipe:
                setSelectedItem();

                for (int i=0; i<selectedItem.size(); i++) {
                    Log.d("test", i + " : selectedItem : " + selectedItem.get(i).getName());
                }

                recipeDialog = new HalfRecipeRecipeDialog(this, selectedItem);
                recipeDialog.show();
                break;
        }
    }

    private void setSelectedItem() {
        selectedItem = new ArrayList<>();

        for (int i=0; i<localSideDish.size(); i++)
            if (checkSideDish[i])
                selectedItem.add(new LocalRefrigeratorItem(localSideDish.get(i).getName(), localSideDish.get(i).getCount()));

        for (int i=0; i<localDairy.size(); i++)
            if (checkDairy[i])
                selectedItem.add(new LocalRefrigeratorItem(localDairy.get(i).getName(), localDairy.get(i).getCount()));

        for (int i=0; i<localEtc.size(); i++)
            if (checkEtc[i])
                selectedItem.add(new LocalRefrigeratorItem(localEtc.get(i).getName(), localEtc.get(i).getCount()));

        for (int i=0; i<localMeat.size(); i++)
            if (checkMeat[i])
                selectedItem.add(new LocalRefrigeratorItem(localMeat.get(i).getName(), localMeat.get(i).getCount()));

        for (int i=0; i<localFresh.size(); i++)
            if (checkFresh[i])
                selectedItem.add(new LocalRefrigeratorItem(localFresh.get(i).getName(), localFresh.get(i).getCount()));

    }

    private void setResult(String type, Boolean[] check) {
        switch (type) {
            case "sideDish":
                for(int i=0; i<localSideDish.size(); i++) {
                    if (check[i] != checkSideDish[i]) {
                        if (check[i])
                            checkSideDish[i] = true;
                        else
                            checkSideDish[i] = false;
                    }
                }

                break;
            case "dairy":
                for(int i=0; i<localDairy.size(); i++) {
                    if (check[i] != checkDairy[i]) {
                        if (check[i])
                            checkDairy[i] = true;
                        else
                            checkDairy[i] = false;
                    }
                }

                break;
            case "etc":
                for(int i=0; i<localEtc.size(); i++) {
                    if (check[i] != checkEtc[i]) {
                        if (check[i])
                            checkEtc[i] = true;
                        else
                            checkEtc[i] = false;
                    }
                }

                break;
            case "meat":
                for(int i=0; i<localMeat.size(); i++) {
                    if (check[i] != checkMeat[i]) {
                        if (check[i])
                            checkMeat[i] = true;
                        else
                            checkMeat[i] = false;
                    }
                }

                break;
            case "fresh":
                for(int i=0; i<localFresh.size(); i++) {
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
}
