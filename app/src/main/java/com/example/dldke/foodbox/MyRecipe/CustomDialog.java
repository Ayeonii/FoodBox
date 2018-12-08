package com.example.dldke.foodbox.MyRecipe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView ok, cancel;
    private EditText title_edittext;

    private MyRecipeBoxFullRecipeAdapter myRecipeBoxFullRecipeAdapter = new MyRecipeBoxFullRecipeAdapter();
    private String recipe_id;
    private boolean isCookignClass;


    public CustomDialog(Context context, boolean isCookingClass){
        super(context);
        this.context = context;
        this.isCookignClass = isCookingClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_box_fullrecipe_detail_popup1);

        recipe_id = myRecipeBoxFullRecipeAdapter.getRecipeId();

        ok = (TextView) findViewById(R.id.dialog_ok);
        cancel = (TextView) findViewById(R.id.dialog_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.dialog_ok:
                String title = title_edittext.getText().toString();
                Mapper.updateIsShared(recipe_id);
                Mapper.createPost(" "+title, recipe_id);
                Mapper.updatePointInfo(10);
                break;
            case R.id.dialog_cancel:
                cancel();
                break;
        }
    }
}
