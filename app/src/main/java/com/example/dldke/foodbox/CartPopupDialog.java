package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.dldke.foodbox.Adapter.PencilCartAdapter;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;

import java.util.ArrayList;
import java.util.List;

public class CartPopupDialog {
    private Context context;

    private PencilRecyclerAdapter pencilAdapter = new PencilRecyclerAdapter();
    private ArrayList<PencilCartItem> clickFood = pencilAdapter.getClickFood();
    private ArrayList<String> clickFoodString = pencilAdapter.getClickFoodString();


    public CartPopupDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        RecyclerView.Adapter adapter;

        Log.e("tag","callFunction");
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog_cartpopup);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        Log.e("tag","커스텀 다이얼 노출 됨");

        final RecyclerView cart_list_view = (RecyclerView) dlg.findViewById(R.id.cart_recycler);
        final Button getInside = (Button) dlg.findViewById(R.id.getInsideButton);
        final Button ok = (Button)dlg.findViewById(R.id.completeButton);

        cart_list_view.setHasFixedSize(true);
        adapter = new PencilCartAdapter(clickFood);
        cart_list_view.setLayoutManager(new LinearLayoutManager(context));
        cart_list_view.setAdapter(adapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("확인버튼","장바구니 보기 종료");
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        getInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("냉장고 넣기 버튼","장바구니 보기 종료");
                // 커스텀 다이얼로그를 종료한다.
                List<RefrigeratorDO.Item> clickedList = new ArrayList<>();
                for(int i =0 ; i<clickFoodString.size(); i++) {
                    PencilCartItem food = clickFood.get(i);
                    clickedList.add(Mapper.createFood(Mapper.searchFood(food.getFoodName(), food.getFoodSection()), food.getFoodCount()));
                }

                Log.e("clickedList",""+clickedList);
                Mapper.putFood(clickedList);
                pencilAdapter.setClickFoodStringNull();
                pencilAdapter.setClickFoodNull();
                dlg.dismiss();
            }
        });
    }
}
