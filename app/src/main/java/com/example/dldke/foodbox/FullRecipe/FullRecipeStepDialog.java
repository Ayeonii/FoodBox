package com.example.dldke.foodbox.FullRecipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeStepDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String recipeId;
    public static String step_description="";
    private RecyclerView ingredient_view;
    private Button button_submit, button_cancel;
    private static Spinner method, minute, fire;

    private FullRecipeStepAdapter adapter;
    private List<String> items = new ArrayList<>();
    private List<RecipeDO.Ingredient> ingredients = new ArrayList<>();
    private List<RecipeDO.Ingredient> specIngredient = new ArrayList<>();
    private static List<RecipeDO.Spec> specList = new ArrayList<>();
    private FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
    private boolean isHalfRecipe = fullRecipeActivity.getIsHalfRecipe();

    private String TAG="FullRecipeStepDialog";

    public List<RecipeDO.Spec> getSpecList(){
        return specList;
    }
    public List<RecipeDO.Ingredient> getIngredients(){
        return ingredients;
    }

    public FullRecipeStepDialog(@NonNull Context context, String recipeId){
        super(context);
        this.context = context;
        this.recipeId = recipeId;
    }

    public FullRecipeStepDialog(@NonNull Context context, List<RecipeDO.Ingredient> items){
        super(context);
        this.context = context;
        this.ingredients = items;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullrecipe_step_dialog);

        ingredient_view = (RecyclerView) findViewById(R.id.check_recycler_view);
        button_submit = (Button) findViewById(R.id.done_btn);
        button_cancel = (Button) findViewById(R.id.cancel_btn);
        method = (Spinner) findViewById(R.id.method_spinner);
        minute = (Spinner) findViewById(R.id.minute_spinner);
        fire = (Spinner) findViewById(R.id.fire_spinner);

        button_submit.setText("삽입");


        if(isHalfRecipe){
            ingredients = Mapper.searchRecipe(recipeId).getIngredient();
        }
        ingredient_view.setHasFixedSize(true);
        ingredient_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FullRecipeStepAdapter(ingredients);
        ingredient_view.setAdapter(adapter);


        //방법, 시간, 불세기 spinner
        String[] methodStr = context.getResources().getStringArray(R.array.MethodSpinner);
        ArrayAdapter<String> madapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, methodStr);
        method.setAdapter(madapter);

        String[] minuteStr = context.getResources().getStringArray(R.array.MinuteSpinner);
        ArrayAdapter<String> minadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, minuteStr);
        minute.setAdapter(minadapter);

        String[] fireStr = context.getResources().getStringArray(R.array.FireSpinner);
        ArrayAdapter<String> fireadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, fireStr);
        fire.setAdapter(fireadapter);


        button_submit.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.done_btn:
                insertData();
                dismiss();
                break;
            case R.id.cancel_btn:
                dismiss();
                break;

                default:
                    break;
        }
    }

    public void insertData(){

        String method_st = method.getSelectedItem().toString();
        Integer minute_int = Integer.parseInt(minute.getSelectedItem().toString());
        String fire_st = fire.getSelectedItem().toString();
        items = adapter.getTempItems();

        String ingredient="";
        for(int i = 0; i<items.size(); i++){
            ingredient = ingredient+items.get(i)+",";
            Log.e(TAG, "재료"+ingredient);
            for(int j = 0; j<ingredients.size(); j++){
                if(items.get(i).equals(ingredients.get(j).getIngredientName()))
                    specIngredient.add(ingredients.get(j));
            }
        }

        if(minute_int.equals(0) || fire_st.equals("없음")){
            step_description = ingredient+"을/를 \r\n"+method_st;
        }
        else{
            step_description = ingredient+"을/를 \r\n"+minute_int+"분 동안 \r\n"+method_st+" (불 세기: "+fire_st+")";
        }

        RecipeDO.Spec spec = Mapper.createSpec(specIngredient, method_st, fire_st, minute_int);
        specList.add(spec);

        FullRecipeData data = new FullRecipeData(step_description);
        FullRecipeActivity.fullRecipeData.add(data);
        FullRecipeActivity.fullRecipeAdapter.notifyDataSetChanged();
        FullRecipeAdapter fullRecipeAdapter = new FullRecipeAdapter();
        fullRecipeAdapter.setSpecList(specList);
        items.clear();
        specIngredient.clear();
    }


}
