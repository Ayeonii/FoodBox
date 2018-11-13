package com.example.dldke.foodbox.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

public class JoinActivity extends AppCompatActivity {
    Button dupl_btn, join_btn;
    EditText join_id, join_pw, dupl_pw;
    TextView check_pw;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        dupl_btn = (Button)findViewById(R.id.re_button);
        join_btn = (Button)findViewById(R.id.join_button);
        join_id = (EditText)findViewById(R.id.edittext_jid);
        join_pw = (EditText)findViewById(R.id.edittext_jpw);
        dupl_pw = (EditText)findViewById(R.id.edittext_repw);
        check_pw = (TextView)findViewById(R.id.edittext_repw_message);

        dupl_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(join_pw.getText().toString().equals(dupl_pw.getText().toString())){
                    check_pw.setVisibility(View.INVISIBLE);
                }
                else {
                    check_pw.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dupl_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

            }
        });
        join_btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainActivity);
            }
        });
    }
}
