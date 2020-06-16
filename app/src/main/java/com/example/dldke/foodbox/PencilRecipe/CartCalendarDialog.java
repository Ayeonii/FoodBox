package com.example.dldke.foodbox.PencilRecipe;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CartCalendarDialog {
    private Context context;
    private CurrentDate currentDate = new CurrentDate();
    private static String inputDBDateString;
    private static long diffDays;
    public CartCalendarDialog( Context context) {
        this.context = context;
    }

    public void callFunction(final TextView foodDate, final PencilCartItem mItems) {

        inputDBDateString = mItems.getFoodDate();

        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.pencilrecipe_dialog_calendar);
        dlg.show();


        final CalendarView calendar = (CalendarView) dlg.findViewById(R.id.calendarView);
        final Button cancel = (Button) dlg.findViewById(R.id.cancel_btn);
        final Button ok = (Button)dlg.findViewById(R.id.ok_btn);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                long diff;
                Date clickedDate;
                try {
                    if((month+1)<10){
                        clickedDate = formatter.parse(""+year+"0"+(month+1)+""+dayOfMonth);
                    }else{
                        clickedDate = formatter.parse(""+year+""+(month+1)+""+dayOfMonth);
                    }
                    inputDBDateString = formatter.format(clickedDate);
                    Date curDay = formatter.parse(currentDate.getCurrenDate());
                    Log.e("","currentDate.getCurrentDate():"+ currentDate.getCurrenDate());
                    diff = clickedDate.getTime() - curDay.getTime();
                    Log.e("","clickedDate = "+clickedDate+"curDay:"+curDay);
                    Log.e("","clickedDate.getTime() = "+clickedDate.getTime()/(24 * 60 * 60 * 1000)+"curDay.getTime():"+curDay.getTime()/(24 * 60 * 60 * 1000));
                    diffDays = diff / (24 * 60 * 60 * 1000);

                    Log.e("diffDays : ", ""+diffDays);

                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDate.setText("D-"+diffDays);
                mItems.setFoodDate(inputDBDateString);
                dlg.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
