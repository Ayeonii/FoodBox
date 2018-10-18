package com.example.dldke.foodbox;

import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.id8z9a74jyqj.EchoTestMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefrigeratorInsideActivity extends AppCompatActivity {
    private static final String LOG_TAG = RefrigeratorInsideActivity.class.getSimpleName();

    Button btnSidedish, btnEggs, btnMeat, btnFruit;

    private EchoTestMobileHubClient apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(EchoTestMobileHubClient.class);

        btnSidedish = (Button)findViewById(R.id.btn_sidedish);
        btnEggs = (Button)findViewById(R.id.btn_eggs);
        btnMeat = (Button)findViewById(R.id.btn_meat);
        btnFruit = (Button)findViewById(R.id.btn_fruit);

        btnSidedish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                ////신선칸 재료 보여주기
                /*List<InfoDO> itemList = Mapper.scanInfo( "fresh");
                for(int i = 0; i < itemList.size(); i++)
                {
                    Log.d("2", String.format("Refri Item: %s", itemList.get(i).getName()));
                }
                */
                ////내 냉장고 만들기(처음 만들때만 하면 됨)
                //Mapper.createRefrigerator();


                ////내 냉장고에 재료 집어넣기
                /*
                List<RefrigeratorDO.Item> foodItem = new ArrayList<>();

                //사용자 입력 몇 개 받는지에 따라 반복
                InfoDO potato = Mapper.searchFood("감자");
                InfoDO onion = Mapper.searchFood("양파");

                foodItem.add(Mapper.createFood(potato, 2.0));
                foodItem.add(Mapper.createFood(onion, 2.0));

                //입력 다 받았으면 집어넣음
                Mapper.putFood(foodItem);
                */

                ////내 냉장고 재료 보여주기
                /*List<RefrigeratorDO.Item> refri_item = Mapper.scanRefri();
                for(int i = 0; i < refri_item.size(); i++)
                {
                    Log.d("2", String.format("Refri Item: %s", refri_item.get(i).getName()));
                }
                */
                //내 냉장고 재료 유통기한 변경
                //Mapper.updateDueDate("감자", 2);

                //내 냉장고 재료 삭제
                //Mapper.deleteFood( "시금치");

                //내 냉장고 재료 소진
                //Mapper.updateCount("감자", 1);

                ////간이레시피 만들기
                /*List<RecipeDO.Ingredient> recipeIngredientList = new ArrayList<>();

                //사용자 입력 몇 개 받는지에 따라 반복
                InfoDO potato = Mapper.searchFood("감자");
                InfoDO onion = Mapper.searchFood("양파");

                recipeIngredientList.add(Mapper.createIngredient(potato, 2.0));
                recipeIngredientList.add(Mapper.createIngredient(onion, 2.0));

                //입력 다 받았으면 간이레시피 만듦
                String recipeId = Mapper.createRecipe(recipeIngredientList);

                //내 커뮤니티 만들기(최초1회만)
                Mapper.createMyCommunity();

                //내 커뮤니티에 내 간이레시피 등록
                Mapper.addRecipeInMyCommunity(recipeId);
                */

                ////풀레시피 만들기
                /*List<RecipeDO.Ingredient> specIngredientList = new ArrayList<>();

                //한 단계에 몇개의 재료인지에 따라 반복
                List<String> recipe_list = Mapper.searchMyCommunity().getMyRecipes();
                String recipe_id = recipe_list.get(0);
                //Log.d("2", "레시피 아이디 " + Mapper.searchRecipe(recipe_id).getIngredient().get(0).getIngredientName());

                specIngredientList.add(Mapper.searchRecipe(recipe_id).getIngredient().get(0));
                specIngredientList.add(Mapper.searchRecipe(recipe_id).getIngredient().get(1));

                //위에서 만든 재료들이랑 방법, 불세기, 시간 넣어서 만듦
                //마찬가지로 단계가 몇 개인지에 따라 반복
                RecipeDO.Spec spec1 = Mapper.createSpec(specIngredientList, "볶는다", "강", 3);
                List<RecipeDO.Spec> specList = new ArrayList<>();
                specList.add(spec1);

                //단계 다 끝나면 풀레시피 만듦
                Mapper.createFullRecipe(recipe_id, "감자볶음", specList);
                */

                ////게시글 작성
                /*List<String> recipe_list2 = Mapper.searchMyCommunity().getMyRecipes();
                String recipe_id2 = recipe_list2.get(0);
                if(Mapper.searchRecipe(recipe_id2).getDetail() != null)
                    Mapper.createPost("까까의 감자볶음",recipe_id2);
                */

                ////댓글 작성
                /*String post_id = Mapper.searchPost("볶음밥").get(0).getPostId();
                Mapper.createComment(post_id,"맛있겠다!");
                */
                //List<String> recipe_list = Mapper.searchMyCommunity().getMyRecipes();
                //String recipe_id = recipe_list.get(0);
                //Mapper.attachRecipeImage(recipe_id,"/storage/emulated/0/Download/test.jpg");
                List<InfoDO> itemList = Mapper.scanInfo( "fresh");
                for(int i = 0; i < itemList.size(); i++)
                {
                    Mapper.uploadImage(itemList.get(i).getName(),"/storage/emulated/0/Download/test.jpg");
                    Log.d("2", String.format("Refri Item: %s", itemList.get(i).getName()));
                }


                Toast.makeText(getApplicationContext(), "반찬", Toast.LENGTH_LONG).show();

            }
        });

        btnEggs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callCloudLogic();
                Toast.makeText(getApplicationContext(), "계란,유제품,음료,소스", Toast.LENGTH_LONG).show();
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
    public void uploadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();


        TransferObserver uploadObserver =
                transferUtility.upload(
                        "uploads/test.jpg",
                        new File("/storage/emulated/0/Download/test.jpg"));


        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }


   /* public RecipeDO.Ingredient createIngredient(String name, Double count)
    {
        RecipeDO.Ingredient ingredient = new RecipeDO.Ingredient();
        ingredient.setIngredientName(name);
        ingredient.setIngredientCount(count);
        return ingredient;

    }

    public RecipeDO.Spec createSpec(List<RecipeDO.Ingredient> ingredient, String method, String fire, Integer minute)
    {
        RecipeDO.Spec spec1 = new RecipeDO.Spec();
        List<RecipeDO.Ingredient> spec1IngredientList = spec1.getSpecIngredient();

        for(int i = 0; i < ingredient.size(); i++)
        {
            spec1IngredientList.add(ingredient.get(i));
        }
        spec1.setSpecIngredient(spec1IngredientList);
        spec1.setSpecMethod(method);
        spec1.setSpecFire(fire);
        spec1.setSpecMinute(minute);

        return spec1;
    }


    public void createRecipe(List<RecipeDO.Ingredient> ingredient) {
        final com.example.dldke.foodbox.RecipeDO recipeItem = new com.example.dldke.foodbox.RecipeDO();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        String dateS = sdf.format(date).toString();

        recipeItem.setRecipeId("kitawo" + dateS);
        recipeItem.setDate(dateS);

        List<RecipeDO.Ingredient> tmpIngredientList = recipeItem.getIngredient();
        int size = ingredient.size();

        for (int i = 0; i < size; i++) {
            tmpIngredientList.add(ingredient.get(i));
        }
        recipeItem.setIngredient(tmpIngredientList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(recipeItem);
            }
        }).start();
    }

    public void createFullRecipe(String recipeId, String name, List<RecipeDO.Spec> spec)
    {
        final String ID = recipeId;
        final String recipe_name = name;
        final List<RecipeDO.Spec> specList = spec;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        ID);

                RecipeDO.Detail detail = new RecipeDO.Detail();
                detail.setFoodName(recipe_name);

                List<RecipeDO.Spec> specList = detail.getSpecList();
                for(int i = 0; i < specList.size(); i++)
                {
                    specList.add(specList.get(i));
                }
                detail.setSpecList(specList);

                recipeItem.setDetail(detail);
                Mapper.getDynamoDBMapper().save(recipeItem);
            }
        }).start();
    }

    public void createPost(String title, String recipeId) {
        final String post_title = title;
        final String ID = recipeId;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.PostDO postItem = new com.example.dldke.foodbox.PostDO();
                final com.example.dldke.foodbox.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        ID);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
                String dateS = sdf.format(date).toString();

                postItem.setPostId("kitawo" + dateS);
                postItem.setDate(dateS);
                postItem.setWriter("kitawo324");
                postItem.setTitle(post_title);
                postItem.setRecipeId(ID);

                Mapper.getDynamoDBMapper().save(postItem);
            }
        }).start();
    }

    public void createComment(String postId, String userId, String content) {
        final String postID = postId;
        final String userID = userId;
        final String text = content;


        new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.PostDO postItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.PostDO.class,
                        postID);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
                String dateS = sdf.format(date).toString();

                PostDO.Comment comment1 = new PostDO.Comment();
                comment1.setDate(dateS);
                comment1.setUserId(userID);
                comment1.setContent(text);
                List<PostDO.Comment> commentList = postItem.getCommentList();
                commentList.add(comment1);
                postItem.setCommentList(commentList);
                Mapper.getDynamoDBMapper().save(postItem);
            }
        }).start();
    }

    public List<InfoDO> scanInfo(String section) {
        final com.example.dldke.foodbox.InfoDO foodItem = new com.example.dldke.foodbox.InfoDO();

        final String sectionName = section;

         returnThread thread = new returnThread(new CustomRunnable() {
            List<InfoDO> itemList;
            @Override
            public void run() {

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(sectionName));
                scanExpression.addFilterCondition("section", condition);
                itemList = Mapper.getDynamoDBMapper().scan(InfoDO.class, scanExpression);
            }
            @Override
            public Object getResult(){
                return itemList;
            }
        });

        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<InfoDO> itemList = (List<InfoDO>)thread.getResult();
        return itemList;
    }

    public List<RefrigeratorDO.Item> scanRefri(String userId) {
        final String ID = userId;
        Toast.makeText(getApplicationContext(), "들어옴", Toast.LENGTH_LONG).show();

        returnThread thread = new returnThread(new CustomRunnable() {
            com.example.dldke.foodbox.RefrigeratorDO Refri;
            @Override
            public void run() {
                 Refri = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        ID);
                 }
             @Override
            public Object getResult(){
                return Refri.getItem();
             }
        });

        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<RefrigeratorDO.Item> refri_item = (List<RefrigeratorDO.Item>)thread.getResult();
        return refri_item;

    }

    public InfoDO searchFood(String name) {

        final String foodName = name;
        returnThread thread = new returnThread(new CustomRunnable() {

            com.example.dldke.foodbox.InfoDO foodItem;
            @Override
            public void run() {
                foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.InfoDO.class,
                        foodName,
                        "fresh");
            }

            @Override
            public Object getResult(){
                return foodItem;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        com.example.dldke.foodbox.InfoDO foodItem = (InfoDO)thread.getResult();

        return foodItem;
    }

    public void updateDueDate(String userId, String name, Integer dueDate) {
        final String ID = userId;
        final String itemName = name;
        final Integer newDueDate = dueDate;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        ID);
                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName)) {
                        foodItem.getItem().get(i).setDueDate(newDueDate);
                        break;
                    }
                }

                Mapper.getDynamoDBMapper().save(foodItem, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
            }
        }).start();
    }

    public void updateCount(String userId, String name, Integer count)
    {
        final String ID = userId;
        final String itemName = name;
        final Integer minus = count;
        new Thread(new Runnable() {
            @Override
            public void run() {

                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        ID);

                double count = 0;
                int index = 0;

                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName)) {
                        count = foodItem.getItem().get(i).getCount();
                        index = i;
                        break;
                    }
                }
                count = count - minus;
                foodItem.getItem().get(index).setCount(count);

                if(foodItem.getItem().get(index).getCount() == 0)
                    foodItem.getItem().remove(index);

                Mapper.getDynamoDBMapper().save(foodItem, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
            }
        }).start();
    }

    public void deleteFood(String userId, String name) {

        final String ID = userId;
        final String itemName = name;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        ID);
                int index = 0;
                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName)) {
                        foodItem.getItem().remove(i);
                        break;
                    }
                }
                Mapper.getDynamoDBMapper().save(foodItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createRefrigerator(String userId) {
        final com.example.dldke.foodbox.RefrigeratorDO refrigeratorItem = new com.example.dldke.foodbox.RefrigeratorDO();

        refrigeratorItem.setUserId(userId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(refrigeratorItem);
            }
        }).start();
    }

    public RefrigeratorDO.Item createFood(InfoDO item, Double count) {

        RefrigeratorDO.Item food = new RefrigeratorDO.Item();
        food.setName(item.getName());
        food.setSection(item.getSection());
        food.setKindOf(item.getKindOf());
        food.setDueDate(item.getDueDate());
        food.setCount(count);

        return food;
    }

    public void putFood(String userId, List<RefrigeratorDO.Item> foods) {
        final String ID = userId;
        final List<RefrigeratorDO.Item> foods_list = foods;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO refrigeratorItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        ID);

                List<RefrigeratorDO.Item> r_item = refrigeratorItem.getItem();
                for(int i = 0; i < foods_list.size(); i++)
                {
                    r_item.add(foods_list.get(i));
                }
                refrigeratorItem.setItem(r_item);
                Mapper.getDynamoDBMapper().save(refrigeratorItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void callCloudLogic() {
        // Create components of api request
        final String method = "GET";

        final String path = "/items";

        final String body = "";
        final byte[] content = body.getBytes(StringUtils.UTF8);

        final Map parameters = new HashMap<>();
        parameters.put("lang", "en_US");

        final Map headers = new HashMap<>();

        // Use components to create the api request
        ApiRequest localRequest =
                new ApiRequest(apiClient.getClass().getSimpleName())
                        .withPath(path)
                        .withHttpMethod(HttpMethodName.valueOf(method))
                        .withHeaders(headers)
                        .addHeader("Content-Type", "application/json")
                        .withParameters(parameters);

        // Only set body if it has content.
        if (body.length() > 0) {
            localRequest = localRequest
                    .addHeader("Content-Length", String.valueOf(content.length))
                    .withBody(content);
        }

        final ApiRequest request = localRequest;

        // Make network call on background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG,
                            "Invoking API w/ Request : " +
                                    request.getHttpMethod() + ":" +
                                    request.getPath());

                    final ApiResponse response = apiClient.execute(request);

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, "Response : " + responseData);
                    }

                    Log.d(LOG_TAG, response.getStatusCode() + " " + response.getStatusText());

                } catch (final Exception exception) {
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                    exception.printStackTrace();
                }
            }
        }).start();
    }*/
}
