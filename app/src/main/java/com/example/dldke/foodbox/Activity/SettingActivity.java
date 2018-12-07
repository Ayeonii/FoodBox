package com.example.dldke.foodbox.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static String user_nickname, business_number;
    boolean isCook = false;

    LinearLayout business_license_number, point_layout;
    TextView business_N1, business_N2, business_N3;
    TextView nickname;
    String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        CircleImageView profile = (CircleImageView) findViewById(R.id.user_profile);
        TextView user_id = (TextView) findViewById(R.id.user_name);
        TextView point = (TextView) findViewById(R.id.point);
        Switch cooking_class = (Switch) findViewById(R.id.cooking_class_btn);
        Button setting_ok = (Button) findViewById(R.id.setting_ok_btn);
        point_layout = (LinearLayout) findViewById(R.id.point_linear);
        business_license_number = (LinearLayout) findViewById(R.id.business_linear);
        business_N1 = (TextView) findViewById(R.id.business_number1);
        business_N2 = (TextView) findViewById(R.id.business_number2);
        business_N3 = (TextView) findViewById(R.id.business_number3);
        nickname = (TextView) findViewById(R.id.nickname);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성


        user_id.setText(Mapper.getUserId());
        point.setText(String.valueOf(Mapper.searchUserInfo().getPoint()));
        isCook = Mapper.searchUserInfo().getIsCookingClass();

        String nicknameStr = Mapper.searchUserInfo().getNickname();
        if(nicknameStr != null){
            nickname.setText(nicknameStr);
        }

//        try{
//            String nicknameStr = Mapper.searchUserInfo().getNickname();
//            Log.e(TAG, "등록되어있는 닉네임 : "+nicknameStr);
//            nickname.setText(nicknameStr);
//        }
//        catch (NullPointerException e){
//        }

        if(isCook){
            cooking_class.setChecked(true);
            business_license_number.setVisibility(View.VISIBLE);
            try{
                String temp = Mapper.searchUserInfo().getRegisterNumber();
                business_N1.setText(temp.substring(0,3));
                business_N2.setText(temp.substring(3, 5));
                business_N3.setText(temp.substring(5, 8));
            }
            catch (NullPointerException e){ }

        }
        else{
            cooking_class.setChecked(false);
        }

        setting_ok.setOnClickListener(this);
        cooking_class.setOnClickListener(this);
        nickname.setOnClickListener(this);
        business_license_number.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.setting_ok_btn:
                //user_nickname = nickname.getText().toString();
                business_number = business_N1.getText().toString() + business_N2.getText().toString() + business_N3.getText().toString();
                Log.e(TAG, "닉네임 : "+user_nickname+"사업자 번호 : "+business_number);
                Mapper.updateUserInfo(user_nickname, isCook, business_number);

                Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                startActivity(RefrigeratorMainActivity);
                break;
            case R.id.cooking_class_btn:
                business_license_number.setVisibility(View.VISIBLE);
                isCook = true;

            case R.id.nickname:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText et = new EditText(SettingActivity.this);
                builder.setTitle("닉네임 추가");
                builder.setMessage("닉네임을 입력해 주세요");
                builder.setView(et);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        user_nickname = et.getText().toString();
                        nickname.setText(user_nickname);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

            case R.id.business_linear:
                AlertDialog.Builder business_builder = new AlertDialog.Builder(this);
                final EditText business_et = new EditText(SettingActivity.this);
                business_et.setText("000-00-000");
                business_builder.setTitle("사업자 번호 추가");
                business_builder.setMessage("사업자 번호를 입력해주세요");
                business_builder.setView(business_et);
                business_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                       String temp = business_et.getText().toString();
                       business_N1.setText(temp.substring(0,3));
                       business_N2.setText(temp.substring(4,6));
                       business_N3.setText(temp.substring(7,10));
                       dialog.dismiss();
                    }
                });
                business_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                business_builder.create().show();
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
