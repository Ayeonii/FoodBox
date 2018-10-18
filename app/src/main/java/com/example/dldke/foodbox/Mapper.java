package com.example.dldke.foodbox;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Mapper {

    private static DynamoDBMapper dynamoDBMapper;
    private static String userId;
    private static String bucketName;
    private Mapper(){
    }

    public static DynamoDBMapper getDynamoDBMapper(){
        return dynamoDBMapper;
    }
    public static void setDynamoDBMapper(){

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
            dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .awsCredentialsProviderForS3(AWSMobileClient.getInstance().getCredentialsProvider())
                    .build();

    }
    public static void setUserId(Context context){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(AWSMobileClient.getInstance().getConfiguration().toString());
        PoolConfig poolConfig= new Gson().fromJson(element.getAsJsonObject().get("CognitoUserPool"), PoolConfig.class);
        CognitoUserPool cognitoUserPool = new CognitoUserPool(context,poolConfig.config.poolId,poolConfig.config.clientId,poolConfig.config.clientSecret);
        CognitoUser user = cognitoUserPool.getCurrentUser();
        userId = user.getUserId();

    }

    public static void setBucketName(Context context){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(AWSMobileClient.getInstance().getConfiguration().toString());
        bucketName = element.getAsJsonObject().get("S3TransferUtility").getAsJsonObject().get("Default").getAsJsonObject().get("Bucket").getAsString();
    }

    public static RecipeDO.Ingredient createIngredient(InfoDO item, Double count)
    {
        RecipeDO.Ingredient ingredient = new RecipeDO.Ingredient();
        ingredient.setIngredientName(item.getName());
        ingredient.setIngredientCount(count);
        return ingredient;
    }

    public static RecipeDO.Spec createSpec(List<RecipeDO.Ingredient> ingredient, String method, String fire, Integer minute)
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


    public static String createRecipe(List<RecipeDO.Ingredient> ingredient) {
        final com.example.dldke.foodbox.RecipeDO recipeItem = new com.example.dldke.foodbox.RecipeDO();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        String dateS = sdf.format(date).toString();

        recipeItem.setRecipeId(userId + dateS);
        recipeItem.setDate(dateS);

        List<RecipeDO.Ingredient> tmpIngredientList = recipeItem.getIngredient();
        int size = ingredient.size();

        for (int i = 0; i < size; i++) {
            tmpIngredientList.add(ingredient.get(i));
        }
        recipeItem.setIngredient(tmpIngredientList);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(recipeItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        return recipeItem.getRecipeId();
    }

    public static RecipeDO searchRecipe(String recipeId) {

        final String recipe_id = recipeId;
        returnThread thread = new returnThread(new CustomRunnable() {

            com.example.dldke.foodbox.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        recipe_id);
            }

            @Override
            public Object getResult(){
                return recipeItem;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        com.example.dldke.foodbox.RecipeDO recipeItem = (RecipeDO) thread.getResult();

        return recipeItem;
    }

    public static void createFullRecipe(String recipeId, String name, List<RecipeDO.Spec> spec)
    {
        final String ID = recipeId;
        final String recipe_name = name;
        final List<RecipeDO.Spec> rspecList = spec;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        ID);

                RecipeDO.Detail detail = new RecipeDO.Detail();
                detail.setFoodName(recipe_name);

                List<RecipeDO.Spec> specList = detail.getSpecList();
                for(int i = 0; i < rspecList.size(); i++)
                {
                    specList.add(rspecList.get(i));
                }
                detail.setSpecList(specList);

                recipeItem.setDetail(detail);
                Mapper.getDynamoDBMapper().save(recipeItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void attachRecipeImage(String recipeId, final String filePath){
        final String recipe_id = recipeId;
        final String[] key = filePath.split("/");
        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        recipe_id);
                Log.d("why",Mapper.bucketName);
                recipeItem.setRecipeImage(Mapper.getDynamoDBMapper().createS3Link(Region.US_Standard,Mapper.bucketName,"kitawo324/test" + key[key.length-1]));
                recipeItem.getRecipeImage().uploadFrom(new File(filePath));
                Mapper.getDynamoDBMapper().save(recipeItem);

            }

        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void uploadImage(final String infoName, final String filePath){
        final String name = infoName;
        final String[] key = filePath.split("/");
        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.InfoDO infoItem;
            @Override
            public void run() {
                infoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.InfoDO.class,
                        infoName,
                        "fresh");
                Log.d("why",Mapper.bucketName);
                infoItem.setInfoImage(Mapper.getDynamoDBMapper().createS3Link(Region.US_Standard,Mapper.bucketName,"Info/" + infoName));
                infoItem.getInfoImage().uploadFrom(new File(filePath));
                Mapper.getDynamoDBMapper().save(infoItem);

            }

        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void createPost(String title, String recipeId) {
        final String post_title = title;
        final String ID = recipeId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.PostDO postItem = new com.example.dldke.foodbox.PostDO();
                final com.example.dldke.foodbox.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RecipeDO.class,
                        ID);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
                String dateS = sdf.format(date).toString();

                postItem.setPostId(userId + dateS);
                postItem.setDate(dateS);
                postItem.setWriter(userId);
                postItem.setTitle(post_title);
                postItem.setRecipeId(ID);

                Mapper.getDynamoDBMapper().save(postItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void createComment(String postId, String content) {
        final String postID = postId;
        final String text = content;


        Thread thread = new Thread(new Runnable() {
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
                comment1.setUserId(userId);
                comment1.setContent(text);
                List<PostDO.Comment> commentList = postItem.getCommentList();
                commentList.add(comment1);
                postItem.setCommentList(commentList);
                Mapper.getDynamoDBMapper().save(postItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<InfoDO> scanInfo(String section) {
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

    public static List<RefrigeratorDO.Item> scanRefri() {

        returnThread thread = new returnThread(new CustomRunnable() {
            com.example.dldke.foodbox.RefrigeratorDO Refri;
            @Override
            public void run() {
                Refri = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        userId);
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

    public static InfoDO searchFood(String name) {

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

    public static void updateDueDate(String name, Integer dueDate) {
        final String itemName = name;
        final Integer newDueDate = dueDate;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        userId);
                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName)) {
                        foodItem.getItem().get(i).setDueDate(newDueDate);
                        break;
                    }
                }

                Mapper.getDynamoDBMapper().save(foodItem, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCount(String name, Integer count)
    {
        final String itemName = name;
        final Integer minus = count;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        userId);

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
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteFood(String name) {
        final String itemName = name;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        userId);
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

    public static void createRefrigerator() {
        final com.example.dldke.foodbox.RefrigeratorDO refrigeratorItem = new com.example.dldke.foodbox.RefrigeratorDO();

        refrigeratorItem.setUserId(userId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

    public static RefrigeratorDO.Item createFood(InfoDO item, Double count) {

        RefrigeratorDO.Item food = new RefrigeratorDO.Item();
        food.setName(item.getName());
        food.setSection(item.getSection());
        food.setKindOf(item.getKindOf());
        food.setDueDate(item.getDueDate());
        food.setCount(count);

        return food;
    }

    public static void putFood(List<RefrigeratorDO.Item> foods) {
        final List<RefrigeratorDO.Item> foods_list = foods;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.RefrigeratorDO refrigeratorItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.RefrigeratorDO.class,
                        userId);

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

    public static void createMyCommunity() {
        final com.example.dldke.foodbox.MyCommunityDO myCommunityDO = new com.example.dldke.foodbox.MyCommunityDO();

        myCommunityDO.setUserId(userId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(myCommunityDO);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static MyCommunityDO searchMyCommunity() {
        returnThread thread = new returnThread(new CustomRunnable() {

            com.example.dldke.foodbox.MyCommunityDO community;
            @Override
            public void run() {
                community = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.MyCommunityDO.class,
                        userId);
            }

            @Override
            public Object getResult(){
                return community;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        MyCommunityDO commuItem = (MyCommunityDO)thread.getResult();

        return commuItem;
    }

    public static List<PostDO> searchPost(String title) {
        final String postTitle = title;
        returnThread thread = new returnThread(new CustomRunnable() {

            List<PostDO> post;
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS).withAttributeValueList(new AttributeValue().withS(postTitle));
                scanExpression.addFilterCondition("title", condition);
                post = Mapper.getDynamoDBMapper().scan(PostDO.class, scanExpression);
            }

            @Override
            public Object getResult(){
                return post;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<PostDO> postItem = (List<PostDO>)thread.getResult();

        return postItem;
    }

    public static void addRecipeInMyCommunity(final String recipeId) {

        Thread thread = new Thread(new Runnable() {
            com.example.dldke.foodbox.MyCommunityDO myCommunityDO;

            @Override
            public void run() {
                myCommunityDO = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.MyCommunityDO.class,
                        userId);
                List<String> tmpList = myCommunityDO.getMyRecipes();
                tmpList.add(recipeId);
                myCommunityDO.setMyRecipes(tmpList);
                Mapper.getDynamoDBMapper().save(myCommunityDO);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private class PoolConfig{
        @SerializedName("Default")
        public Config config;

        class Config{
            @SerializedName("PoolId")
            public String poolId;

            @SerializedName("AppClientId")
            public String clientId;

            @SerializedName("AppClientSecret")
            public String clientSecret;

            @SerializedName("Region")
            public String region;

        }
    }
}
