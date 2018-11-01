package com.example.dldke.foodbox.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.FullRecipeAdapter;
import com.example.dldke.foodbox.Adapter.FullRecipeHorizontalAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipeDictionary;
import com.example.dldke.foodbox.FullRecipeHorizontalData;
import com.example.dldke.foodbox.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.regions.RegionUtils.init;

public class FullRecipeActivity extends AppCompatActivity implements View.OnClickListener  {

    private ArrayList<FullRecipeDictionary> mArrayList;
    private FullRecipeAdapter mAdapter;
    private RecyclerView fullrecipeRecyclerView, recipeIngredientHorizontalView;
    private FullRecipeHorizontalAdapter fullRecipeHorizontalAdapter;
    private LinearLayoutManager mLayoutManager;

    private int MAX_ITEM_COUNT=10;

    private final String TAG="FullRecipe DB Test";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);

        fullrecipeRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);
        fullrecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new FullRecipeAdapter(this, mArrayList);
        fullrecipeRecyclerView.setAdapter(mAdapter);


        final EditText foodtitle_et = (EditText)findViewById(R.id.food_title);

        //DB에 풀레시피 만들기
        final List<RecipeDO.Ingredient> specIngredientList = new ArrayList<>();
        final List<RecipeDO.Spec> specList = new ArrayList<>();
        final String recipe_id;

        /*
        요리 카테고리 만드는 spinner
         */
        Spinner spinner = (Spinner)findViewById(R.id.food_spinner);
        String[] foodrecipe_list = new String[]{"메인요리","국/찌개","반찬","간식"};

        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, foodrecipe_list);
        spinner.setAdapter(spinnerAdapter);


        //=======================================================================================


        /*
        선택된 재료 보여주기
         */
        recipeIngredientHorizontalView = (RecyclerView)findViewById(R.id.recyclerview_horizontal_list);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recipeIngredientHorizontalView.setLayoutManager(mLayoutManager);

        fullRecipeHorizontalAdapter = new FullRecipeHorizontalAdapter();

        ArrayList<FullRecipeHorizontalData> data = new ArrayList<>();

        int i = 0;
        while(i<MAX_ITEM_COUNT){
            data.add(new FullRecipeHorizontalData(R.drawable.food, i+"번째 데이터"));
            i++;
        }

        fullRecipeHorizontalAdapter.setData(data);
        recipeIngredientHorizontalView.setAdapter(fullRecipeHorizontalAdapter);



        //=================================================================================================


        /*
        커뮤니티 객체 만들기
         */
        List<String> recipe_list = Mapper.searchMyCommunity().getMyRecipes();
        recipe_id = recipe_list.get(0);
        Log.d(TAG, "뭔지 모르겠는 데이터!!!!" + Mapper.searchRecipe(recipe_id).getIngredient().get(0).getIngredientName());


        /*
        풀레시피에 간이레시피에 등록된 식재료 입력
         */
        specIngredientList.add(Mapper.searchRecipe(recipe_id).getIngredient().get(0));
        specIngredientList.add(Mapper.searchRecipe(recipe_id).getIngredient().get(1));

        /*
        풀레시피 detail 작성 팝업창
         */
        Button ingredient_btn = (Button)findViewById(R.id.button_main_insert);
        ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //팝업창 build
                AlertDialog.Builder builder = new AlertDialog.Builder(FullRecipeActivity.this);
                View view = LayoutInflater.from(FullRecipeActivity.this)
                        .inflate(R.layout.fullrecipe_popup, null, false);
                builder.setView(view);
                //final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                //final EditText method = (EditText) view.findViewById(R.id.edittext_dialog_method);
                //final EditText minute = (EditText) view.findViewById(R.id.edittext_dialog_minute);
                //final EditText fire = (EditText) view.findViewById(R.id.edittext_dialog_fire);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.done_btn);
                final Spinner method_sp = (Spinner) view.findViewById(R.id.method_spinner);
                final Spinner minute_sp = (Spinner) view.findViewById(R.id.minute_spinner);
                final Spinner fire_sp = (Spinner) view.findViewById(R.id.fire_spinner);


                String[] methodStr = getResources().getStringArray(R.array.MethodSpinner);
                ArrayAdapter<String> madapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, methodStr);
                method_sp.setAdapter(madapter);

                String[] minuteStr = getResources().getStringArray(R.array.MinuteSpinner);
                ArrayAdapter<String> miadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, minuteStr);
                minute_sp.setAdapter(miadapter);

                String[] fireStr = getResources().getStringArray(R.array.FireSpinner);
                ArrayAdapter<String> fadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fireStr);
                fire_sp.setAdapter(fadapter);


                ButtonSubmit.setText("삽입");

                //레시피 작성후 화면과 데이터베이스에 삽입
                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String method = method_sp.getSelectedItem().toString();
                        String minute = minute_sp.getSelectedItem().toString();
                        Integer minuteInt = Integer.parseInt(minute);
                        String fire = fire_sp.getSelectedItem().toString();

                        FullRecipeDictionary dict = new FullRecipeDictionary(method, minute, fire);

                        //mArrayList.add(0, dict); //첫 줄에 삽입
                        mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영


                        //풀레시피에 단계별 레시피 등록
                        RecipeDO.Spec spec = Mapper.createSpec(specIngredientList, method, fire, minuteInt);
                        specList.add(spec);
                        Log.d(TAG, "방법 : "+method + "불 세기 : "+fire +"시간 : "+minuteInt);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        /*
        작성 완료시 풀레시피 데이터 DB에 저장하기
         */
        Button ok_btn = (Button)findViewById(R.id.recipe_ok);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FoodTitle = foodtitle_et.getText().toString();
                Mapper.createFullRecipe(recipe_id, FoodTitle, specList);
                Intent RefrigeratorActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                startActivity(RefrigeratorActivity);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ingredient_add:
                Intent choice_ingred = new Intent(getApplicationContext(), PencilRecipeActivity.class);
                startActivity(choice_ingred);
            case R.id.get_recipe:
        }


    }
}
