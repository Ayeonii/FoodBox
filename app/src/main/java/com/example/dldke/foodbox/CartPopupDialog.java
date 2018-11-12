package com.example.dldke.foodbox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.dldke.foodbox.Activity.PencilRecipeActivity;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.Adapter.PencilCartAdapter;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;

import java.util.ArrayList;
import java.util.List;

public class CartPopupDialog {
    private Context context;
    private boolean isEnd = false;
    private PencilRecipeActivity pencilRecipeActivity = new PencilRecipeActivity();
    private PencilRecyclerAdapter pencilAdapter = new PencilRecyclerAdapter();
    private ArrayList<PencilCartItem> clickFood = pencilAdapter.getClickFood();

    private PencilCartAdapter pencilCartAdapter = new PencilCartAdapter(clickFood);
    private ArrayList<PencilCartItem> clickItems = pencilCartAdapter.getCartItems();

    public CartPopupDialog(Context context) {
        this.context = context;
    }

    public boolean getisEnd(){
        return isEnd;
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        RecyclerView.Adapter adapter;
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.custom_dialog_cartpopup);
        dlg.show();

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
                dlg.dismiss();
            }
        });
        getInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RefrigeratorDO.Item> clickedList = new ArrayList<>();
                for(int i =0 ; i<clickItems.size(); i++) {
                    PencilCartItem food = clickItems.get(i);
                    try {
                        clickedList.add(Mapper.createFood(Mapper.searchFood(food.getFoodName(), food.getFoodSection()), food.getFoodCount(), food.getFoodDate()));
                    }
                    catch (NullPointerException e){ //디비에 없는 재료를 냉장고에 넣고 싶을 때
                        clickedList.add(Mapper.createNonFood(food.getFoodName(), "sideDish" , food.getFoodCount(), food.getFoodDate()));
                    }
                }
                Log.e("clickedList",""+clickedList);
                Mapper.putFood(clickedList);

                pencilAdapter.getClickFood().clear();
                isEnd = true;

                dlg.dismiss();

            }
        });
    }
}
