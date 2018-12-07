package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.id3idutfky0i.TestMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefrigeratorInsideActivity extends AppCompatActivity {
    private Button btnSidedish, btnDairy, btnSauce, btnMeat, btnFruit;
    //private CrawlerMobileHubClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnDairy = (Button)findViewById(R.id.btn_dairy);
        btnSauce = (Button)findViewById(R.id.btn_etc);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fresh);

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*apiClient = new ApiClientFactory()
                        .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                        .build(CrawlerMobileHubClient.class);
                        */

                Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnDairy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mapper.getImageUrl("사과");
                Toast.makeText(getApplicationContext(), "계란,유제품", Toast.LENGTH_LONG).show();
            }
        });

        btnSauce.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapper.attachRecipeImage("gayoung2018-11-15, 10:20:43 AM","/storage/emulated/0/Download/감.jpg");
                String url = Mapper.getImageUrlRecipe("gayoung2018-11-15, 10:20:43 AM");
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "음료,소스", Toast.LENGTH_LONG).show();
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
