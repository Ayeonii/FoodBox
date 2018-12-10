package com.example.dldke.foodbox.CloudVision;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.MyRecipe.CustomDialog;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PopupDialog implements View.OnClickListener {

    private Context context;
    private NotMatchAdapter notMatchAdapter = new NotMatchAdapter();
    public static List<String> notMatchingInfo = new ArrayList<>();
    private static int index;
    private VisionActivity visionActivity = new VisionActivity();


    private RecyclerView ingredientView, notmatch;
    private EditText searchBar;
    private ImageButton deleteButton;
    private FloatingActionButton ok;

    private PopupAdapter popupAdapter;
    private static List<PencilItem> allfoodListInfo = new ArrayList<>();
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static List<InfoDO> freshList, meatList, etcList;
    private String foodImg;
    private boolean isFrozen;
  //  private static List<PencilCartItem> changeItem;
    private static Dialog dlg;
    private String TAG="PopupDialog";

    public PopupDialog(Context context, RecyclerView notmatch, int index, List<String> notMatchingInfo){
        this.context = context;
        this.notmatch = notmatch;
        this.index = index;
        this.notMatchingInfo = notMatchingInfo;
    }

    public void callFunction( ) {

        dlg = new Dialog(context);
    //    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.vision_ingredient_popup);
        dlg.show();


        ingredientView = (RecyclerView) dlg.findViewById(R.id.vision_ingredient_view);
        searchBar = (EditText) dlg.findViewById(R.id.vision_searchBar);
        deleteButton = (ImageButton) dlg.findViewById(R.id.vision_delete_button);
        ok = (FloatingActionButton) dlg.findViewById(R.id.vision_ingredient_add);

        if(visionActivity.getEnterTime() == 0 ) {
            freshList = getInfoDOList("fresh");
            meatList = getInfoDOList("meat");
            etcList = getInfoDOList("etc");

            makeFoodList(freshList);
            makeFoodList(meatList);
            makeFoodList(etcList);

            visionActivity.setEnterTime(1);
        }
            ingredientView.setHasFixedSize(true);
            popupAdapter = new PopupAdapter(list);
            ingredientView.setLayoutManager(new GridLayoutManager(context, 4));
            ingredientView.setAdapter(popupAdapter);

            setData();

        searchBar.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {

        for(int i =0 ; i< foodList.size(); i++) {
            if(foodList.get(i).getKindOf() == "frozen"){
                isFrozen = true;
            }
            else {
                isFrozen = false;
            }

            allfoodListInfo.add(new PencilItem(foodList.get(i).getName(), foodList.get(i).getSection(), isFrozen));

            File file = new File("/storage/emulated/0/Download/" + foodList.get(i).getName() + ".jpg");
            if (!file.exists()) {
                Mapper.downLoadImage(foodList.get(i).getName(), "/storage/emulated/0/Download/", foodList.get(i).getSection());
            }
        }
    }

    private void setData(){
        for(int i =0 ; i<allfoodListInfo.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+allfoodListInfo.get(i).getFoodName()+".jpg";
            list.add(new PencilItem(allfoodListInfo.get(i).getFoodName(), Uri.parse(foodImg),allfoodListInfo.get(i).getFoodSection(), allfoodListInfo.get(i).getIsFrozen()));
        }
        popupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.vision_searchBar:
                break;
            case R.id.vision_delete_button:
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 재료명을 입력하세요.");
                    searchBar.setText(null);
                }
                break;
            case R.id.vision_ingredient_add:
                notMatchingInfo.remove(index);
                for(int i = 0; i<notMatchingInfo.size(); i++){
                    Log.e(TAG, "재료 : "+notMatchingInfo.get(i));
                }
                VisionReturnActivity visionReturnActivity = new VisionReturnActivity();
                visionReturnActivity.notMatchingIngredient(notmatch, notMatchingInfo);
                //notMatchAdapter.notifyItemRemoved(index);
                //notMatchAdapter.notifyItemRangeChanged(index, notMatchingInfo.size());
                notMatchAdapter.notifyDataSetChanged();
                //cancel();
                dlg.dismiss();
                break;

            default:
                break;
        }
    }
}