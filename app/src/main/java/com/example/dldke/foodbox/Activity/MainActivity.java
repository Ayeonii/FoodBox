package com.example.dldke.foodbox.Activity;

import android.content.Intent;
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

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import com.example.dldke.foodbox.R;

public class MainActivity extends AppCompatActivity {
    Button join_btn, login_btn, login_ok_btn;
    EditText id_edittext, pw_edittext;
    RelativeLayout login_box, login_back;
    boolean inputID=false, inputPW=false;

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

        join_btn = (Button) findViewById(R.id.btn_join);
        join_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent JoinActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.JoinActivity.class);
                startActivity(JoinActivity);

                //JoinActivity 구현 완성시 없앰
                Toast toast = Toast.makeText(getApplicationContext(), "join 눌림", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        login_btn = (Button)findViewById(R.id.btn_login);
        login_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                login_box = (RelativeLayout) findViewById(R.id.login_box);
                login_box.setVisibility(login_box.VISIBLE);
                login_ok_btn = (Button)findViewById(R.id.ok_btn);
                login_ok_btn.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){

                        Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(LoginActivity);
                        //Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                        //startActivity(RefrigeratorMainActivity);
                    }
                });

                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(id_edittext.isFocused()){
                            inputID = true;
                        }

                        if(pw_edittext.isFocused()){
                            inputPW = true;
                        }

                        if(inputID && inputPW){
                            login_back.setVisibility(login_back.VISIBLE);
                            login_box.setVisibility(login_box.INVISIBLE);
                        }
                    }
                };
            }
        });

    }
}

