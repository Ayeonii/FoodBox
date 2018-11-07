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
import com.example.dldke.foodbox.HalfRecipeIngreDialog;
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
                        dialog = new HalfRecipeIngreDialog(this, "sideDish", false, localSideDish);
                    }
                }
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
                        dialog = new HalfRecipeIngreDialog(this, "dairy", false, localDairy);
                    }
                }
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
                        dialog = new HalfRecipeIngreDialog(this, "etc", false, localEtc);
                    }
                }
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
                        dialog = new HalfRecipeIngreDialog(this, "meat", false, localMeat);
                    }
                }
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
                        dialog = new HalfRecipeIngreDialog(this, "fresh", false, localFresh);
                    }
                }
                dialog.show();

                break;
//            case R.id.floatingButtonRecipe:
//                break;
        }
    }
}
