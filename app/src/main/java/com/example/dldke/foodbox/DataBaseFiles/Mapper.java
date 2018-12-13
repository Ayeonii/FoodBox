package com.example.dldke.foodbox.DataBaseFiles;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.Region;
import com.example.dldke.foodbox.PencilRecipe.CurrentDate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Mapper {
    private static CurrentDate currentDate = new CurrentDate();
    private static boolean isFavorite = false;
    private static boolean isMyRecipe = false;
    private static DynamoDBMapper dynamoDBMapper;
    private static String userId;
    private static String bucketName;
    private Mapper(){}

    public static DynamoDBMapper getDynamoDBMapper(){
        return dynamoDBMapper;
    }
    public static void setDynamoDBMapper(AWSMobileClient awsMobileClient){

        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(awsMobileClient.getCredentials());
            dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                    .awsCredentialsProviderForS3(awsMobileClient.getCredentialsProvider())
                    .build();

    }
    public static void setUserId(Context context){
        try{
            Log.e("username",AWSMobileClient.getInstance().getUsername());
            CognitoUserPool cognitoUserPool = new CognitoUserPool(context,AWSMobileClient.getInstance().getConfiguration());
            CognitoUser user = cognitoUserPool.getCurrentUser();
            userId = user.getUserId();
        }
        catch (Exception e){
            userId = GoogleSignIn.getLastSignedInAccount(context).getEmail();
        }
        Log.e("Login userId",userId);

    }
    public static String getUserId(){
        return userId;
    }


    public static void setBucketName(Context context){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(AWSMobileClient.getInstance().getConfiguration().toString());
        bucketName = element.getAsJsonObject().get("S3TransferUtility").getAsJsonObject().get("Default").getAsJsonObject().get("Bucket").getAsString();
    }
    public static void updateRecipePushEndPoint(TargetingClient target){
        List<String> list = new ArrayList<String>();

        MyCommunityDO mycommu = searchMyCommunity();
        List<String> my_recipe = mycommu.getMyRecipes();

        if(my_recipe != null)
        {
            boolean isDone = false;
            int i;
            for(i = 0; i < my_recipe.size(); i++){
                if((searchRecipe(my_recipe.get(i)).getIng()) == 1){
                    list.add("작성중");
                    break;
                }
                else if((searchRecipe(my_recipe.get(i)).getIng()) == 0){
                    isDone = true;
                }
            }
            if(i == my_recipe.size() && isDone)
                list.add("작성완료");
            else if(i == my_recipe.size() && !(isDone))
                list.add("사용완료");


            Log.e("endpointId",target.currentEndpoint().getEndpointId());
            target.addAttribute("flag",list);
            target.updateEndpointProfile();
        }


    }

    public static void updateUrgentPushEndPoint(TargetingClient target){
        List<String> list = new ArrayList<String>();

        List<RecipeDO.Ingredient> urgent = scanUrgentMemo();
        try {
            if(urgent.get(0) != null) {
                Log.e("설마?","유통기한");
                list.add("유통기한");
                Log.e("endpointId",target.currentEndpoint().getEndpointId());
                target.addAttribute("urgent",list);
                target.updateEndpointProfile();
            }

        }
        catch(Exception e){
            Log.e("정말","유통기한X");
            list.add("유통기한X");
            Log.e("endpointId",target.currentEndpoint().getEndpointId());
            target.addAttribute("urgent",list);
            target.updateEndpointProfile();
        }
    }

    //String name => InfoDO item
    //ingredient.setIngredientName(name) => ingredient.setIngredientName(item.getFoodname())
    public static com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient createIngredient(String name, Double count)
    {
        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient ingredient = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient();
        //ingredient.setIngredientName(item.getFoodname());
        ingredient.setIngredientName(name);
        ingredient.setIngredientCount(count);
        return ingredient;
    }

    public static void createUserInfo(){
        final com.example.dldke.foodbox.DataBaseFiles.UserDO userInfo= new com.example.dldke.foodbox.DataBaseFiles.UserDO();

        userInfo.setUserId(userId);
        userInfo.setIsCookingClass(false);
        userInfo.setPoint(0);
        userInfo.setTheme("기본 테마");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(userInfo);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static UserDO searchUserInfo(){

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            UserDO userInfo;

            @Override
            public void run() {
                userInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);
            }

            @Override
            public Object getResult(){
                return userInfo;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        UserDO userInfo  = (UserDO)thread.getResult();

        return userInfo;
    }

    public static void updateUserInfo(String nickName, boolean isCook, String registNum){

        final String nickN = nickName;
        final boolean isCooking = isCook;
        final String registN = registNum;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserDO userInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);

                userInfo.setNickname(nickN);
                userInfo.setIsCookingClass(isCooking);
                userInfo.setRegisterNumber(registN);
                Mapper.getDynamoDBMapper().save(userInfo);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateTheme(String theme, int point){

        final String userTheme = theme;
        final int userPoint = point;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserDO userInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);

                userInfo.setTheme(userTheme);
                userInfo.setPoint(userPoint);
                Mapper.getDynamoDBMapper().save(userInfo);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


/****************************User Profile***********************************/

/*
public static String getImageUrlUser(final String userid){

    returnThread thread = new returnThread(new CustomRunnable() {

        com.example.dldke.foodbox.DataBaseFiles.UserDO userItem;
        URL url;
        @Override
        public void run() {
            userItem = Mapper.getDynamoDBMapper().load(
                    com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                    userid);
            // Log.d("why",Mapper.bucketName);
            try {
                url = userItem.getProfileImage().getAmazonS3Client().getUrl(userItem.getProfileImage().getBucketName(), "Users/" + userid + ".jpg");
                Log.d("gerUserProfile", url.toString());
            }catch (NullPointerException e){

            }
        }
        @Override
        public Object getResult(){
            return url.toString();
        }
    });
    thread.start();
    try{
        thread.join();
    }catch (Exception e){
        e.printStackTrace();
    }
    String url = (String)thread.getResult();
    return url;
}*/


public static String getImageUrlUser(final String userid){

    returnThread thread = new returnThread(new CustomRunnable() {

        com.example.dldke.foodbox.DataBaseFiles.UserDO userItem;
        URL url;
        @Override
        public void run() {
            userItem = Mapper.getDynamoDBMapper().load(
                    com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                    userid);
            // Log.d("why",Mapper.bucketName);
            try {
                url = userItem.getProfileImage().getAmazonS3Client().getUrl(userItem.getProfileImage().getBucketName(), "Users/" + userid + ".jpg");
                Log.d("gerUserProfile", url.toString());
            }catch (Exception e){
                url = null;
            }
        }
        @Override
        public Object getResult(){
            if(url == null){
                return url;
            }else {
                return url.toString();
            }
        }
    });
    thread.start();
    try{
        thread.join();
    }catch (Exception e){
        e.printStackTrace();
    }
    String url;
    if(thread.getResult()==null){
        url = "default";
    }
    else{
        url = (String)thread.getResult();
    }
    return url;
}




    /************************* Refrigerator Section Method *********************************/

    //Check Refigerator Created
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

        try{
            Object refri_item = thread.getResult();
        }
        catch (NullPointerException e){
            return true;
        }

        return false;
    }

    public static void checkAndCreateFirst() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UserDO user = Mapper.getDynamoDBMapper().load(
                            com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                            userId);
                    user.getUserId();
                }catch (NullPointerException e){
                    createUserInfo();
                }

                try {
                    RefrigeratorDO Refri = Mapper.getDynamoDBMapper().load(
                            com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                            userId);
                    Refri.getUserId();
                }catch (NullPointerException e){
                    createRefrigerator();
                }

                try {
                    MemoDO memo = Mapper.getDynamoDBMapper().load(
                            com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                            userId);
                    memo.getUserId();
                }catch (NullPointerException e){
                    createMemo();
                }

                try {
                    MyCommunityDO mycommu = Mapper.getDynamoDBMapper().load(
                            com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                            userId);
                    mycommu.getUserId();
                }catch (NullPointerException e){
                    createMyCommunity();
                }

            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //Create Refrigerator
    public static void createRefrigerator() {
        final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO refrigeratorItem = new com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO();

        refrigeratorItem.setUserId(userId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("test", "Mapper.createRefrigerator");
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

    public static com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item createFood(InfoDO item, Double count, String dueDate, Boolean isFrozen) {

        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item food = new com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item();
        food.setName(item.getName());
        food.setSection(item.getSection());
        food.setKindOf(item.getKindOf());
        food.setDueDate(dueDate);
        food.setCount(count);
        food.setIsFrozen(isFrozen);

        Log.e("getFoodname",""+food.getName());
        Log.e("getSection",""+food.getSection());
        Log.e("getDueDate",""+food.getDueDate());
        Log.e("getCount",""+food.getCount());

        return food;
    }

    public static com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item createNonFood(String name,String section,Double count, String dueDate, Boolean isFrozen) {

        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item food = new com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item();
        food.setName(name);
        food.setSection(section);
        food.setIsFrozen(isFrozen);
        food.setDueDate(dueDate);
        food.setCount(count);

        Log.e("getFoodname",""+food.getName());
        Log.e("getSection",""+food.getSection());
        Log.e("getDueDate",""+food.getDueDate());
        Log.e("getCount",""+food.getCount());

        return food;
    }


    public static void putFood(final List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> foods) {
        final List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> foods_list = foods;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO refrigeratorItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);

                List<RefrigeratorDO.Item> r_item = refrigeratorItem.getItem();
                for(int i = 0; i < foods_list.size(); i++) {
                    boolean temp = false;
                    for(int j = 0; j < r_item.size(); j++){
                        if((r_item.get(j).getName().equals(foods_list.get(i).getName())) && (r_item.get(j).getDueDate().equals(foods_list.get(i).getDueDate()))){
                            r_item.get(j).setCount(r_item.get(j).getCount() + foods_list.get(i).getCount());
                            temp = true;
                        }
                    }
                    if(temp == false)
                        r_item.add(foods_list.get(i));

                    Log.e("putFoodIn"+i+"번째",""+r_item.get(i));
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


    public static List<com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.Item> scanRefri() {

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO Refri;
            @Override
            public void run() {
                Refri = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
                Log.d("test", "Mapper.scanRefri");
                Log.d("test", "userId : "+ userId);
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


    public static List<InfoDO> scanKindof(String kindof) {
        final com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = new com.example.dldke.foodbox.DataBaseFiles.InfoDO();

        final String kindofName = kindof;

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            List<InfoDO> itemList;
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(kindofName));
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


    public static InfoDO searchFood(String name, String section) {

        final String foodName = name;
        final String sectionName = section;
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem;
            @Override
            public void run() {
                try {
                    foodItem = Mapper.getDynamoDBMapper().load(
                            com.example.dldke.foodbox.DataBaseFiles.InfoDO.class,
                            foodName,
                            sectionName);
                }catch(DynamoDBMappingException e){

                }

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

    public static void updateDueDate(String name, String dueDate_old, String dueDate_new) {
        final String itemName = name;
        final String oldDueDate = dueDate_old;
        final String newDueDate = dueDate_new;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName) && foodItem.getItem().get(i).getDueDate().equals(oldDueDate)) {
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

    public static void updateCount(String name, String dueDate_old, Double count_new) {
        final String itemName = name;
        final String oldDueDate = dueDate_old;
        final Double newCount = count_new;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
                for(int i = 0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName) && foodItem.getItem().get(i).getDueDate().equals(oldDueDate)) {
                        foodItem.getItem().get(i).setCount(newCount);
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

    public static void minusCountwithDueDate(String name, String dueDate, Double count)
    {
        final String itemName = name;
        final Double minus = count;
        final String itemDueDate = dueDate;
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
                    if(foodItem.getItem().get(i).getName().equals(itemName) && foodItem.getItem().get(i).getDueDate().equals(itemDueDate)) {
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

    public static void minusCount(String name, Double count)
    {
        final String itemName = name;
        final Double minus = count;
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


    public static void deleteFood(String name, String dueDate) {
        final String itemName = name;
        final String itemDueDate = dueDate;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO foodItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO.class,
                        userId);
                int index = 0;
                for(int i =0; i < foodItem.getItem().size(); i++)
                {
                    if(foodItem.getItem().get(i).getName().equals(itemName) && foodItem.getItem().get(i).getDueDate().equals(itemDueDate)) {
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


    /***************** Recipe Section Method ******************/

    // Create HalfRecipe
    public static String createRecipe(List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Ingredient> ingredient, String simpleName) {
        final com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem = new com.example.dldke.foodbox.DataBaseFiles.RecipeDO();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        String dateS = sdf.format(date).toString();

        recipeItem.setRecipeId(userId + dateS);
        recipeItem.setDate(dateS);
        recipeItem.setSimpleName(simpleName);

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

    //Search Recipe
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
    //Register Food each Spec Image in S3
    public static void attachSpecImage(String recipeId, final String filePath, final int index){
        final String recipe_id = recipeId;
        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipe_id);
                recipeItem.getDetail().getSpecList().get(index).setSpecImage(Mapper.getDynamoDBMapper().createS3Link(Region.AP_Seoul,Mapper.bucketName,"Recipes/"+recipe_id+"_"+index+".jpg"));
                recipeItem.getDetail().getSpecList().get(index).getSpecImage().uploadFrom(new File(filePath));
                recipeItem.getDetail().getSpecList().get(index).getSpecImage().setAcl(CannedAccessControlList.PublicRead);
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
    public static String getImageUrlSpec(final String recipeId, final int index){
        returnThread thread = new returnThread(new CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            URL url;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipeId);
                url = recipeItem.getDetail().getSpecList().get(index).getSpecImage().getAmazonS3Client().getUrl(recipeItem.getRecipeImage().getBucketName(),"Recipes/"+recipeId+"_"+index+".jpg");
                Log.d("getImageUrl",url.toString());
            }
            @Override
            public Object getResult(){
                return url.toString();
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        String url = (String)thread.getResult();
        return url;
    }

    //Create FullRecipe
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


    //Create Chef FullRecipe
    public static String createChefRecipe(String name, List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> spec)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        final String dateS = sdf.format(date).toString();

        final String ID = userId + dateS;

        final String recipe_name = name;
        final List<com.example.dldke.foodbox.DataBaseFiles.RecipeDO.Spec> rspecList = spec;
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {

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

            @Override
            public Object getResult(){
                return ID;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        String recipeId = (String) thread.getResult();

        return recipeId;

    }

    //Register Recipe in MyCommunity
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

    //Register Food Spec Image in S3
    public static void attachRecipeImage(String recipeId, final String filePath){
        final String recipe_id = recipeId;
        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipe_id);
                Log.d("why",Mapper.bucketName);
                recipeItem.setRecipeImage(Mapper.getDynamoDBMapper().createS3Link(Region.AP_Seoul,Mapper.bucketName,"Recipes/"+recipe_id+".jpg"));
                recipeItem.getRecipeImage().uploadFrom(new File(filePath));
                recipeItem.getRecipeImage().setAcl(CannedAccessControlList.PublicRead);
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

    public static void deleteRecipe(String recipeId) {
        final String recipe_Id = recipeId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO communityItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);
                int index = 0;
                for(int i =0; i < communityItem.getMyRecipes().size(); i++)
                {
                    if(communityItem.getMyRecipes().get(i).equals(recipe_Id)) {
                        communityItem.getMyRecipes().remove(i);
                        break;
                    }
                }
                Mapper.getDynamoDBMapper().save(communityItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getImageUrlRecipe(final String recipeId){
        returnThread thread = new returnThread(new CustomRunnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            URL url;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipeId);
                // Log.d("why",Mapper.bucketName);
                url = recipeItem.getRecipeImage().getAmazonS3Client().getUrl(recipeItem.getRecipeImage().getBucketName(),"Recipes/"+recipeId+".jpg");
                Log.d("getImageUrl",url.toString());
            }
            @Override
            public Object getResult(){
                return url.toString();
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        String url = (String)thread.getResult();
        return url;
    }


    public static void updatePointInfo(Integer Point){

        final Integer point = Mapper.searchUserInfo().getPoint()+Point;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserDO userInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);

                userInfo.setPoint(point);
                Mapper.getDynamoDBMapper().save(userInfo);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updatePassword(String recipe_id,String password){

        final String inputPassword = password;
        final String recipeId = recipe_id;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RecipeDO recipeInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipeId);

                recipeInfo.setPassword(inputPassword);
                Mapper.getDynamoDBMapper().save(recipeInfo);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateIngredient(List<RecipeDO.Ingredient> input, String recipeId){
        final String recipe_id = recipeId;
        final List<RecipeDO.Ingredient> ingredient_list = input;
        Thread thread = new Thread(new Runnable() {
            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipe_id);
                recipeItem.setIngredient(ingredient_list);
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

    public static void updateIngInfo(Integer ing, String recipe_id){

        final Integer Ing = ing;
        final String recipeId = recipe_id;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RecipeDO recipeDO = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipeId);

                recipeDO.setIng(Ing);
                Mapper.getDynamoDBMapper().save(recipeDO);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updateIsShared(String recipe_id){

        final String recipeId = recipe_id;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RecipeDO recipe = Mapper.searchRecipe(recipeId);
                recipe.setIsShare(true);
                Mapper.getDynamoDBMapper().save(recipe);
            }

        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updateIsPost(String recipe_id){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RecipeDO recipe = Mapper.searchRecipe(recipe_id);
                recipe.setIsPost(true);
                Mapper.getDynamoDBMapper().save(recipe);
            }

        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    /***************** Up/Download Image Method  *************************/

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
                        "etc");
                Log.d("why",Mapper.bucketName);
                infoItem.setInfoImage(Mapper.getDynamoDBMapper().createS3Link(Region.AP_Seoul,Mapper.bucketName,"Info/" + infoName));
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

    public static void downLoadImage(final String infoName, final String locatePath, final String sectionName){
        //final String name = infoName;
        final String section = sectionName;
        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.InfoDO infoItem;
            @Override
            public void run() {
                URL url;
                infoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.InfoDO.class,
                        infoName,
                        section);
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

    public static void downLoadImageRecipe(final String recipeId, final String locatePath){

        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.RecipeDO recipeItem;
            @Override
            public void run() {
                URL url;
                recipeItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.RecipeDO.class,
                        recipeId);
                // Log.d("why",Mapper.bucketName);
                recipeItem.getRecipeImage().downloadTo(new File(locatePath + ".jpg"));
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /****************** Community Section Method ************************/

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

    public static boolean matchFavorite(String postId) {
        final String post_Id = postId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO communityItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);

                for(int i =0; i < communityItem.getFavorites().size(); i++)
                {
                    if(communityItem.getFavorites().get(i).equals(post_Id)) {
                        isFavorite = true;
                        break;
                    }
                    else{
                        isFavorite = false;
                    }
                }

            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isFavorite;
    }

    public static boolean matchMyRecipe(String recipe_id) {
        final String recipeId = recipe_id;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO communityItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);

                for(int i =0; i < communityItem.getFavorites().size(); i++)
                {
                    if(communityItem.getMyRecipes().get(i).equals(recipeId)) {
                        isMyRecipe = true;
                        break;
                    }
                    else{
                        isMyRecipe = false;
                    }
                }

            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isMyRecipe;
    }

    public static List<PostDO> scanFavorite() {

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO myCommunity;

            List<String> favorites;
            List<PostDO> favoritePosts = new ArrayList<>();

            @Override
            public void run() {
                myCommunity = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);
                favorites = myCommunity.getFavorites();
                for(int i = 0; i < favorites.size(); i++)
                {
                    Log.e("favorite:::",""+favorites.get(i));
                    PostDO post = searchPost("postId",favorites.get(i)).get(0);

                    favoritePosts.add(post);
                }
            }
            @Override
            public Object getResult(){
                return favoritePosts;
            }
        });

        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<PostDO> favoriteList = (List<com.example.dldke.foodbox.DataBaseFiles.PostDO>)thread.getResult();

        return favoriteList;
    }

    public static void deleteFavorite(String postId) {
        final String post_Id = postId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO communityItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);
                int index = 0;
                for(int i =0; i < communityItem.getFavorites().size(); i++)
                {
                    if(communityItem.getFavorites().get(i).equals(post_Id)) {
                        communityItem.getFavorites().remove(i);
                        break;
                    }
                }
                Mapper.getDynamoDBMapper().save(communityItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void addFavoriteInMyCommunity(final String postId) {

        Thread thread = new Thread(new Runnable() {
            com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO myCommunityDO;
            @Override
            public void run() {
                myCommunityDO = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MyCommunityDO.class,
                        userId);
                List<String> tmpList = myCommunityDO.getFavorites();
                tmpList.add(postId);
                myCommunityDO.setFavorites(tmpList);
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

    public static List<PostDO> recommendRecipe()    {
        List<PostDO> entirePost = scanPost();
        List<RecipeDO.Ingredient> urgentIngredient = scanUrgentMemo();
        List<PostDO> resultPost = new ArrayList<>();
        int urgentIngredientNum = urgentIngredient.size();

        for(int i = 0; i < entirePost.size(); i++)
        {
            List<RecipeDO.Ingredient> postIngredient = searchRecipe(entirePost.get(i).getRecipeId()).getIngredient();
            int tempPostIngredientNum = postIngredient.size();
            int compareCount = 0;
            for(int j = 0; j < tempPostIngredientNum; j++)
            {
                for(int k = 0; k < urgentIngredientNum; k++)
                {
                    if((postIngredient.get(j).getIngredientName().equals(urgentIngredient.get(k).getIngredientName())) && (postIngredient.get(j).getIngredientCount() <= urgentIngredient.get(k).getIngredientCount()))
                        compareCount++;
                }
            }
            if(compareCount >= 2)
                resultPost.add(entirePost.get(i));

        }
        return resultPost;
    }




    /************************** Post Section Method  ******************************/

    //Create Post
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

                postItem.setPostId(dateS + userId);
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


    //Create Comment
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm a");
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

    public static void deletePost(String postId) {
        final String post_Id = postId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.PostDO postItem = new PostDO();

                postItem.setPostId(post_Id);
                dynamoDBMapper.delete(postItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static List<com.example.dldke.foodbox.DataBaseFiles.PostDO> searchPost(String attribute, String identifier) {
        final String postAttribute = attribute;
        final String postIdentifier = identifier;

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            List<com.example.dldke.foodbox.DataBaseFiles.PostDO> post;
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS).withAttributeValueList(new AttributeValue().withS(postIdentifier));
                scanExpression.addFilterCondition(postAttribute, condition);
                post = Mapper.getDynamoDBMapper().parallelScan(com.example.dldke.foodbox.DataBaseFiles.PostDO.class, scanExpression,4);
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

        Log.e("","postItem.get(0)"+postItem.get(0));
        return postItem;
    }

    public static List<com.example.dldke.foodbox.DataBaseFiles.PostDO> scanPost() {
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            List<com.example.dldke.foodbox.DataBaseFiles.PostDO> post;

            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS).withAttributeValueList(new AttributeValue().withS(" "));
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
        List<com.example.dldke.foodbox.DataBaseFiles.PostDO> postItem = (List<PostDO>)thread.getResult();

        return postItem;
    }



    /********************* Memo Section Method ************************/

    public static void createMemo() {
        final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = new com.example.dldke.foodbox.DataBaseFiles.MemoDO();

        memoItem.setUserId(userId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mapper.getDynamoDBMapper().save(memoItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateUrgentMemo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                        userId);

                List<RecipeDO.Ingredient> urgent = new ArrayList<>();
                List<RefrigeratorDO.Item> all = scanRefri();

                for(int t = 0; t < all.size(); t++)
                {
                    String oldstring = all.get(t).getDueDate();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyyMMdd").parse(oldstring);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date today = new Date();
                    long result = (date.getTime() - today.getTime());
                    int calDate = (int)(Math.ceil(result/(24*60*60*1000.0)));

                    if(calDate <= 3)
                    {
                        RecipeDO.Ingredient ig = new RecipeDO.Ingredient();
                        ig.setIngredientCount(all.get(t).getCount());
                        ig.setIngredientDuedate(all.get(t).getDueDate());
                        ig.setIngredientName(all.get(t).getName());
                        urgent.add(ig);
                    }
                }
                memoItem.setUrgent(urgent);
                Mapper.getDynamoDBMapper().save(memoItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<RecipeDO.Ingredient> scanUrgentMemo() {
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            List<RecipeDO.Ingredient> urgent;
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                        userId);
                urgent = memoItem.getUrgent();
            }

            @Override
            public Object getResult(){
                return urgent;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<RecipeDO.Ingredient> urgentList = (List<RecipeDO.Ingredient>)thread.getResult();

        return urgentList;
    }

    public static void appendToBuyMemo(final List<RecipeDO.Ingredient> inputList) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                        userId);

                List<RecipeDO.Ingredient> exist = memoItem.getTobuy();

                for(int i = 0; i < inputList.size(); i++)
                {
                    boolean temp = false;
                    for(int j = 0; j < exist.size(); j++)
                    {
                        if(exist.get(j).getIngredientName().equals(inputList.get(i).getIngredientName()))
                        {
                            exist.get(j).setIngredientCount(exist.get(j).getIngredientCount() + inputList.get(i).getIngredientCount());
                            temp = true;
                        }
                    }
                    if(temp == false)
                        exist.add(inputList.get(i));
                }

                memoItem.setTobuy(exist);

                Mapper.getDynamoDBMapper().save(memoItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateToBuyMemo(List<RefrigeratorDO.Item> compare){
        final List<RecipeDO.Ingredient> toBuyMemo = scanToBuyMemo();
        //final List<RefrigeratorDO.Item> myRefri = scanRefri();
        final List<RefrigeratorDO.Item> toPut = compare;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                        userId);
                int toBuyNum = toBuyMemo.size();
                //int refriNum = myRefri.size();

                for(int i = 0; i < toBuyMemo.size(); i++)
                {
                    for(int j = 0; j < toPut.size(); j++)
                    {
                        if(toPut.get(j).getName().equals(toBuyMemo.get(i).getIngredientName()))
                            toBuyMemo.get(i).setIngredientCount(toBuyMemo.get(i).getIngredientCount() - toPut.get(j).getCount());
                    }
                }

                for(int k = 0; k < toBuyMemo.size(); k++)
                {
                    if(toBuyMemo.get(k).getIngredientCount() <= 0) {
                        toBuyMemo.remove(k);
                        k--;
                    }
                }

                memoItem.setTobuy(toBuyMemo);
                getDynamoDBMapper().save(memoItem);
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<RecipeDO.Ingredient> scanToBuyMemo() {
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            List<RecipeDO.Ingredient> toBuy;
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.MemoDO memoItem = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.MemoDO.class,
                        userId);
                toBuy = memoItem.getTobuy();
            }

            @Override
            public Object getResult(){
                return toBuy;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<RecipeDO.Ingredient> toBuyList = (List<RecipeDO.Ingredient>)thread.getResult();

        return toBuyList;
    }


    /********************* Appreciate Receipt Section Method **********************/

    public static class RecipeMatching{
        private List<InfoDO> matchingList = new ArrayList<>();
        private List<String> nonMatchingList = new ArrayList<>();

        public List<InfoDO> getMatchingList() {
            return matchingList;
        }

        public void setMatchingList(List<InfoDO> matchingList) {
            this.matchingList = matchingList;
        }

        public List<String> getNonMatchingList() {
            return nonMatchingList;
        }

        public void setNonMatchingList(List<String> nonMatchingList) {
            this.nonMatchingList = nonMatchingList;
        }
    }

    public static RecipeMatching matchingInfo(String message) {
        final com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = new com.example.dldke.foodbox.DataBaseFiles.InfoDO();
        final List<String> inputNames = new ArrayList<String>();
        String[] arr = message.split("\n");
        for(String temp : arr){
            inputNames.add(temp.replaceAll("\\p{Z}", ""));
        }
        for(String temp : inputNames){
            Log.d("matchingInfo",temp);
        }
        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            RecipeMatching recipeMatching = new RecipeMatching();
            @Override
            public void run() {

                for (String temp : inputNames){
                    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                    Condition condition = new Condition().withComparisonOperator(ComparisonOperator.CONTAINS).withAttributeValueList(new AttributeValue().withS(temp));
                    scanExpression.addFilterCondition("productName", condition);
                    List<InfoDO> tmpItemList = Mapper.getDynamoDBMapper().scan(InfoDO.class, scanExpression);
                    try{
                        List<InfoDO> tmpMatchingList = recipeMatching.getMatchingList();
                        tmpMatchingList.add(tmpItemList.get(0));
                        recipeMatching.setMatchingList(tmpMatchingList);
                    }
                    catch (Exception e){
                        List<String> tmpNonMatchingList = recipeMatching.getNonMatchingList();
                        tmpNonMatchingList.add(temp);
                        recipeMatching.setNonMatchingList(tmpNonMatchingList);
                    }
                }
            }
            @Override
            public Object getResult(){
                return recipeMatching;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        RecipeMatching itemList = (RecipeMatching) thread.getResult();

        for(InfoDO temp : itemList.getMatchingList()){
            Log.d("matchingInfo",temp.getName());
        }
        for(String temp : itemList.getNonMatchingList()){
            Log.d("matchingInfo",temp);
        }
        return itemList;
    }

    public static InfoDO updateMatching(final String productName, final String foodName){
        final com.example.dldke.foodbox.DataBaseFiles.InfoDO foodItem = new com.example.dldke.foodbox.DataBaseFiles.InfoDO();

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new com.example.dldke.foodbox.DataBaseFiles.returnThread(new com.example.dldke.foodbox.DataBaseFiles.CustomRunnable() {
            List<InfoDO> itemList;
            InfoDO updateItem;
            @Override
            public void run() {
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                Condition condition = new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(foodName));
                scanExpression.addFilterCondition("name", condition);
                itemList = Mapper.getDynamoDBMapper().scan(InfoDO.class, scanExpression);
                updateItem = itemList.get(0);
                try{
                    List<String> tmpList = updateItem.getProductName();
                    tmpList.add(productName);
                    updateItem.setProductName(tmpList);
                }
                catch(Exception e){
                    List<String> tmpList = new ArrayList<>();
                    tmpList.add(productName);
                    updateItem.setProductName(tmpList);
                }

                Mapper.getDynamoDBMapper().save(updateItem);

            }
            @Override
            public Object getResult(){
                return updateItem;
            }
        });

        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        InfoDO item = (InfoDO)thread.getResult();
        return item;
    }




    public static boolean expendPoint(Integer point){
        final Integer minus = point;

        com.example.dldke.foodbox.DataBaseFiles.returnThread thread = new returnThread(new CustomRunnable() {

            boolean result;
            @Override
            public void run() {
                final com.example.dldke.foodbox.DataBaseFiles.UserDO userInfo = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);

                Integer temp = userInfo.getPoint();
                if(temp >= minus) {
                    userInfo.setPoint(temp - minus);
                    result = true;
                }
                else{
                    result = false;
                }

                Mapper.getDynamoDBMapper().save(userInfo);
            }

            @Override
            public Object getResult(){
                return result;
            }
        });
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }

        boolean result = (boolean)thread.getResult();

        return result;
    }

    //Register User Profile Image in S3
    public static void uploadUserImage(final String filePath){

        Thread thread = new Thread(new Runnable() {

            com.example.dldke.foodbox.DataBaseFiles.UserDO user;
            @Override
            public void run() {
                user = Mapper.getDynamoDBMapper().load(
                        com.example.dldke.foodbox.DataBaseFiles.UserDO.class,
                        userId);
                Log.d("why",Mapper.bucketName);
                user.setProfileImage(Mapper.getDynamoDBMapper().createS3Link(Region.AP_Seoul,Mapper.bucketName,"Users/"+userId+".jpg"));
                user.getProfileImage().uploadFrom(new File(filePath));
                user.getProfileImage().setAcl(CannedAccessControlList.PublicRead);
                Mapper.getDynamoDBMapper().save(user);

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