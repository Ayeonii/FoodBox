package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout nickname = (LinearLayout) findViewById(R.id.nickname_linear);
        LinearLayout alarm = (LinearLayout) findViewById(R.id.alarm_linear);
        LinearLayout cooking_class = (LinearLayout) findViewById(R.id.cookingclass_linear);
        LinearLayout logout = (LinearLayout) findViewById(R.id.logout_linear);
        TextView user_id = (TextView) findViewById(R.id.user_name);

        nickname.setOnClickListener(this);
        alarm.setOnClickListener(this);
        cooking_class.setOnClickListener(this);
        logout.setOnClickListener(this);

        user_id.setText(Mapper.getUserId());
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.nickname_linear:
                break;
            case R.id.alarm_linear:
                break;
            case R.id.cookingclass_linear:
                break;
            case R.id.logout_linear:
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                //로그아웃 후, 뒤로가기 누르면 다시 로그인 된 상태로 가는 것을 방지
                MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(MainActivity);
                break;
                default: break;
        }
    }
}
