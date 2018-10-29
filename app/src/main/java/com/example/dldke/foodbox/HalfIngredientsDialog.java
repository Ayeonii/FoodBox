package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;

import java.util.List;

public class HalfIngredientsDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private TextView txtCategory, txtContent, txtCancel, txtOk;

    private String category, content;

    public HalfIngredientsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public HalfIngredientsDialog(@NonNull Context context, String category, String content) {
        super(context);
        this.context = context;
        this.category = category;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_ingredients_dialog);

        txtCategory = (TextView) findViewById(R.id.textCategory);
        txtContent = (TextView) findViewById(R.id.textContent);
        txtCancel = (TextView) findViewById(R.id.textCancel);
        txtOk = (TextView) findViewById(R.id.textOk);

        txtCancel.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        if(category != null) {
            txtCategory.setText(category);
        }
        if(content != null) {
            List<InfoDO> itemList = Mapper.scanInfo(category);
            for(int i = 0; i < itemList.size(); i++)
            {
                Log.d("recipe", String.format("Refri Item: %s", itemList.get(i).getName()));
            }
            txtContent.setText(content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textCancel:
                cancel();
                break;
            case R.id.textOk:
                dismiss();
                break;
        }
    }
}
