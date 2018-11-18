package com.example.dldke.foodbox.MyRefrigerator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

public class InsideItemDialog extends Dialog implements View.OnClickListener {

    private TextView txtName, txtCount, txtDueDate, txtOk;
    private ImageView imgCountEdit, imgDueDateEdit, imgDelete, imgFood;
    private Uri imgUri;

    private Context context;
    private String mName, mDueDate;
    private Double mCount;

    private InsideDialogListener dialogListener;
    private int delResult;

    public InsideItemDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public InsideItemDialog(@NonNull Context context, String name, Double count, String dueDate, Uri imgUri) {
        super(context);
        this.context = context;
        this.mName = name;
        this.mCount = count;
        this.mDueDate = dueDate;
        this.imgUri = imgUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_item_dialog);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtCount = (TextView) findViewById(R.id.txt_count);
        txtDueDate = (TextView) findViewById(R.id.txt_dueDate);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        imgCountEdit = (ImageView) findViewById(R.id.iv_count_edit);
        imgDueDateEdit = (ImageView) findViewById(R.id.iv_dueDate_edit);
        imgDelete = (ImageView) findViewById(R.id.iv_delete);
        imgFood = (ImageView)findViewById(R.id.img_food);
        imgFood.setImageURI(imgUri);

        imgCountEdit.setOnClickListener(this);
        imgDueDateEdit.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        txtOk.setOnClickListener(this);

        txtName.setText(mName);
        txtCount.setText("보유개수 " + mCount);
        txtDueDate.setText("유통기한 " + mDueDate);

        delResult = 0;  //1 : 삭제한 재료
    }

    public void setDialogListener(InsideDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_count_edit:
                //number picker
                Log.d("test", "onClick : iv_count_edit");
                break;
            case R.id.iv_dueDate_edit:
                //calender picker
                Log.d("test", "onClick : iv_dueDate_edit");
                break;
            case R.id.iv_delete:
                //alert dialog
                Log.d("test", "onClick : iv_delete");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("재료 삭제");
                alertDialog.setMessage("재료를 삭제하시겠습니까?");
                alertDialog.setPositiveButton("확인", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Mapper.deleteFood(mName);
                        delResult = 1;
                        dialogListener.onPositiveClicked(delResult, mCount, mDueDate);
                        dismiss();
                    }
                });
                alertDialog.setNegativeButton("취소", null);
                alertDialog.show();
                break;
            case R.id.txt_ok:
                //dialogListener.onPositiveClicked(delResult, mCount, mDueDate);
                dismiss();
                break;
        }
    }
}
