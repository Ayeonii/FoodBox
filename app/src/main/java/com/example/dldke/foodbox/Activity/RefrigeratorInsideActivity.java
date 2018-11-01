package com.example.dldke.foodbox.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorInsideActivity extends AppCompatActivity {

    private Button btnSidedish, btnEggs, btnBottles, btnMeat, btnFruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnEggs = (Button)findViewById(R.id.btn_eggs);
        btnBottles = (Button)findViewById(R.id.btn_bottles);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fruit);



        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<RecipeDO.Ingredient> specIngredientList = new ArrayList<>();

                //사용자 입력 몇 개 받는지에 따라 반복
                InfoDO potato = Mapper.searchFood("감자");
                InfoDO onion = Mapper.searchFood("양파");

                specIngredientList.add(Mapper.createIngredient(potato, 2.0));
                specIngredientList.add(Mapper.createIngredient(onion, 2.0));

                //위에서 만든 재료들이랑 방법, 불세기, 시간 넣어서 만듦
                //마찬가지로 단계가 몇 개인지에 따라 반복
                RecipeDO.Spec spec1 = Mapper.createSpec(specIngredientList, "볶는다", "강", 3);
                List<RecipeDO.Spec> specList = new ArrayList<>();
                specList.add(spec1);

                //단계 다 끝나면 풀레시피 만듦
                Mapper.createChefRecipe("감자볶음", specList);

                Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnEggs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<InfoDO> itemList = Mapper.scanKindOf("dairy");
                Log.d("mmmmmmmmmmm",itemList.get(0).getName());
                Toast.makeText(getApplicationContext(), "계란,유제품", Toast.LENGTH_LONG).show();
            }
        });

        btnBottles.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "음료, 소스", Toast.LENGTH_LONG).show();
            }
        });

        btnMeat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "육류,생선", Toast.LENGTH_LONG).show();
            }
        });

        btnFruit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "과일,야채", Toast.LENGTH_LONG).show();
            }
        });
    }
}
