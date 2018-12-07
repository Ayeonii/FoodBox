package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

public class DCEditDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button btnOk, btnMinus, btnPlus;
    private CalendarView calendarView;
    private TextView txtCountEdit;

    private String dueDateEdit, countEdit_str;
    private Double countEdit_double;

    private InsideDialogListener dialogListener;

    public DCEditDialog(@NonNull Context context, String dueDateEdit, Double countEdit_double) {
        super(context);
        this.context = context;
        this.dueDateEdit = dueDateEdit;
        this.countEdit_double = countEdit_double;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refrigerator_dc_dialog);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        btnMinus = (Button) findViewById(R.id.btn_minus);
        btnPlus = (Button) findViewById(R.id.btn_plus);
        btnOk = (Button) findViewById(R.id.btn_ok);
        txtCountEdit = (TextView) findViewById(R.id.txt_count_edit);

        countEdit_str = Double.toString(countEdit_double);
        txtCountEdit.setText(countEdit_str);

        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dueDateEdit = year+""+(month+1)+""+dayOfMonth;
                Log.d("test", dueDateEdit);
            }
        });
    }

    public void setDialogListener(InsideDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_minus:
                if (countEdit_double > 0.5) {
                    countEdit_double -= 0.5;
                    countEdit_str = Double.toString(countEdit_double);
                    txtCountEdit.setText(countEdit_str);
                }
                break;
            case R.id.btn_plus:
                countEdit_double+=0.5;
                countEdit_str = Double.toString(countEdit_double);
                txtCountEdit.setText(countEdit_str);
                break;
            case R.id.btn_ok:
                dialogListener.onPositiveClicked(countEdit_double, dueDateEdit);
                dismiss();
                break;
        }
    }
}
