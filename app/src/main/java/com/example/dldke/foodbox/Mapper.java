package com.example.dldke.foodbox;

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
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Mapper {

    private static DynamoDBMapper dynamoDBMapper;

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
                    .awsConfiguration(
                            AWSMobileClient.getInstance().getConfiguration())
                    .build();

    }

    public static RecipeDO.Ingredient createIngredient(String name, Double count)
    {
        RecipeDO.Ingredient ingredient = new RecipeDO.Ingredient();
        ingredient.setIngredientName(name);
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


    public static void createRecipe(List<RecipeDO.Ingredient> ingredient) {
        final com.example.dldke.foodbox.RecipeDO recipeItem = new com.example.dldke.foodbox.RecipeDO();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        String dateS = sdf.format(date).toString();

        recipeItem.setRecipeId("test1" + dateS);
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
    }

    public static void createFullRecipe(String recipeId, String name, List<RecipeDO.Spec> spec)
    {
        final String ID = recipeId;
        final String recipe_name = name;
        final List<RecipeDO.Spec> specList = spec;

        Thread thread = new Thread(new Runnable() {
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

                postItem.setPostId("kitawo" + dateS);
                postItem.setDate(dateS);
                postItem.setWriter("kitawo324");
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

    public static void createComment(String postId, String userId, String content) {
        final String postID = postId;
        final String userID = userId;
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
                comment1.setUserId(userID);
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

    public static List<RefrigeratorDO.Item> scanRefri(String userId) {
        final String ID = userId;

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

    public static void updateDueDate(String userId, String name, Integer dueDate) {
        final String ID = userId;
        final String itemName = name;
        final Integer newDueDate = dueDate;

        Thread thread = new Thread(new Runnable() {
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
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCount(String userId, String name, Integer count)
    {
        final String ID = userId;
        final String itemName = name;
        final Integer minus = count;
        Thread thread = new Thread(new Runnable() {
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
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteFood(String userId, String name) {

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

    public static void createRefrigerator(String userId) {
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

    public static void putFood(String userId, List<RefrigeratorDO.Item> foods) {
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
}
