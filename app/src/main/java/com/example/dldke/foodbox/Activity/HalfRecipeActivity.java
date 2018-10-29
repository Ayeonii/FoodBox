package com.example.dldke.foodbox.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dldke.foodbox.HalfRecipeDialog;
import com.example.dldke.foodbox.R;

public class HalfRecipeActivity extends AppCompatActivity {

    private Button btnSidedish, btnEggs, btnBottles, btnMeat, btnFruit;
    private FloatingActionButton fbtnRecipe;
    private HalfRecipeDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnEggs = (Button) findViewById(R.id.btn_eggs);
        btnBottles = (Button) findViewById(R.id.btn_bottles);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFruit = (Button) findViewById(R.id.btn_fruit);
        fbtnRecipe = (FloatingActionButton) findViewById(R.id.floatingButtonRecipe);

        fbtnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog("선택한재료", "선택한재료");
            }
        });

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnEggs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void Dialog(String type, String content) {
        dialog = new HalfRecipeDialog(HalfRecipeActivity.this, type, content, cancelListener, okListener);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    };

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    };
}
