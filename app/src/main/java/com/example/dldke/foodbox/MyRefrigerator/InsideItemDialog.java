package com.example.dldke.foodbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.InsideAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InsideItemDialog extends Dialog implements View.OnClickListener {

    private TextView txtName, txtCount, txtDueDate, txtOk;
    private ImageView ivCountEdit, ivDueDateEdit, ivDelete, clickImg;
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

    public InsideItemDialog(@NonNull Context context, String name, Double count, String dueDate, Uri foodImg) {
        super(context);
        this.context = context;
        this.mName = name;
        this.mCount = count;
        this.mDueDate = dueDate;
        this.imgUri = foodImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_item_dialog);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtCount = (TextView) findViewById(R.id.txt_count);
        txtDueDate = (TextView) findViewById(R.id.txt_dueDate);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        ivCountEdit = (ImageView) findViewById(R.id.iv_count_edit);
        ivDueDateEdit = (ImageView) findViewById(R.id.iv_dueDate_edit);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        clickImg = (ImageView)findViewById(R.id.img_food);
        clickImg.setImageURI(imgUri);


        ivCountEdit.setOnClickListener(this);
        ivDueDateEdit.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
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
                        Log.d("test", "진짜 디비 연결해서 삭제되는 부분");
                        Mapper.deleteFood(mName);
                        delResult = 1;
                    }
                });
                alertDialog.setNegativeButton("취소", null);
                alertDialog.show();
                break;
            case R.id.txt_ok:
                dialogListener.onPositiveClicked(delResult, mCount, mDueDate);
                dismiss();
                break;
        }
    }
}
