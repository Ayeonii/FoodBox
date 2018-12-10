package com.example.dldke.foodbox.MyRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeActivity;
import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeCompleteActivity;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDialogListener;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDueDateDialog;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeDueDateItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecipeItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.dldke.foodbox.Activity.MainActivity.getPinpointManager;

public class RecipeBoxHalfRecipeDetailActivity extends AppCompatActivity {
    public RecipeBoxHalfRecipeDetailActivity(){}

    public String getRecipeId(){
        return recipe_id;
    }

    private String TAG = "RecipeBoxHalfRecipeDetailActivity";
    private static int ing;

    private HalfRecipeBoxFragment halfRecipeBoxFragment = new HalfRecipeBoxFragment();
    private MyRecipeBoxHalfRecipeAdapter myRecipeBoxHalfRecipeAdapter = new MyRecipeBoxHalfRecipeAdapter();
    private String recipe_id = myRecipeBoxHalfRecipeAdapter.getRecipeId();
    private RecipeBoxHalfRecipeDetailAdapter recipeBoxHalfRecipeDetailAdapter;
    private List<RecipeDO.Ingredient> recipeItems = new ArrayList<>();
    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<DCItem> dcArray = new ArrayList<>();
    private HalfRecipeDueDateDialog dueDateDialog;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_box_halfrecipe_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.recipe_box_halfrecipe_detail_toolbar);
        TextView recipe_title = (TextView)findViewById(R.id.recipe_title);
        RecyclerView recipe_detail_view = (RecyclerView)findViewById(R.id.ingredient_detail_view);
        Button ingredient_use = (Button)findViewById(R.id.ingredient_use);
        Button fullrecipe_make = (Button)findViewById(R.id.fullrecipe_make);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipeItems = Mapper.searchRecipe(recipe_id).getIngredient();
        String simplename = Mapper.searchRecipe(recipe_id).getSimpleName();
        ing = Mapper.searchRecipe(recipe_id).getIng();

        // Toolbar Title Initialize
        recipe_title.setText(simplename);
        recipe_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        recipe_detail_view.setLayoutManager(new GridLayoutManager(this, 2));
        recipeBoxHalfRecipeDetailAdapter = new RecipeBoxHalfRecipeDetailAdapter(getApplicationContext(), recipeItems);
        recipe_detail_view.setAdapter(recipeBoxHalfRecipeDetailAdapter);

        int cnt = recipeBoxHalfRecipeDetailAdapter.getCnt();

        if (ing==0 && cnt==0) {
            ingredient_use.setVisibility(View.VISIBLE);
            fullrecipe_make.setVisibility(View.GONE);
        } else if (ing==1 || cnt!=0) {
            ingredient_use.setVisibility(View.VISIBLE);
            ingredient_use.setEnabled(false);   // '작성중'인 간이레시피는 재고소진을 할 수 없도록 함
            ingredient_use.setAlpha(.5f);       // 버튼 회색처리하는거
            fullrecipe_make.setVisibility(View.GONE);
        } else if (ing==2) {
            ingredient_use.setVisibility(View.GONE);
            fullrecipe_make.setVisibility(View.VISIBLE);
        }

        // 재료가 소진되는 플로우
        ingredient_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refrigeratorItem = Mapper.scanRefri();
                ArrayList<String> dueDateCheckArray = new ArrayList<>();

                for (int i = 0; i < recipeItems.size(); i++) {
                    dcArray.clear();
                    for (int j = 0; j < refrigeratorItem.size(); j++) {
                        if (refrigeratorItem.get(j).getName().equals(recipeItems.get(i).getIngredientName())) {
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

                        if (recipeItems.get(i).getIngredientCount() < countSum) {   //유통기한이 여러개인 재료인데 보유개수보다 적게 사용했을때 따로 배열에 모아둘거임 나중에 다이얼로그 띄워서 라디오 체크하게하려고
                            dueDateCheckArray.add(recipeItems.get(i).getIngredientName());
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
                    completeRecipe(recipeItems, dueDateCheckArray);
                }
            }
        });

        fullrecipe_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
                fullRecipeActivity.setIsHalfRecipe(true);
                Intent FullRecipeActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.FullRecipe.FullRecipeActivity.class);
                startActivity(FullRecipeActivity);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.e(TAG, "뒤로가기 눌림");
            Intent MyRecipeBoxActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
            MyRecipeBoxActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            halfRecipeBoxFragment.setisDetailBack(true);
            startActivity(MyRecipeBoxActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                for (int i = 0; i < recipeItems.size(); i++) {
                    for (int j = 0; j < radioCheckItems.size(); j++) {
                        if (recipeItems.get(i).getIngredientName().equals(radioCheckItems.get(j).getName())) {
                            radioCheckItems.get(j).setEditCount(recipeItems.get(i).getIngredientCount());
                        }
                    }
                }

                // 유통기한이 여러개인 재료의 개수를 소진하는 함수
                boolean result = minusCountByDueDate(radioCheckItems); //그냥 함수가 끝나면 true반환
                if (result)
                    completeRecipe(recipeItems, dueDateCheckArray);

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

        Mapper.updateIngInfo(2, recipe_id); // 레시피 사용완료
        PinpointManager tmp =getPinpointManager(getApplicationContext());
        Mapper.updateRecipePushEndPoint(tmp.getTargetingClient());

        Intent halfRecipeCompleteActivity = new Intent(getApplicationContext(), HalfRecipeCompleteActivity.class);
        halfRecipeCompleteActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        halfRecipeCompleteActivity.putExtra("complete", 2);
        startActivity(halfRecipeCompleteActivity);
    }
}


class AscendingSort implements Comparator<DCItem> {

    @Override
    public int compare(DCItem t1, DCItem t2) {
        return t1.getDueDate().compareTo(t2.getDueDate());
    }
}

class DescendingSort implements Comparator<DCItem> {

    @Override
    public int compare(DCItem t1, DCItem t2) {
        return t2.getDueDate().compareTo(t1.getDueDate());
    }
}