package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;

import com.amazonaws.mobile.auth.google.GoogleButton;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class LoginActivity extends AppCompatActivity {
    Button ok_btn;
    EditText id_edittext, pw_edittext;
    boolean isFirst;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AWSMobileClient.getInstance().initialize(this).execute();


        // Sign-in listener
        IdentityManager.getDefaultIdentityManager().addSignInStateChangeListener(new SignInStateChangeListener() {
            @Override
            public void onUserSignedIn() {
                //Log.d(LOG_TAG, "User Signed In");
            }

            // Sign-out listener
            @Override
            public void onUserSignedOut() {

                //Log.d(LOG_TAG, "User Signed Out");
                showSignIn();
            }
        });
        ok_btn = (Button)findViewById(R.id.login_button);
        id_edittext = (EditText)findViewById(R.id.edittext_id);
        pw_edittext = (EditText)findViewById(R.id.edittext_pw);

        showSignIn();


    }
    private void showSignIn() {

        Log.d("showSignIn", "showSignIn");

        AuthUIConfiguration config =
                new AuthUIConfiguration.Builder()
                        .userPools(true)  // true? show the Email and Password UI
                        .signInButton(GoogleButton.class) // Show Google button
                        .backgroundColor(Color.WHITE) // Change the backgroundColor
                        .logoResId(R.drawable.splash_background)
                        .isBackgroundColorFullScreen(true) // Full screen backgroundColor the backgroundColor full screenff
                        .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                        .canCancel(true)
                        .build();
        SignInUI signinUI = (SignInUI) AWSMobileClient.getInstance()
                                                     .getClient(LoginActivity.this, SignInUI.class);



        signinUI.login(LoginActivity.this, RefrigeratorMainActivity.class).authUIConfiguration(config).execute();
        Mapper.setDynamoDBMapper();
        Mapper.setUserId(LoginActivity.this);
    }
}
