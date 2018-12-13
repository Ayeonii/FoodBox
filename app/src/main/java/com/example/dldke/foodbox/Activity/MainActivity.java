package com.example.dldke.foodbox.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.UserStateListener;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PushListenerService;
import com.example.dldke.foodbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import android.support.v4.content.ContextCompat;



public class MainActivity extends AppCompatActivity {

    Button login_btn;
    ConstraintLayout main_backgroudn;

    public static final String TAG = MainActivity.class.getSimpleName();

    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {

        if (pinpointManager == null) {
            AWSMobileClient.getInstance().initialize(applicationContext, new AWSConfiguration(applicationContext),new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    //Mapper.setUserId(applicationContext);
                    //Mapper.setBucketName(applicationContext);
                    //Mapper.setDynamoDBMapper(AWSMobileClient.getInstance());
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    new AWSConfiguration(applicationContext));
                    //AWSMobileClient.getInstance().getConfiguration());

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            final String token = task.getResult().getToken();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //settingTheme();
        setContentView(R.layout.activity_main);

        //로그인 전에는 사용자 정보 받아올 수 없음
//        main_backgroudn = (ConstraintLayout) findViewById(R.id.refrigerator_background);
//        String theme = Mapper.searchUserInfo().getTheme();
//        if(theme == "블랙"){
//            main_backgroudn.setBackground(getApplicationContext().getDrawable(R.drawable.fridgerator_background_black));
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        //읽기 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        } //쓰기 권한 요청


        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        switch (userStateDetails.getUserState()){
                            case SIGNED_IN:
                                Log.e("자동로그인","자동로그인");
                                Mapper.setUserId(getApplicationContext());
                                Mapper.setBucketName(getApplicationContext());
                                Mapper.setDynamoDBMapper(AWSMobileClient.getInstance());

                                String locate = getIntent().getStringExtra("locate");
                                Intent RefriActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                                RefriActivity.putExtra("locate",locate);
                                startActivity(RefriActivity);
                                break;
                            case SIGNED_OUT_FEDERATED_TOKENS_INVALID:
                            case SIGNED_OUT_USER_POOLS_TOKENS_INVALID:
                                Log.i("userState", "need to login again.");
                                Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(LoginActivity);
                                //Alternatively call .showSignIn()
                                break;
                        }
                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

        login_btn = (Button)findViewById(R.id.btn_login);

        login_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(LoginActivity);
            }
        });
    }

//    public void settingTheme(){
//        String theme = "블랙";
//        if(theme == "기본 테마"){
//            login_btn.setBackground(getApplicationContext().getDrawable(R.color.colorPrimary));
//        }
//        if(theme == "블랙"){
//            login_btn.setBackground(getApplicationContext().getDrawable(R.color.blackPrimary));
//        }
//        if(theme == "베이지"){
//            login_btn.setBackground(getApplicationContext().getDrawable(R.color.beigePrimary));
//        }
//    }

}

