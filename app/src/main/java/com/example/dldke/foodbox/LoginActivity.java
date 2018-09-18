package com.example.dldke.foodbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button ok_btn;
    EditText id_edittext, pw_edittext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ok_btn = (Button)findViewById(R.id.login_button);
        id_edittext = (EditText)findViewById(R.id.edittext_id);
        pw_edittext = (EditText)findViewById(R.id.edittext_pw);
    }
}
