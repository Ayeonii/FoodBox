package com.example.dldke.foodbox.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        CircleImageView profile = (CircleImageView) findViewById(R.id.user_profile);
        TextView user_id = (TextView) findViewById(R.id.user_name);
        EditText nickname = (EditText) findViewById(R.id.nickname);
        Switch cooking_class = (Switch) findViewById(R.id.cooking_class_btn);
        Button setting_ok = (Button) findViewById(R.id.setting_ok_btn);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성


        user_id.setText(Mapper.getUserId());


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.nickname_linear:
                break;
                default: break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.Activity.RefrigeratorMainActivity.class);
            RefrigeratorMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(RefrigeratorMainActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
