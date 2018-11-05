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

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;

    private Button btnSidedish, btnDairy, btnSauce, btnMeat, btnFruit;
    private FloatingActionButton fbtnRecipe;

    private String reqCategory;

    private HalfRecipeIngreDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnDairy = (Button) findViewById(R.id.btn_dairy);
        btnSauce = (Button) findViewById(R.id.btn_sauce);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFruit = (Button) findViewById(R.id.btn_fruit);
        fbtnRecipe = (FloatingActionButton) findViewById(R.id.floatingButtonRecipe);

        btnSidedish.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
        btnSauce.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFruit.setOnClickListener(this);
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

        for (int i = 0; i < refrigeratorItem.size(); i++) {
//            if(refrigeratorItem.get(i).getSection().equals("sideDish")) {
//                localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
//            } else
            if(refrigeratorItem.get(i).getKindOf().equals("dairy")) {
                localDairy.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
            } else if(refrigeratorItem.get(i).getKindOf().equals("beverage") || refrigeratorItem.get(i).getKindOf().equals("sauce")) {
                localEtc.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
            } else if(refrigeratorItem.get(i).getSection().equals("meat")) {
                localMeat.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
            } else if(refrigeratorItem.get(i).getSection().equals("fresh")) {
                localFresh.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount()));
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                Toast.makeText(getApplicationContext(), "siedDish null처리 보류", Toast.LENGTH_SHORT).show();
//                if (localSideDish.size()==0) {
//                    Log.d("test", "sideDish : null");
//                } else {
//                    Log.d("test", "sideDish : " + localSideDish.size());
//                }
//                reqCategory = "sideDish";
//                dialog = new HalfRecipeIngreDialog(this, reqCategory);
//                dialog.show();
                break;
            case R.id.btn_dairy:
                for(int i=0; i<localDairy.size(); i++) {
                    Log.d("test", "dairy : " + localDairy.get(i).getName());
                }

                dialog = new HalfRecipeIngreDialog(this, "dairy", localDairy);
                dialog.show();
                break;
            case R.id.btn_sauce:
                for(int i=0; i<localEtc.size(); i++) {
                    Log.d("test", "etc : " + localEtc.get(i).getName());
                }

                dialog = new HalfRecipeIngreDialog(this, "etc", localEtc);
                dialog.show();
                break;
            case R.id.btn_meat:
                for(int i=0; i<localMeat.size(); i++) {
                    Log.d("test", "meat : " + localMeat.get(i).getName());
                }

                dialog = new HalfRecipeIngreDialog(this, "meat", localMeat);
                dialog.show();
                break;
            case R.id.btn_fruit:
                for(int i=0; i<localFresh.size(); i++) {
                    Log.d("test", "fresh : " + localFresh.get(i).getName());
                }

                dialog = new HalfRecipeIngreDialog(this, "fresh", localFresh);
                dialog.show();
                break;
//            case R.id.floatingButtonRecipe:
//                break;
        }
    }
}
