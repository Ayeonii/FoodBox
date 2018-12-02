package com.example.dldke.foodbox.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    String user_nickname, business_number;
    boolean isCook = false;

    LinearLayout business_license_number;
    EditText nickname, business_N1, business_N2, business_N3;
    String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        CircleImageView profile = (CircleImageView) findViewById(R.id.user_profile);
        TextView user_id = (TextView) findViewById(R.id.user_name);
        nickname = (EditText) findViewById(R.id.nickname);
        Switch cooking_class = (Switch) findViewById(R.id.cooking_class_btn);
        Button setting_ok = (Button) findViewById(R.id.setting_ok_btn);
        business_license_number = (LinearLayout) findViewById(R.id.business_linear);
        business_N1 = (EditText) findViewById(R.id.business_number1);
        business_N2 = (EditText) findViewById(R.id.business_number2);
        business_N3 = (EditText) findViewById(R.id.business_number3);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성

        //추후에 다른곳으로 이동
        Mapper.createUserInfo();

        user_id.setText(Mapper.getUserId());
        isCook = Mapper.searchUserInfo().getIsCookingClass();

        try{
            String nicknameStr = Mapper.searchUserInfo().getNickname();
            Log.e(TAG, "등록되어있는 닉네임 : "+nicknameStr);
            nickname.setText(nicknameStr);
        }
        catch (NullPointerException e){
            nickname.setText("닉네임을 입력해주세요");
        }

        if(isCook){
            cooking_class.setChecked(true);
            business_license_number.setVisibility(View.VISIBLE);
            String temp = Mapper.searchUserInfo().getRegisterNumber();
            business_N1.setText(temp.substring(0,3));
            business_N2.setText(temp.substring(3, 5));
            business_N3.setText(temp.substring(5, 9));
        }
        else{
            cooking_class.setChecked(false);
        }

        setting_ok.setOnClickListener(this);
        cooking_class.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.setting_ok_btn:
                user_nickname = nickname.getText().toString();
                business_number = business_N1.getText().toString() + business_N2.getText().toString() + business_N3.getText().toString();
                Log.e(TAG, "닉네임 : "+user_nickname+"사업자 번호 : "+business_number);
                Mapper.updateUserInfo(user_nickname, isCook, business_number);

                Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                startActivity(RefrigeratorMainActivity);
                break;
            case R.id.cooking_class_btn:
                business_license_number.setVisibility(View.VISIBLE);
                isCook = true;
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
