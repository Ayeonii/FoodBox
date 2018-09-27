package com.example.dldke.foodbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class RefrigeratorInsideActivity extends AppCompatActivity {

    Button btnSidedish, btnEggs, btnMeat, btnFruit;

    // Declare a DynamoDBMapper object
    DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {

            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Add code to instantiate a AmazonDynamoDBClient
                AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
                dynamoDBMapper = DynamoDBMapper.builder()
                        .dynamoDBClient(dynamoDBClient)
                        .awsConfiguration(
                                AWSMobileClient.getInstance().getConfiguration())
                        .build();

            }
        }).execute();

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnEggs = (Button)findViewById(R.id.btn_eggs);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fruit);

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFood();
                //Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnEggs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFood();
                //Toast.makeText(getApplicationContext(), "계란,유제품,음료,소스", Toast.LENGTH_LONG).show();
            }
        });

        btnMeat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFood();
                //Toast.makeText(getApplicationContext(), "육류,생선", Toast.LENGTH_LONG).show();
            }
        });

        btnFruit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFood();
                //Toast.makeText(getApplicationContext(), "과일,야채", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createFood() {
        final com.example.dldke.foodbox.FoodInfoDO foodItem = new com.example.dldke.foodbox.FoodInfoDO();

        foodItem.set칸이름("신선칸");

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(foodItem);
                // Item saved
                Log.d("2", String.format("Food Item: %s", foodItem.toString()));
            }
        }).start();
    }
    public void readFood() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                com.example.dldke.foodbox.FoodInfoDO foodItem = dynamoDBMapper.load(
                        com.example.dldke.foodbox.FoodInfoDO.class,
                        "신선칸");

                // Item read
                Log.d("1", String.format("Food Item: %s", foodItem.toString()));
                //Toast.makeText(getApplicationContext(),String.format("Books Item: %s", foodItem.toString()), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    public void updateFood() {
        final com.example.dldke.foodbox.FoodInfoDO foodItem = new com.example.dldke.foodbox.FoodInfoDO();


        foodItem.set칸이름("신선칸2");

        //  booksItem.setTitle("Escape from Slavery");

        new Thread(new Runnable() {
            @Override
            public void run() {


                // Using .save(bookItem) with no Title value makes that attribute value equal null
                // The .Savebehavior shown here leaves the existing value as is
                dynamoDBMapper.save(foodItem, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));

                // Item updated
            }
        }).start();
    }

    public void deleteFood() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                com.example.dldke.foodbox.FoodInfoDO foodItem = new com.example.dldke.foodbox.FoodInfoDO();
                foodItem.set칸이름("신선칸2");

                dynamoDBMapper.delete(foodItem);

                // Item deleted
            }
        }).start();
    }
}
