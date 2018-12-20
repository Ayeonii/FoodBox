package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeIngredientAdapter;
import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeCompleteActivity;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDialogListener;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDueDateDialog;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDueDateItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecipeBoxFullRecipeDetailActivity extends AppCompatActivity {

    private MyRecipeBoxFullRecipeAdapter myRecipeBoxFullRecipeAdapter = new MyRecipeBoxFullRecipeAdapter();
    private MyRecipeBoxHalfRecipeAdapter myRecipeBoxHalfRecipeAdapter = new MyRecipeBoxHalfRecipeAdapter();
    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private String recipe_id;
    private boolean isCookingClass = refrigeratorMainActivity.getisCookingClass();
    private RecipeDO.Detail detail;
    private RecyclerView detail_recyclerview, detail_ingredient_recyclerview;
    private RecipeBoxFullRecipeDetailAdapter recipeDetailAdapter;
    private FullRecipeIngredientAdapter recipeIngredientAdapter;

    private List<RecipeDO.Ingredient> recipeData;
    private List<RefrigeratorDO.Item> refrigeratorItem;
    private Double refriCount[];
    List<HalfRecipeRecipeItem> recipeItems = new ArrayList<>();
    private ArrayList<DCItem> dcArray = new ArrayList<>();
    private HalfRecipeDueDateDialog dueDateDialog;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_fullrecipe_detail);

        boolean isPost = myRecipeBoxHalfRecipeAdapter.IsPost();

        if(isPost){
            recipe_id = myRecipeBoxHalfRecipeAdapter.getRecipeId();
        } else{
            recipe_id = myRecipeBoxFullRecipeAdapter.getRecipeId();
        }

        String imgUrl = Mapper.getImageUrlRecipe(recipe_id);
        boolean isShared = Mapper.searchRecipe(recipe_id).getIsShare();

        Toolbar toolbar = (Toolbar)findViewById(R.id.recipe_box_fullrecipe_detail_toolbar);
        ImageView mainImg = (ImageView)findViewById(R.id.fullrecipe_detail_foodimg);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.fullrecipe_detail_collasping_toolbar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        Button post_fullrecipe_write = (Button) findViewById(R.id.fullrecipe_write);

        new DownloadImageTask(mainImg).execute(imgUrl);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detail = Mapper.searchRecipe(recipe_id).getDetail();
        String foodName = detail.getFoodName();
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        detail_ingredient_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_ingredient_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detail_ingredient_recyclerview.setLayoutManager(mLayoutManager);
        recipeData = Mapper.searchRecipe(recipe_id).getIngredient();
        recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, recipeData);
        detail_ingredient_recyclerview.setAdapter(recipeIngredientAdapter);

        detail_recyclerview = (RecyclerView)findViewById(R.id.fullrecipe_detail_view);
        detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recipeDetailAdapter = new RecipeBoxFullRecipeDetailAdapter(recipe_id);
        detail_recyclerview.setAdapter(recipeDetailAdapter);

        if(isShared){
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }

        if(isPost){
            post_fullrecipe_write.setVisibility(View.VISIBLE);
        } else{
            post_fullrecipe_write.setVisibility(View.INVISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        post_fullrecipe_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIngredient(recipeData);
                scanRefrigeratorAndRecipe();
            }
        });

    }

    public void AddIngredient(List<RecipeDO.Ingredient> ingredientList) {

        for (int i = 0; i < ingredientList.size(); i++) {
            String name = ingredientList.get(i).getIngredientName();
            Double count = ingredientList.get(i).getIngredientCount();

            String foodImg = "file://"+getApplicationContext().getFilesDir()+name+".jpg";
            recipeItems.add(new HalfRecipeRecipeItem(name, count, Uri.parse(foodImg)));
        }
    }

    public void scanRefrigeratorAndRecipe() {

        refrigeratorItem = Mapper.scanRefri();
        refriCount = new Double[recipeItems.size()];

        for (int i = 0; i < recipeItems.size(); i++) {
            refriCount[i] = 0.0;
            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(recipeItems.get(i).getName())) {
                    // 냉장고에 재료가 있으면 -> 가진 개수를 넣는다.(없으면 0.0으로 미리 되어있음)
                    refriCount[i] = refrigeratorItem.get(j).getCount();
                }
            }
        }

        int cnt = 0;
        for (int i = 0; i < refriCount.length; i++) {
            if (refriCount[i] == 0.0 || refriCount[i] < recipeItems.get(i).getCount()) {
                cnt++;
            }
        }

        if (cnt!=0)
            Toast.makeText(getApplicationContext(), "냉장고에 몇몇 재료가 없습니다!", Toast.LENGTH_LONG).show();
        else
            minusIngredient();

//        if (cnt != 0)
//            Mapper.updateIngInfo(1, recipe_id);
//        else
//            Mapper.updateIngInfo(0, recipe_id);

//        PinpointManager tmp = getPinpointManager(getApplicationContext());
//        Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());
    }

    private void minusIngredient() {
        //refrigeratorItem = Mapper.scanRefri();
        ArrayList<String> dueDateCheckArray = new ArrayList<>();

        for (int i = 0; i < recipeData.size(); i++) {
            dcArray.clear();
            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(recipeData.get(i).getIngredientName())) {
                    // 냉장고에 재료가 있으면
                    // 해당 재료의 유통기한과 개수를 받아온다.
                    dcArray.add(new DCItem(refrigeratorItem.get(j).getDueDate(), refrigeratorItem.get(j).getCount()));
                }
            }

            for (int l=0; l<dcArray.size(); l++) {
                Log.d("test", l + ", "+dcArray.get(l).getStrDueDate());
            }

            if (dcArray.size() > 1) {
                Double countSum = 0.0;  // 총 보유개수
                for (int k = 0; k < dcArray.size(); k++) {
                    countSum += dcArray.get(k).getCount();
                }

                if (recipeData.get(i).getIngredientCount() < countSum) {   //유통기한이 여러개인 재료인데 보유개수보다 적게 사용했을때 따로 배열에 모아둘거임 나중에 다이얼로그 띄워서 라디오 체크하게하려고
                    dueDateCheckArray.add(recipeData.get(i).getIngredientName());
                }
            }
        }

        for (int i=0; i<dueDateCheckArray.size(); i++) {
            Log.d("test", i + ", "+dueDateCheckArray.get(i));
        }

        if (dueDateCheckArray.size() > 0) {
            // 유통기한 체크하는 다이얼로그 띄우기
            showDueDateDialog(dueDateCheckArray);
        } else {
            // 바로 재고소진하는 로직으로 들어가기
            completeRecipe(recipeData, dueDateCheckArray);
        }
    }

    public void showDueDateDialog(final ArrayList<String> dueDateCheckArray) {
        dueDateDialog = new HalfRecipeDueDateDialog(this, dueDateCheckArray);
        dueDateDialog.setDialogListener(new HalfRecipeDialogListener() {
            @Override
            public void onPositiveClicked(String type, Boolean[] check) { }

            @Override
            public void onCompleteClicked(int result, String recipeName, ArrayList<HalfRecipeRecipeItem> mItems, ArrayList<String> dueDateCheckArray) { }

            @Override
            public void onDueDateOKClicked(ArrayList<HalfRecipeDueDateItem> radioCheckItems) {
                for (int i = 0; i < recipeData.size(); i++) {
                    for (int j = 0; j < radioCheckItems.size(); j++) {
                        if (recipeData.get(i).getIngredientName().equals(radioCheckItems.get(j).getName())) {
                            radioCheckItems.get(j).setEditCount(recipeData.get(i).getIngredientCount());
                        }
                    }
                }

                // 유통기한이 여러개인 재료의 개수를 소진하는 함수
                boolean result = minusCountByDueDate(radioCheckItems); //그냥 함수가 끝나면 true반환
                if (result)
                    completeRecipe(recipeData, dueDateCheckArray);

            }

            @Override
            public void onIngOkClicked(int ok) {

            }
        });
        dueDateDialog.setCancelable(false);
        dueDateDialog.show();
    }

    private boolean minusCountByDueDate(ArrayList<HalfRecipeDueDateItem> radioCheckItems) {

        for (int i = 0; i < radioCheckItems.size(); i++) {

            // 1. 해당 이름의 유통기한과 보유개수 가져오기 배열로
            // 정수형태의 유통기한과 더블형의 보유개수를 저장할 배열 생성
            ArrayList<DCItem> dcArray = new ArrayList<>();

            Log.e("test", "정수형태의 유통기한과 더블형의 보유개수를 저장할 배열");
            for (int j = 0; j < refrigeratorItem.size(); j++) {
                if (refrigeratorItem.get(j).getName().equals(radioCheckItems.get(i).getName())) {
                    Integer iDueDate = Integer.parseInt(refrigeratorItem.get(j).getDueDate());
                    dcArray.add(new DCItem(iDueDate, refrigeratorItem.get(j).getCount()));
                }
            }

            for (int j=0; j<dcArray.size(); j++)
                Log.d("test", dcArray.get(j).getDueDate() + ", " + dcArray.get(j).getCount());

            Log.d("test", "radioCheckItems.get(i).getWhich() = " + radioCheckItems.get(i).getWhich());
            // 2. 유통기한 기준 정렬 which = 0 이면 오름차순 1 이면 내림차순
            switch (radioCheckItems.get(i).getWhich()) {
                case 0: //오름차순
                    Collections.sort(dcArray, new AscendingSort());
                    break;
                case 1: //내림차순
                    Collections.sort(dcArray, new DescendingSort());
                    break;
            }

            Log.e("test", "정렬된 후의 dcArray");
            for (int a=0; a<dcArray.size(); a++) {
                Log.d("test", a+ " : "+dcArray.get(a).getDueDate() + ", " + dcArray.get(a).getCount());
            }

            // 계산하고 바로 updatecount
            // 예를들어 감자 2.0개 + 3.0개, 근데 사용할 개수는 1.0개
            Double editCount = radioCheckItems.get(i).getEditCount();   //사용할 개수
            for (int k = 0; k < dcArray.size(); k++) {      //dcArray.size=2
                if (editCount <= dcArray.get(k).getCount()) {    // 예를들어 감자 3.0개 + 2.0개, 근데 사용할 개수는 1.0개
                    Mapper.minusCountwithDueDate(radioCheckItems.get(i).getName(), Integer.toString(dcArray.get(k).getDueDate()), editCount);
                    break;  // 하고 바로 반복문을 나온다..?
                } else {        // 예를들어 감자 3.0개 + 2.0개, 근데 사용할 개수는 4.0개
                    editCount -= dcArray.get(k).getCount();
                    Mapper.minusCountwithDueDate(radioCheckItems.get(i).getName(), Integer.toString(dcArray.get(k).getDueDate()), dcArray.get(k).getCount());
                }
            }

        }

        return true;
    }

    private void completeRecipe(List<RecipeDO.Ingredient> recipeItems, ArrayList<String> dueDateCheckArray) {

        if (dueDateCheckArray.size() == 0) {
            for (int i = 0; i < recipeItems.size(); i++) {
                Mapper.minusCount(recipeItems.get(i).getIngredientName(), recipeItems.get(i).getIngredientCount());
            }
        } else {
            // 중복 배열에 있는 거 제외하고 나머지에 대해 정상적으로 minus
            for (int i = 0; i < recipeItems.size(); i++) {
                int has = 0;
                for (int j = 0; j < dueDateCheckArray.size(); j++) {
                    if (recipeItems.get(i).getIngredientName().equals(dueDateCheckArray.get(j)))
                        has = 1;
                }

                if (has == 0) {
                    Mapper.minusCount(recipeItems.get(i).getIngredientName(), recipeItems.get(i).getIngredientCount());
                }
            }
        }

//        Mapper.updateIngInfo(2, recipe_id); // 레시피 사용완료
//        PinpointManager tmp =getPinpointManager(getApplicationContext());
//        Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());

        // 가져온 레시피를 재료 소진해서 사용했으면 MyCommunity에서 지우기

        Intent halfRecipeCompleteActivity = new Intent(getApplicationContext(), HalfRecipeCompleteActivity.class);
        halfRecipeCompleteActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        halfRecipeCompleteActivity.putExtra("complete", 2);
        startActivity(halfRecipeCompleteActivity);
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlImg =urls[0];
            Bitmap foodImg = null;
            try {
                InputStream in = new java.net.URL(urlImg).openStream();
                foodImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodImg;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    void showDialog(){
        CustomDialog customDialog = new CustomDialog(this, isCookingClass);
        customDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window window = customDialog.getWindow();
        window.setAttributes(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent MyRecipeBoxActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
            MyRecipeBoxActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(MyRecipeBoxActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
