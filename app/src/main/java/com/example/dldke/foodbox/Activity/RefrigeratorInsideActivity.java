package com.example.dldke.foodbox.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.R;

import java.util.List;

public class RefrigeratorInsideActivity extends AppCompatActivity {

    private Button btnSidedish, btnDairy, btnSauce, btnMeat, btnFruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnDairy = (Button)findViewById(R.id.btn_dairy);
        btnSauce = (Button)findViewById(R.id.btn_sauce);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fruit);

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapper.createMemo();
                Mapper.updateUrgentMemo();
                Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnDairy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "계란,유제품", Toast.LENGTH_LONG).show();
            }
        });

        btnSauce.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "음료,소스", Toast.LENGTH_LONG).show();
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
