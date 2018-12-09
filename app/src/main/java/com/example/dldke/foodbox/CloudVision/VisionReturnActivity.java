package com.example.dldke.foodbox.CloudVision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.api.id3idutfky0i.TestMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;
import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.PencilRecipe.PencilCartAdapter;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisionReturnActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button insert;
    private ImageView receipt_img;
    private TextView loading;
    private static RecyclerView match, notmatch;

    private List<InfoDO> matchingList;
    private List<String> notmatchingList;
    private Bitmap bitmap;

    private static GregorianCalendar cal = new GregorianCalendar();
    private static Date inputDBDate;
    private static String inputDBDateString;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private static boolean isFrozen;
    private ArrayList<PencilCartItem> matchFood = new ArrayList<>();

    private VisionActivity visionActivity = new VisionActivity();

    private String TAG="VisionReturnActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_return);

        Log.e(TAG, "matching"+matchingList);
        Log.e(TAG, "notmatching"+notmatchingList);

        //toolbar = (Toolbar) findViewById(R.id.vision_return_toolbar);
        receipt_img = (ImageView) findViewById(R.id.vision_return_main_image);
        loading = (TextView) findViewById(R.id.vision_return_loading_text);
        insert = (Button) findViewById(R.id.vision_return_btn);
        match = (RecyclerView) findViewById(R.id.vision_return_matching_ingredient_view);
        notmatch = (RecyclerView) findViewById(R.id.vision_return_notmatching_ingredient_view);

        bitmap = visionActivity.getBitmap();
        receipt_img.setImageBitmap(bitmap);
        loading.setVisibility(View.INVISIBLE);

        matchingList = visionActivity.getMatch();
        notmatchingList = visionActivity.getNotMatch();

        matchingIngredient(matchingList);
        notMatchingIngredient(notmatch, notmatchingList);

        insert.setOnClickListener(this);
    }

    public void matchingIngredient(List<InfoDO> matching) {
        List<InfoDO> matchingList = matching;
        for (int i = 0; i < matchingList.size(); i++) {
            String matching_name = matchingList.get(i).getName();
            Log.e(TAG, "matching_name" + matching_name);

            int dueDate;
            try {
                dueDate = Mapper.searchFood(matchingList.get(i).getName(), matchingList.get(i).getSection()).getDueDate();
            } catch (NullPointerException e) {
                dueDate = 0;
            }
            cal.add(cal.DATE, dueDate);
            inputDBDate = cal.getTime();
            inputDBDateString = formatter.format(inputDBDate);

            String foodImg = "file:///storage/emulated/0/Download/" + matchingList.get(i).getName() + ".jpg";
            Uri uri = Uri.parse(foodImg);
            if (matchingList.get(i).getKindOf() == "frozen") {
                isFrozen = true;
            } else {
                isFrozen = false;
            }
            matchFood.add(new PencilCartItem(matchingList.get(i).getName(), uri, inputDBDateString, 1, matchingList.get(i).getSection(), isFrozen, dueDate));
        }

        PencilCartAdapter adapter;
        match.setHasFixedSize(true);
        adapter = new PencilCartAdapter(matchFood);
        match.setLayoutManager(new LinearLayoutManager(this));
        match.setAdapter(adapter);

        for (int i = 0; i < matchFood.size(); i++) {
            Log.d(TAG, "재료 이름 : " + matchFood.get(i).getFoodName() + " Section : " + matchFood.get(i).getFoodSection() + " 유통기한 : " + matchFood.get(i).getFoodDate() + "냉동고? : " + matchFood.get(i).getIsFrozen());
        }
    }

    public void notMatchingIngredient(RecyclerView notmatch_view, List<String> items) {
        NotMatchAdapter adapter;
        notmatch_view.setHasFixedSize(true);
        adapter = new NotMatchAdapter(items, this, notmatch_view);
        notmatch_view.setLayoutManager(new LinearLayoutManager(this));
        notmatch_view.setAdapter(adapter);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.vision_return_btn:
                List<RefrigeratorDO.Item> clickedList = new ArrayList<>();
                for (int i = 0; i < matchFood.size(); i++) {
                    PencilCartItem food = matchFood.get(i);
                    try {
                        Log.e("Dialog", "" + food.getIsFrozen());
                        clickedList.add(Mapper.createFood(Mapper.searchFood(food.getFoodName(), food.getFoodSection()), food.getFoodCount(), food.getFoodDate(), food.getIsFrozen()));
                    } catch (NullPointerException e) { //디비에 없는 재료를 냉장고에 넣고 싶을 때
                        Log.e("Dialog", "" + food.getIsFrozen());
                        clickedList.add(Mapper.createNonFood(food.getFoodName(), "sideDish", food.getFoodCount(), food.getFoodDate(), food.getIsFrozen()));
                    }
                }
                Mapper.putFood(clickedList);
                Toast.makeText(getApplicationContext(), "냉장고에 재료가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                Intent refMain = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                refMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refMain);
                break;
            default:
                break;
        }
    }
}
