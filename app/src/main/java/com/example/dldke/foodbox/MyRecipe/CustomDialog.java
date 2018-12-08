package com.example.dldke.foodbox.MyRecipe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView ok, cancel;
    private EditText title, password;
    private LinearLayout password_layout;

    private MyRecipeBoxFullRecipeAdapter myRecipeBoxFullRecipeAdapter = new MyRecipeBoxFullRecipeAdapter();
    private String recipe_id;
    private boolean isCookignClass;

    String TAG="CustomDialog";

    public CustomDialog(Context context, boolean isCookingClass){
        super(context);
        this.context = context;
        this.isCookignClass = isCookingClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_box_fullrecipe_detail_popup1);

        Log.e(TAG, "쿠킹 클래스"+isCookignClass);
        recipe_id = myRecipeBoxFullRecipeAdapter.getRecipeId();

        ok = (TextView) findViewById(R.id.dialog_ok);
        cancel = (TextView) findViewById(R.id.dialog_cancel);
        title = (EditText) findViewById(R.id.dialog_edittext);
        password_layout = (LinearLayout) findViewById(R.id.dialog_password_layout);
        password = (EditText) findViewById(R.id.dialog_password);
        password_layout.setVisibility(View.GONE);

        if(isCookignClass){
            password_layout.setVisibility(View.VISIBLE);
        }

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.dialog_ok:

                String title_str = title.getText().toString();
                String password_str = password.getText().toString();

                Mapper.updateIsShared(recipe_id);
                Mapper.createPost(" "+title_str, recipe_id);
                Mapper.updatePointInfo(10);

                if(password_str != null){
                    Mapper.updatePassword(recipe_id, password_str);
                }

                cancel();
                break;
            case R.id.dialog_cancel:
                cancel();
                break;
        }
    }
}
