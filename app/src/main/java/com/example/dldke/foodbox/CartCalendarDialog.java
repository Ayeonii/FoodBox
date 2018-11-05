package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.TextView;
import android.widget.Toast;

public class CartCalendarDialog {
    private Context context;

    public CartCalendarDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView foodDate) {

        Log.e("tag","callFunction");
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog_cart);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        Log.e("tag","커스텀 다이얼 노출 됨");
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final CalendarView calendar = (CalendarView) dlg.findViewById(R.id.calendarView);
        final Button cancel = (Button) dlg.findViewById(R.id.cancel_btn);
        final Button ok = (Button)dlg.findViewById(R.id.ok_btn);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(context, year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                //클릭한 날짜로 바꾸기 => 추후에 디비의 날짜 차이를 계산 해서 띄워야함.
                foodDate.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }

        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("확인버튼","달력 종료");
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
