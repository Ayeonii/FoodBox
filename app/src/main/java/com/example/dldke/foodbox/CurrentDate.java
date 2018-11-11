package com.example.dldke.foodbox;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CurrentDate {

    public CurrentDate(){

    }

    public String getCurrenDate()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String str_date = df.format(new Date());

        Log.e("Date of Today",""+str_date);
        return str_date;
    }


}
