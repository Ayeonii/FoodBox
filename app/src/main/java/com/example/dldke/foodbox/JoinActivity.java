package com.example.dldke.foodbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JoinActivity extends AppCompatActivity {
    Button re_btn, join_btn;
    EditText jid_edittext, jpw_edittext, repw_edittext;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        re_btn = (Button)findViewById(R.id.re_button);
        join_btn = (Button)findViewById(R.id.join_button);
        jid_edittext = (EditText)findViewById(R.id.edittext_jid);
        jpw_edittext = (EditText)findViewById(R.id.edittext_jpw);
        repw_edittext = (EditText)findViewById(R.id.edittext_repw);

        re_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

            }
        });
        join_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

            }
        });
    }
}
