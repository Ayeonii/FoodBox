package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;

import com.example.dldke.foodbox.InsideDialog;
import com.example.dldke.foodbox.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorInsideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;

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

        //scanToLocalRefrigerator();
        try {
            refrigeratorItem = Mapper.scanRefri();
        } catch (NullPointerException e) {
            //해당 아이디의 create된 냉장고가 없을 경우 create해야됨
            Mapper.createRefrigerator();
            refrigeratorItem = Mapper.scanRefri();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                Log.d("test", "======sideDish======");
                refrigeratorItem = Mapper.scanRefri();
                localSideDish = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    Log.d("test", "index : " + i);
                    try {
                        if (refrigeratorItem.get(i).getSection().equals("sideDish")) {
                            Log.d("test", "sideDish : " + refrigeratorItem.get(i).getName());
                            localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "sideDish null: " + e.getMessage());
                    }
                }

                if (localSideDish.size() == 0) {
                    Log.d("test", "empty");
                    dialog = new InsideDialog(this, "sideDish", true);
                } else {
                    for (int i = 0; i < localSideDish.size(); i++) {
                        Log.d("test", localSideDish.get(i).getName());
                    }
                    dialog = new InsideDialog(this, "sideDish", false, localSideDish);
                }
                dialog.show();

                break;


            case R.id.btn_dairy:
                Log.d("test", "======dairy======");
                refrigeratorItem = Mapper.scanRefri();
                localDairy = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    Log.d("test", "index : " + i);
                    try {
                        if (refrigeratorItem.get(i).getKindOf().equals("dairy")) {
                            Log.d("test", "dairy : " + refrigeratorItem.get(i).getName());
                            localDairy.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "dairy null: " + e.getMessage());
                    }
                }

                if (localDairy.size() == 0) {
                    Log.d("test", "empty");
                    dialog = new InsideDialog(this, "dairy", true);
                } else {
                    for (int i = 0; i < localDairy.size(); i++) {
                        Log.d("test", localDairy.get(i).getName());
                    }
                    dialog = new InsideDialog(this, "dairy", false, localDairy);
                }
                dialog.show();

                break;
            case R.id.btn_etc:
                Log.d("test", "======etc======");
                refrigeratorItem = Mapper.scanRefri();
                localEtc = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    Log.d("test", "index : " + i);
                    try {
                        if (refrigeratorItem.get(i).getKindOf().equals("beverage") || refrigeratorItem.get(i).getKindOf().equals("sauce")) {
                            Log.d("test", "etc : " + refrigeratorItem.get(i).getName());
                            localEtc.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "etc null: " + e.getMessage());
                    }
                }

                if (localEtc.size() == 0) {
                    Log.d("test", "empty");
                    dialog = new InsideDialog(this, "etc", true);
                } else {
                    for (int i = 0; i < localEtc.size(); i++) {
                        Log.d("test", localEtc.get(i).getName());
                    }
                    dialog = new InsideDialog(this, "etc", false, localEtc);
                }

                dialog.show();

                break;
            case R.id.btn_meat:
                Log.d("test", "======meat======");
                refrigeratorItem = Mapper.scanRefri();
                localMeat = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    Log.d("test", "index : " + i);
                    try {
                        if (refrigeratorItem.get(i).getSection().equals("meat")) {
                            Log.d("test", "meat : " + refrigeratorItem.get(i).getName());
                            localMeat.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "meat null: " + e.getMessage());
                    }
                }

                if (localMeat.size() == 0) {
                    Log.d("test", "empty");
                    dialog = new InsideDialog(this, "meat", true);
                } else {
                    for (int i = 0; i < localMeat.size(); i++) {
                        Log.d("test", localMeat.get(i).getName());
                    }
                    dialog = new InsideDialog(this, "meat", false, localMeat);
                }
                dialog.show();

                break;
            case R.id.btn_fresh:
                Log.d("test", "======fresh======");
                refrigeratorItem = Mapper.scanRefri();
                localFresh = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    Log.d("test", "index : " + i);
                    try {
                        if (refrigeratorItem.get(i).getSection().equals("fresh")) {
                            Log.d("test", "fresh : " + refrigeratorItem.get(i).getName());
                            localFresh.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "fresh null: " + e.getMessage());
                    }
                }

                if (localFresh.size() == 0) {
                    Log.d("test", "empty");
                    dialog = new InsideDialog(this, "fresh", true);
                } else {
                    for (int i = 0; i < localFresh.size(); i++) {
                        Log.d("test", localFresh.get(i).getName());
                    }
                    dialog = new InsideDialog(this, "fresh", false, localFresh);
                }
                dialog.show();

                break;
        }
    }
}
