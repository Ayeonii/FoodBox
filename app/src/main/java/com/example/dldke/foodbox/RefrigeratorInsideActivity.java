package com.example.dldke.foodbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.ArrayList;
import java.util.List;

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

                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {

                    @Override
                    public void onIdentityId(String s) {

                        //The network call to fetch AWS credentials succeeded, the cached
                        // user ID is available from IdentityManager throughout your app
                        Log.d("MainActivity", "Identity ID is: " + s);
                        Log.d("MainActivity", "Cached Identity ID: " + IdentityManager.getDefaultIdentityManager().getCachedUserID());
                    }

                    @Override
                    public void handleError(Exception e) {
                        Log.e("MainActivity", "Error in retrieving Identity ID: " + e.getMessage());
                    }
                });

            }
        }).execute();

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnEggs = (Button)findViewById(R.id.btn_eggs);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fruit);

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
                //scanFood();
                //createPost();
                //createFood();
                //Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();
            }
        });

        btnEggs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //readFood();
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

    public void createRecipe() {
        final com.example.dldke.foodbox.RecipeDO recipeItem = new com.example.dldke.foodbox.RecipeDO();

        recipeItem.setRecipeId("001");
        recipeItem.setDate("2018-09-26 21:24");
        RecipeDO.Ingredient onion = new RecipeDO.Ingredient();
        onion.setIngredientName("onion");
        onion.setIngredientCount(2);

        RecipeDO.Ingredient carrot = new RecipeDO.Ingredient();
        carrot.setIngredientName("carrot");
        carrot.setIngredientCount(1);

        List<RecipeDO.Ingredient> tmpIngredientList = recipeItem.getIngredient();
        tmpIngredientList.add(onion);
        tmpIngredientList.add(carrot);
        recipeItem.setIngredient(tmpIngredientList);

        RecipeDO.Detail detail = new RecipeDO.Detail();
        detail.setFoodName("볶음밥");

        RecipeDO.Spec spec1 = new RecipeDO.Spec();
        List<RecipeDO.Ingredient> spec1IngredientList = spec1.getSpecIngredient();
        spec1IngredientList.add(onion);
        spec1.setSpecIngredient(spec1IngredientList);
        spec1.setSpecMethod("깍둑썰기해서 볶는다");
        spec1.setSpecFire("강");
        spec1.setSpecMinute(2);

        RecipeDO.Spec spec2 = new RecipeDO.Spec();
        List<RecipeDO.Ingredient> spec2IngredientList = spec2.getSpecIngredient();
        spec2IngredientList.add(carrot);
        spec2.setSpecIngredient(spec1IngredientList);
        spec2.setSpecMethod("깍둑썰기해서 볶는다");
        spec2.setSpecFire("중");
        spec2.setSpecMinute(3);

        List<RecipeDO.Spec> specList = detail.getSpecList();
        specList.add(spec1);
        specList.add(spec2);
        detail.setSpecList(specList);

        recipeItem.setDetail(detail);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(recipeItem);
                // Item saved
                Log.d("2", String.format("Post Item: %s", recipeItem.getDetail().getFoodName()));
            }
        }).start();
    }

    public void createPost() {
        final com.example.dldke.foodbox.PostDO postItem = new com.example.dldke.foodbox.PostDO();

        postItem.setPostId("001");
        postItem.setRecipeId("001");
        postItem.setTitle("까까의 볶음밥");
        postItem.setWriter("kitawo324");
        postItem.setDate("2018-09-26 15:20");
        PostDO.Comment comment1 = new PostDO.Comment();
        comment1.setDate("2018-09-26 15:21");
        comment1.setUserId("gayoung1429");
        comment1.setContent("너무 맛있어보여요");
        List<PostDO.Comment> commentList = postItem.getCommentList();
        commentList.add(comment1);
        postItem.setCommentList(commentList);
        Log.d("2", String.format("Post Item: %s", postItem.getTitle()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(postItem);
                // Item saved
                Log.d("2", String.format("after Post Item: %s", postItem.getTitle()));
            }
        }).start();
    }

    public void scanFood() {
        final com.example.dldke.foodbox.InfoDO foodItem = new com.example.dldke.foodbox.InfoDO();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS("fresh"));
                scanExpression.addFilterCondition("section", condition);
                List<InfoDO> itemList = dynamoDBMapper.scan(InfoDO.class, scanExpression);


                // Item saved
                for (int i=0;i<itemList.size();i++){
                    Log.d("2", String.format("List Item: %s", itemList.get(i).getName()));
                }

            }
        }).start();
    }

    public void createFood() {
        final com.example.dldke.foodbox.InfoDO foodItem = new com.example.dldke.foodbox.InfoDO();

        foodItem.setSection("fresh");
        foodItem.setName("오렌지");
        foodItem.setKindOf("fruit");
        foodItem.setDueDate(5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(foodItem);
                // Item saved
                Log.d("2", String.format("Food Item: %s", foodItem.getName()));
            }
        }).start();
    }
    public void readFood() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                com.example.dldke.foodbox.InfoDO foodItem = dynamoDBMapper.load(
                        com.example.dldke.foodbox.InfoDO.class,
                        "수박",
                        "fresh");

                // Item read
                Log.d("1", String.format("Read Food Item: %s", foodItem.getDueDate()));
                //Toast.makeText(getApplicationContext(),String.format("Books Item: %s", foodItem.toString()), Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    public void updateFood() {
        final com.example.dldke.foodbox.InfoDO foodItem = new com.example.dldke.foodbox.InfoDO();


        foodItem.setSection("fresh");
        foodItem.setName("사과");
        foodItem.setDueDate(1);

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

                com.example.dldke.foodbox.InfoDO foodItem = new com.example.dldke.foodbox.InfoDO();
                foodItem.setSection("fresh");
                foodItem.setName("오렌지");

                dynamoDBMapper.delete(foodItem);

                // Item deleted
            }
        }).start();
    }
}
