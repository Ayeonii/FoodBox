package com.example.dldke.foodbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

public class LoginActivity extends AppCompatActivity {
    Button ok_btn;
    EditText id_edittext, pw_edittext;

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

        showSignIn();

        ok_btn = (Button)findViewById(R.id.login_button);
        id_edittext = (EditText)findViewById(R.id.edittext_id);
        pw_edittext = (EditText)findViewById(R.id.edittext_pw);

        ok_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                startActivity(RefrigeratorMainActivity);
            }
        });


    }
    private void showSignIn() {

        //Log.d(LOG_TAG, "showSignIn");

        SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(LoginActivity.this, SignInUI.class);
        signin.login(LoginActivity.this, RefrigeratorMainActivity.class).execute();
    }
}
