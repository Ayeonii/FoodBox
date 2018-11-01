package com.example.dldke.foodbox.DataBaseFiles;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient createIngredient(InfoDO item, Double count)
    {
        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient ingredient = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient();
        ingredient.setIngredientName(item.getName());
        ingredient.setIngredientCount(count);
        return ingredient;
    }

    public static com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec createSpec(List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient> ingredient, String method, String fire, Integer minute)
    {
        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec spec1 = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec();
        List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient> spec1IngredientList = spec1.getSpecIngredient();

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


    public static String createRecipe(List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient> ingredient) {
        final com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        String dateS = sdf.format(date).toString();

        recipeItem.setRecipeId(userId + dateS);
        recipeItem.setDate(dateS);

        List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient> tmpIngredientList = recipeItem.getIngredient();
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

    public static com.example.dldke.foodbox.DataBaseFiles.RecipeDO searchRecipe(String recipeId) {

        final String recipe_id = recipeId;
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
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
        com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = (com.example.dldke.foodbox.DataBaseFiles.RecipeDO) thread.getResult();

        return recipeItem;
    }

    public static void createFullRecipe(String recipeId, String name, List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> spec)
    {
        final String ID = recipeId;
        final String recipe_name = name;
        final List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> rspecList = spec;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        ID);

                com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Detail detail = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Detail();
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

    public static void createChefRecipe(String name, List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> spec)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        final String dateS = sdf.format(date).toString();

        final String ID = userId + dateS;

        final String recipe_name = name;
        final List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> rspecList = spec;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO();
                recipeItem.setRecipeId(ID);
                recipeItem.setDate(dateS);

                com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Detail detail = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Detail();
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

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
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

            com.example.dldke.foodbox.DataBaseFiles.InfoDO infoItem;
            @Override
            public void run() {
                infoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.InfoDO.class,
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

    public static void downLoadImage(final String infoName, final String locatePath){
        //final String name = infoName;

        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.InfoDO infoItem;
            @Override
            public void run() {
                URL url;
                infoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.InfoDO.class,
                        infoName,
                        "fresh");
                // Log.d("why",Mapper.bucketName);
                infoItem.getInfoImage().downloadTo(new File(locatePath + infoName + ".jpg"));
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
                final com.example.dldke.foodbox.DataBaseFiles.PostDO postItem = new com.example.dldke.foodbox.DataBaseFiles.PostDO();
                final com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
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
                final com.example.dldke.foodbox.DataBaseFiles.PostDO postItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.PostDO.class,
                        postID);

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
                String dateS = sdf.format(date).toString();

                com.example.dldke.foodbox.DataBaseFiles.PostDO.Comment comment1 = new com.example.dldke.foodbox.DataBaseFiles.PostDO.Comment();
                comment1.setDate(dateS);
                comment1.setUserId(userId);
                comment1.setContent(text);
                List<com.example.dldke.foodbox.DataBaseFiles.PostDO.Comment> commentList = postItem.getCommentList();
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

    public static List<InfoDO> scanSection(String section) {
        final com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = new com.example.dldke.foodbox.DataBaseFiles.InfoDO();

        final String sectionName = section;

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
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

   public static List<InfoDO> scanKindOf(String kindOf) {
        final com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = new com.example.dldke.foodbox.DataBaseFiles.InfoDO();

        final String kindName = kindOf;

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            List<InfoDO> itemList;
            @Override
            public void run() {

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(kindName));
                scanExpression.addFilterCondition("kindOf", condition);
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

    public static boolean checkFirst() {

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO Refri;
            @Override
            public void run() {
                Refri = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
            }
            @Override
            public Object getResult(){
                return Refri.getUserId();
            }
        });

        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        Object refri_item = thread.getResult();
        if(refri_item.equals(null)){
            return true;
        }
        return false;
    }

    public static List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> scanRefri() {

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO Refri;
            @Override
            public void run() {
                Refri = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
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
        List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> refri_item = (List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item>)thread.getResult();

        return refri_item;

    }

    public static InfoDO searchFood(String name) {

        final String foodName = name;
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem;
            @Override
            public void run() {
                foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.InfoDO.class,
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
        com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = (InfoDO)thread.getResult();

        return foodItem;
    }

    public static void updateDueDate(String name, Integer dueDate) {
        final String itemName = name;
        final Integer newDueDate = dueDate;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
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

                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
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
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
                int index = 0;
                for(int i =0; i < foodItem.getItem().size(); i++)
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
        final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO refrigeratorItem = new com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO();

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

    public static com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item createFood(InfoDO item, Double count) {

        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item food = new com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item();
        food.setName(item.getName());
        food.setSection(item.getSection());
        food.setKindOf(item.getKindOf());
        food.setDueDate(item.getDueDate());
        food.setCount(count);

        return food;
    }

    public static void putFood(List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> foods) {
        final List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> foods_list = foods;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO refrigeratorItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
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
        final com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO myCommunityDO = new com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO();

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

    public static com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO searchMyCommunity() {
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO community;
            @Override
            public void run() {
                community = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
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
        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO commuItem = (MyCommunityDO)thread.getResult();

        return commuItem;
    }

    public static List<com.example.dldke.foodbox.DataBaseFiles.PostDO> searchPost(String title) {
        final String postTitle = title;
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            List<com.example.dldke.foodbox.DataBaseFiles.PostDO> post;
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS).withAttributeValueList(new AttributeValue().withS(postTitle));
                scanExpression.addFilterCondition("title", condition);
                post = Mapper.getDynamoDBMapper().scan(com.example.dldke.foodbox.DataBaseFiles.PostDO.class, scanExpression);
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
        List<com.example.dldke.foodbox.DataBaseFiles.PostDO> postItem = (List<PostDO>)thread.getResult();

        return postItem;
    }

    public static void addRecipeInMyCommunity(final String recipeId) {

        Thread thread = new Thread(new Runnable() {
            com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO myCommunityDO;

            @Override
            public void run() {
                myCommunityDO = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
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
