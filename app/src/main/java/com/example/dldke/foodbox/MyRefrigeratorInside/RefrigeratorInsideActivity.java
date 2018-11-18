package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorInsideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSidedish, btnDairy, btnEtc, btnMeat, btnFresh;

    private List<RefrigeratorDO.Item> refrigeratorItem;
    private ArrayList<LocalRefrigeratorItem> localSideDish, localDairy, localEtc, localMeat, localFresh;
    private ArrayList<String> dupliArray = new ArrayList<>();

    private InsideDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_inside);

        btnSidedish = (Button) findViewById(R.id.btn_sidedish);
        btnDairy = (Button) findViewById(R.id.btn_dairy);
        btnEtc = (Button) findViewById(R.id.btn_etc);
        btnMeat = (Button) findViewById(R.id.btn_meat);
        btnFresh = (Button) findViewById(R.id.btn_fresh);

        btnSidedish.setOnClickListener(this);
        btnDairy.setOnClickListener(this);
        btnEtc.setOnClickListener(this);
        btnMeat.setOnClickListener(this);
        btnFresh.setOnClickListener(this);

        //해당 아이디의 create된 냉장고가 없을 경우 create해야됨
        try {
            refrigeratorItem = Mapper.scanRefri();
        } catch (NullPointerException e) {
            Mapper.createRefrigerator();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sidedish:
                Log.d("test", "======sideDish======");
                scanToLocalRefrigerator("sideDish");
                showDialog("sideDish", localSideDish);

                break;
        }
    }

    public void showDialog(String type, ArrayList<LocalRefrigeratorItem> localArray) {
        Log.d("test", "======"+type+"======");

        if (localArray.size() == 0)
            dialog = new InsideDialog(this, type, true);
        else
            dialog = new InsideDialog(this, type, false, localArray);

        dialog.show();
    }

    public void scanToLocalRefrigerator(String type) {
        refrigeratorItem = Mapper.scanRefri();

        switch (type) {
            case "sideDish":
                localSideDish = new ArrayList<>();
                for (int i = 0; i < refrigeratorItem.size(); i++) {
                    try {
                        if (refrigeratorItem.get(i).getSection().equals(type)) {
                            if (duplicateCheck(refrigeratorItem.get(i).getName(), localSideDish)) {
                                dupliArray.add(refrigeratorItem.get(i).getName());
                            } else {
                                localSideDish.add(new LocalRefrigeratorItem(refrigeratorItem.get(i).getName(), refrigeratorItem.get(i).getCount(), refrigeratorItem.get(i).getDueDate()));
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.d("test", "sideDish null: " + e.getMessage());
                    }
                }

                //Log출력하는 부분
                Log.d("test", "duplicate===========");
                for (int i=0; i<dupliArray.size(); i++) {
                    Log.d("test", dupliArray.get(i));
                }
                Log.d("test", "localSideDish===========");
                for (int i=0; i<localSideDish.size(); i++) {
                    Log.d("test", localSideDish.get(i).getName());
                }
                break;
        }
    }

    public boolean duplicateCheck(String foodName, ArrayList<LocalRefrigeratorItem> localArray) {
        // (유통기한때문에) localArray에 같은 이름이 이미 있으면 true, 아니면 false
        // true : localArray에 넣지 말고 dupliArray에 넣는다.
        // false : localArray에만 넣는다.
        for (int i = 0; i < localArray.size(); i++) {
            if (localArray.get(i).getName().equals(foodName)) {
                return true;
            }
        }
        return false;
    }
}