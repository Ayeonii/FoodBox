package com.example.dldke.foodbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobile.auth.core.IdentityManager;

public class RefrigeratorMainActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);
        Button clickButton = (Button) findViewById(R.id.signOutButton);
        clickButton.setOnClickListener( new View.OnClickListener() {

            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
            }
        });
    }
}
