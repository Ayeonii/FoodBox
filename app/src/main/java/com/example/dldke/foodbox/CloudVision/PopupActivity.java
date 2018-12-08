package com.example.dldke.foodbox.CloudVision;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PopupActivity extends Activity implements View.OnClickListener{

    private RecyclerView ingredientView;
    private EditText searchBar;
    private ImageButton deleteButton;
    private FloatingActionButton ok;

    private PopupAdapter popupAdapter;
    private static List<String[]> allfoodList = new ArrayList<>();
    private ArrayList<PencilItem> list = new ArrayList<>();
    private static List<InfoDO> freshList, meatList, etcList;
    private String foodImg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vision_ingredient_popup);

        ingredientView = (RecyclerView) findViewById(R.id.vision_ingredient_view);
        searchBar = (EditText) findViewById(R.id.vision_searchBar);
        deleteButton = (ImageButton) findViewById(R.id.vision_delete_button);
        ok = (FloatingActionButton) findViewById(R.id.vision_ingredient_add);


        ingredientView.setHasFixedSize(true);
        popupAdapter = new PopupAdapter(list);
        ingredientView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        ingredientView.setAdapter(popupAdapter);


        freshList = Mapper.scanSection("fresh");
        meatList = Mapper.scanSection("meat");
        etcList = Mapper.scanSection("etc");

        makeFoodList(freshList, "fresh");
        makeFoodList(meatList,"meat");
        makeFoodList(etcList,"etc");


        searchBar.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    private void makeFoodList(List<InfoDO> foodList, String section) {
        for(int i =0 ; i< foodList.size(); i++) {
            allfoodList.add(new String[]{foodList.get(i).getName(), section});
            /**********이미지 추가후 주석 삭제**********/

//            File file = new File("/storage/emulated/0/Download/" + foodList.get(i).getName() + ".jpg");
//            if (!file.exists()) {
//                Mapper.downLoadImage(foodList.get(i).getName(), "/storage/emulated/0/Download/", section);
//            }
        }
    }

    private void setData(){
        for(int i =0 ; i<allfoodList.size(); i++ ){
            foodImg = "file:///storage/emulated/0/Download/"+allfoodList.get(i)[0]+".jpg";
            list.add(new PencilItem(allfoodList.get(i)[0], Uri.parse(foodImg),allfoodList.get(i)[1], false));
        }
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
                break;

                default:
                    break;
        }
    }
}
