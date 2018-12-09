package com.example.dldke.foodbox.Activity;

import android.content.Intent;
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
import com.example.dldke.foodbox.R;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button join_btn, login_btn, login_ok_btn;
    EditText id_edittext, pw_edittext;
    RelativeLayout login_box, login_back;
    boolean inputID=false, inputPW=false;
    public static Editable id,pw;

    int MY_PERMISSIONS_REQUEST_CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
                @Override
                public void onComplete(AWSStartupResult awsStartupResult) {
                    Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
                }
            }).execute();


        login_btn = (Button)findViewById(R.id.btn_login);
        login_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(LoginActivity);
            }
        });


//        PermissionCheck();
    }

//    void PermissionCheck() {
//        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//
//        if (permissionCheck!=PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
//                // dialog
//                Log.e("test", "다이얼로그 띄워서 권한 요청하는 부분");
//            } else {
//                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
//
//                Log.e("test", "권한허가요청을 받아서 결과를 받는 부분");
//            }
//
//        } else {
//
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA:
//                return;
//        }
//    }
}

