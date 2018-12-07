package com.example.dldke.foodbox.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.dldke.foodbox.PushListenerService;
import com.example.dldke.foodbox.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    Button join_btn, login_btn, login_ok_btn;
    EditText id_edittext, pw_edittext;
    RelativeLayout login_box, login_back;
    boolean inputID=false, inputPW=false;
    public static Editable id,pw;



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
    }

}

