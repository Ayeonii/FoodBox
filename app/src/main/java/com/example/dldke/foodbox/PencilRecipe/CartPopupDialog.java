package com.example.dldke.foodbox.PencilRecipe;

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

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeActivity;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class CartPopupDialog {
    private Context context;
    private boolean isEnd = false, isFullRecipe;
    private FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
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
    public void callFunction(final Context activityContext) {
        isFullRecipe = fullRecipeActivity.getIsFullRecipe();
        RecyclerView.Adapter adapter;
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.pencilrecipe_dialog_cart);
        dlg.show();

        final RecyclerView cart_list_view = (RecyclerView) dlg.findViewById(R.id.cart_recycler);
        final Button getInside = (Button) dlg.findViewById(R.id.getInsideButton);
        final Button ok = (Button)dlg.findViewById(R.id.completeButton);

        if(isFullRecipe){
            getInside.setText("풀레시피 작성하기");
        }
        cart_list_view.setHasFixedSize(true);
        adapter = new PencilCartAdapter(clickFood);
        cart_list_view.setLayoutManager(new LinearLayoutManager(context));
        cart_list_view.setAdapter(adapter);


        //확인버튼
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        //냉장고에 넣기 버튼
        getInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFullRecipe){
                    Log.e("CartPopupDialog", "풀레시피에서 왔음");
                    Intent intent = new Intent(activityContext, FullRecipeActivity.class);
                    activityContext.startActivity(intent);
                    dlg.dismiss();
                }

                List<RefrigeratorDO.Item> clickedList = new ArrayList<>();
                for(int i =0 ; i<clickItems.size(); i++) {
                    PencilCartItem food = clickItems.get(i);
                    try {
                        clickedList.add(Mapper.createFood(Mapper.searchFood(food.getFoodName(), food.getFoodSection()), food.getFoodCount(), food.getFoodDate(), food.getIsFrozen()));
                    }
                    catch (NullPointerException e){ //디비에 없는 재료를 냉장고에 넣고 싶을 때
                        clickedList.add(Mapper.createNonFood(food.getFoodName(), "sideDish" , food.getFoodCount(), food.getFoodDate(), food.getIsFrozen()));
                    }
                }
                //Log.e("clickedList",""+clickedList);
                Mapper.putFood(clickedList);
                Mapper.updateToBuyMemo(clickedList);
                Toast.makeText(context, "냉장고에 재료가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                pencilAdapter.getClickFood().clear();
                pencilAdapter.setClickCnt(0);
                Intent refMain = new Intent(activityContext, RefrigeratorMainActivity.class);
                refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(refMain);
                dlg.dismiss();

            }
        });
    }
}