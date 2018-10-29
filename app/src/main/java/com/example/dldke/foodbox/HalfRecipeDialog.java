package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HalfRecipeDialog extends Dialog {

    private TextView mTitleView;
    private TextView mContentView;  //recyclerview
    private String mTitle;
    private String mContent;
    private TextView mCancelTextView;
    private TextView mOkTextView;

    private View.OnClickListener mCancelClickListener;
    private View.OnClickListener mOkClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe_dialog);

        mTitleView = (TextView) findViewById(R.id.dialog_title);
        mContentView = (TextView) findViewById(R.id.dialog_content);
        mCancelTextView = (TextView) findViewById(R.id.dialog_cancel);
        mOkTextView = (TextView) findViewById(R.id.dialog_ok);

        mTitleView.setText(mTitle);
        mContentView.setText(mContent);

        if(mCancelClickListener != null && mOkClickListener != null) {
            mCancelTextView.setOnClickListener(mCancelClickListener);
            mOkTextView.setOnClickListener(mOkClickListener);
        } else if(mCancelClickListener != null && mOkClickListener == null) {
            mCancelTextView.setOnClickListener(mCancelClickListener);
        } else {
            mOkTextView.setOnClickListener(mOkClickListener);
        }
    }

    public HalfRecipeDialog(Context context, String title, String content,
                      View.OnClickListener cancelListener, View.OnClickListener okListener) {
        super(context);
        this.mTitle = title;
        this.mContent = content;
        this.mCancelClickListener = cancelListener;
        this.mOkClickListener = okListener;
    }
}
