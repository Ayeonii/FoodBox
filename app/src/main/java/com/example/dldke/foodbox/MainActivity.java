package com.example.dldke.foodbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;


public class MainActivity extends AppCompatActivity {
    Button join_btn, login_btn;
    EditText id_edittext, pw_edittext;

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
                    //Intent JoinActivity = new Intent(getApplicationContext(), JoinActivity.class);
                    //startActivity(JoinActivity);

                    //JoinActivity 구현 완성시 없앰
                    Toast toast = Toast.makeText(getApplicationContext(), "join 눌림", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            login_btn = (Button)findViewById(R.id.btn_login);
            login_btn.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(LoginActivity);
                }
            });
    }
}

