package com.example.dldke.foodbox.CloudVision;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PopupDialog {

    private Context context;
    private NotMatchAdapter notMatchAdapter = new NotMatchAdapter();

    public static List<String> notMatchingInfo = new ArrayList<>();
    private static int index;
    private VisionActivity visionActivity = new VisionActivity();

    private FrameLayout frag;
    private RecyclerView ingredientView, notmatch;
    private EditText searchBar;
    private ImageButton deleteButton;
    private FloatingActionButton ok;

    private PopupAdapter popupAdapter;
    private static List<PencilItem> allfoodListInfo = new ArrayList<>();
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static ArrayList<PencilItem> visionList = new ArrayList<>();
    private static List<InfoDO> freshList, meatList, etcList;
    private String foodImg;
    private boolean isFrozen;
    private static Dialog dlg;

    public PopupDialog() {
    }

    public PopupDialog(Context context, RecyclerView notmatch, int index, List<String> notMatchingInfo) {
        this.context = context;
        this.notmatch = notmatch;
        this.index = index;
        this.notMatchingInfo = notMatchingInfo;

    }

    public ArrayList<PencilItem> getVisionList() {
        return visionList;
    }

    public void callFunction() {
        dlg = new Dialog(context);
        dlg.setContentView(R.layout.vision_ingredient_popup);
        dlg.show();
        ingredientView = (RecyclerView) dlg.findViewById(R.id.vision_ingredient_view);

        if (visionActivity.getEnterTime() == 0) {
            freshList = getInfoDOList("fresh");
            meatList = getInfoDOList("meat");
            etcList = getInfoDOList("etc");
            makeFoodList(freshList);
            makeFoodList(meatList);
            makeFoodList(etcList);
            visionActivity.setEnterTime(1);
        }
        ingredientView.setHasFixedSize(true);
        popupAdapter = new PopupAdapter(list, notmatch, index, notMatchingInfo, dlg);
        ingredientView.setLayoutManager(new GridLayoutManager(context, 4));
        ingredientView.setAdapter(popupAdapter);
        setData();
    }

    private List<InfoDO> getInfoDOList(String section) {
        return Mapper.scanSection(section);
    }

    private void makeFoodList(List<InfoDO> foodList) {

        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getKindOf() == "frozen") {
                isFrozen = true;
            } else {
                isFrozen = false;
            }
            allfoodListInfo.add(new PencilItem(foodList.get(i).getName(), foodList.get(i).getSection(), isFrozen));

            File file = new File(context.getFilesDir() + foodList.get(i).getName() + ".jpg");
            if (!file.exists()) {
                Mapper.downLoadImage(foodList.get(i).getName(), context.getFilesDir().toString(), foodList.get(i).getSection());
            }

        }
    }

    private void setData() {
        for (int i = 0; i < allfoodListInfo.size(); i++) {
            foodImg = "file://"+context.getFilesDir()+allfoodListInfo.get(i).getFoodName()+".jpg";
            list.add(new PencilItem(allfoodListInfo.get(i).getFoodName(), Uri.parse(foodImg), allfoodListInfo.get(i).getFoodSection(), allfoodListInfo.get(i).getIsFrozen()));
        }
        visionList = list;
        popupAdapter.notifyDataSetChanged();
    }
}