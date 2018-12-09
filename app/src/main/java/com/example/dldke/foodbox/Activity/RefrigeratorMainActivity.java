package com.example.dldke.foodbox.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.example.dldke.foodbox.CloudVision.VisionActivity;
import com.example.dldke.foodbox.Community.CommunityActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.FullRecipe.FullRecipeActivity;
import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeActivity;
import com.example.dldke.foodbox.Memo.MemoActivity;
import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.example.dldke.foodbox.MyRefrigeratorInside.RefrigeratorFrozenInsideActivity;
import com.example.dldke.foodbox.MyRefrigeratorInside.RefrigeratorInsideActivity;
import com.example.dldke.foodbox.PencilRecipe.CurrentDate;
import com.example.dldke.foodbox.PencilRecipe.PencilRecipeActivity;
import com.example.dldke.foodbox.PencilRecipe.PencilRecyclerAdapter;
import com.example.dldke.foodbox.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class RefrigeratorMainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_refrigerator;
    private PencilRecyclerAdapter pencilAdapter = new PencilRecyclerAdapter();
    private static List<RecipeDO.Ingredient> urgentList = new ArrayList<>();
    private static List<RecipeDO.Ingredient> tobuyList = new ArrayList<>();
    public RefrigeratorMainActivity(){ }
    String TAG = "RefrigeratorMainActivity";

    public List<RecipeDO.Ingredient> getUrgentList(){
        return urgentList;
    }

    public List<RecipeDO.Ingredient> getTobuyList() {
        return tobuyList;
    }

    public void setUrgentList(List<RecipeDO.Ingredient> urgentList){
        this.urgentList = urgentList;
    }

    /*********************FloatingButtons***********************/
    //플로팅 버튼 애니메이션
    Animation ShowPlus, HidePlus, LayHide, ShowMinus, HideMinus, LayShow;
    //플로팅 버튼
    private FloatingActionButton fabPlus, fabCamera, fabPencil, fabMinus, fabFull, fabMini;
    //플로팅 레이아웃 (아이콘 + 아이콘 글씨)
    LinearLayout CameraLayout, PencilLayout, FullLayout, MiniLayout;
    //플로팅 배경 레이아웃
    RelativeLayout plusBack, minusBack;

    /***********************Menu****************************/
    //메뉴 버튼
    ImageView menu;
    //메뉴 배경 레이아웃
    LinearLayout menuTransBack;
    //메뉴창에 들어갈 리스트
    static final String[] LIST_MENU = {"내 레시피 보기", "Community", "Store", "설정", "로그아웃"};
    //메뉴 슬라이딩 열기/닫기 플래그
    boolean isPageOpen = false;
    //메뉴 슬라이드 열기/닫기 애니메이션
    Animation leftAnim, rightAnim;
    //메뉴 슬라이드 레이아웃
    LinearLayout menuPage;
    //메뉴창 리스트뷰
    ListView listview;

    /***********************Refrigerator****************************/
    //냉장고 오른쪽/왼쪽 부분
    Button leftDoor;
    Button rightDoor;

    /***************************memo********************************/
    private ImageView urgent_postit, tobuy_postit;
    private TextView txtUrgent1, txtUrgent2, txtUrgent3;
    private TextView txtTobuy;
    private static long diffDays;
    private CurrentDate currentDate = new CurrentDate();

    /***************************etc********************************/
    public static boolean isCookingClass;
    private String user_id;


    //public RefrigeratorMainActivity(){  }

    public boolean getisCookingClass(){
        return isCookingClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        Log.e("test", "onCreate() 들어옴");

        //User DB Create
        Mapper.setUserId(getApplicationContext());
        try {
            user_id = Mapper.searchUserInfo().getUserId();
            Log.e(TAG, "유저 아이디 : "+user_id);
        } catch (NullPointerException e) {
            Mapper.createUserInfo();
            Log.e(TAG, "유저 아이디 : "+user_id+"쿠킹 클래스? "+Mapper.searchUserInfo().getIsCookingClass()+"포인트 : "+Mapper.searchUserInfo().getPoint());
        }

        Mapper.checkAndCreateFirst();


        //Mapper.createMemo();
        if(pencilAdapter.getClickCnt() != 0 ){
            pencilAdapter.setClickCnt(0);
        }

        //Separate User vs CookingClass
        isCookingClass = Mapper.searchUserInfo().getIsCookingClass();

        //Toast.makeText(RefrigeratorMainActivity.this, "UserPoolId"+Mapper.getUserId(), Toast.LENGTH_SHORT).show();
        //pencilAdapter.getClickFoodString().clear();
        pencilAdapter.getClickFood().clear();
        /*메뉴*/
        menuTransBack = (LinearLayout) findViewById(R.id.transparentBack);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        menu = (ImageView) findViewById(R.id.menu);
        listview = (ListView) findViewById(R.id.optionList);
        listview.setAdapter(adapter);
        menuPage = (LinearLayout) findViewById(R.id.optionPage);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.go_left);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.go_right);
        //메뉴 애니메이션 이벤트리스너
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        leftAnim.setAnimationListener(animationListener);
        rightAnim.setAnimationListener(animationListener);

        /*플로팅 버튼*/
        plusBack = (RelativeLayout) findViewById(R.id.plusLayout);
        minusBack = (RelativeLayout) findViewById(R.id.minusLayout);

        fabPlus = (FloatingActionButton) findViewById(R.id.fabPlus);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabPencil = (FloatingActionButton) findViewById(R.id.fabPencil);

        CameraLayout = (LinearLayout) findViewById(R.id.cameraLayout);
        PencilLayout = (LinearLayout) findViewById(R.id.pencilLayout);

        fabMinus = (FloatingActionButton) findViewById(R.id.fabMinus);
        fabFull = (FloatingActionButton) findViewById(R.id.fabFull);
        fabMini = (FloatingActionButton) findViewById(R.id.fabMini);

        FullLayout = (LinearLayout) findViewById(R.id.fullLayout);
        MiniLayout = (LinearLayout) findViewById(R.id.miniLayout);

        //플로팅 애니메이션
        ShowPlus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_button);
        HidePlus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_button);
        ShowMinus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_minus_button);
        HideMinus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_minus_button);
        LayShow = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_layout);
        LayHide = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_layout);

        /*memo*/
        urgent_postit = (ImageView) findViewById(R.id.urgent_postit);
        tobuy_postit = (ImageView) findViewById(R.id.tobuy_postit);
        txtUrgent1 = (TextView) findViewById(R.id.urgent_item1);
        txtUrgent2 = (TextView) findViewById(R.id.urgent_item2);
        txtUrgent3 = (TextView) findViewById(R.id.urgent_item3);
        txtTobuy = (TextView) findViewById(R.id.tobuy_items);

        /*etc 버튼들*/
        leftDoor = (Button) findViewById(R.id.leftButton);
        rightDoor = (Button) findViewById(R.id.rightButton);

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        ListClickListener listClickListener = new ListClickListener();

        /************버튼 리스너들 시작**************/

        fabPlus.setOnClickListener(onClickListener);
        plusBack.setOnClickListener(onClickListener);
        fabCamera.setOnClickListener(onClickListener);
        fabPencil.setOnClickListener(onClickListener);

        fabMinus.setOnClickListener(onClickListener);
        minusBack.setOnClickListener(onClickListener);
        fabFull.setOnClickListener(onClickListener);
        fabMini.setOnClickListener(onClickListener);

        rightDoor.setOnClickListener(onClickListener);
        leftDoor.setOnClickListener(onClickListener);

        menu.setOnClickListener(onClickListener);
        menuTransBack.setOnClickListener(onClickListener);
        listview.setOnItemClickListener(listClickListener);

        urgent_postit.setOnClickListener(onClickListener);
        tobuy_postit.setOnClickListener(onClickListener);

        MemoCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("test", "onResume() 들어옴");
        MemoCreate();
    }

    /*************리스트뷰 리스너************/
    class ListClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            //클릭한 리스트의 text가져오기
            String strText = (String) parent.getItemAtPosition(position);

            if (strText.equals("로그아웃")) {
                IdentityManager.getDefaultIdentityManager().signOut();
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                //로그아웃 후, 뒤로가기 누르면 다시 로그인 된 상태로 가는 것을 방지
                MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(MainActivity);
            }

            if (strText.equals("내 레시피 보기")) {
                Intent MyRecipeBoxActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity.class);
                startActivity(MyRecipeBoxActivity);
            }
            //Toast.makeText(RefrigeratorMainActivity.this, strText+"눌렸어용", Toast.LENGTH_SHORT).show();

            if (strText.equals("Community")) {
                Intent communityActivity = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(communityActivity);
            }
            if (strText.equals("설정")){
                Intent settingActivity = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(settingActivity);
            }

            if (isPageOpen) {
                //fabPlus.setElevation(10);
                //fabMinus.setElevation(10);
                menuTransBack.setVisibility(View.GONE);
                menuPage.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                isPageOpen = false;
            }
            //슬라이드 닫기->열기
            else
                isPageOpen = true;
        }
    }

    /******************메뉴버튼 애니메이션 리스너***************/
    class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            //슬라이드 열기->닫기
            if (isPageOpen) {
                //fabPlus.setElevation(10);
                //fabMinus.setElevation(10);
                menuTransBack.setVisibility(View.GONE);
                menuPage.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                isPageOpen = false;
            }
            //슬라이드 닫기->열기
            else
                isPageOpen = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        }
    }

    /*********************버튼 리스너********************************/
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fabPlus:
                case R.id.plusLayout:
                    //열려있으면 닫고 여러 이벤트
                    if (plusBack.getVisibility() == View.VISIBLE) {
                        plusBack.setVisibility(View.GONE);
                        CameraLayout.setVisibility(View.GONE);
                        PencilLayout.setVisibility(View.GONE);
                        fabCamera.startAnimation(LayHide);
                        fabPencil.startAnimation(LayHide);
                        fabPlus.startAnimation(HidePlus);
                        fabMinus.setElevation(10);
                    }
                    //닫혀있으면 열고 여러 이벤트
                    else {
                        CameraLayout.setVisibility(View.VISIBLE);
                        PencilLayout.setVisibility(View.VISIBLE);
                        plusBack.setVisibility(View.VISIBLE);
                        fabCamera.startAnimation(LayShow);
                        fabPencil.startAnimation(LayShow);
                        fabPlus.startAnimation(ShowPlus);
                        fabMinus.setElevation(0);
                    }
                    break;

                case R.id.fabMinus:
                case R.id.minusLayout:
                    //열려있으면 닫기
                    if (minusBack.getVisibility() == View.VISIBLE) {
                        minusBack.setVisibility(View.GONE);
                        FullLayout.setVisibility(View.GONE);
                        MiniLayout.setVisibility(View.GONE);
                        fabFull.startAnimation(LayHide);
                        fabMini.startAnimation(LayHide);
                        fabMinus.startAnimation(HideMinus);
                        fabPlus.setElevation(10);
                    }
                    //닫혀있으면 열기
                    else {
                        minusBack.setVisibility(View.VISIBLE);
                        FullLayout.setVisibility(View.VISIBLE);
                        MiniLayout.setVisibility(View.VISIBLE);
                        fabFull.startAnimation(LayShow);
                        fabMini.startAnimation(LayShow);
                        fabMinus.startAnimation(ShowMinus);
                        fabPlus.setElevation(0);
                    }
                    break;

                case R.id.fabCamera:
                    //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //Intent visionIntent = new Intent(getApplicationContext(), VisionActivity.class);
                    //startActivity(visionIntent);
                    //Intent deepLink = new Intent(getApplicationContext(),DeepLinkActivity.class);
                    //startActivity(deepLink);

                    Intent intent = new Intent(getApplicationContext(), VisionActivity.class);
                    startActivity(intent);

                    break;
                case R.id.fabPencil:
                    Intent PencilActivity = new Intent(getApplicationContext(),PencilRecipeActivity.class);
                    startActivity(PencilActivity);
                    //다음 화면이 아래에서 올라오는 애니메이션
                    overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
                    break;
                case R.id.fabFull:
                    if(isCookingClass){
                        FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
                        fullRecipeActivity.setIsHalfRecipe(false);
                        Intent FullRecipeActivity = new Intent(getApplicationContext(), FullRecipeActivity.class);
                        startActivity(FullRecipeActivity);
                        overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
                    }
                    else{
                        Intent myRecipeBoxActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                        startActivity(myRecipeBoxActivity);
                        //다음 화면이 아래에서 올라오는 애니메이션
                        overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
                    }

                    break;
                case R.id.fabMini:
                    Intent halfRecipeActivity = new Intent(getApplicationContext(), HalfRecipeActivity.class);
                    halfRecipeActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(halfRecipeActivity);
                    //다음 화면이 아래에서 올라오는 애니메이션
                    overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
                    break;
                case R.id.rightButton:
                    Intent rightSideActivity = new Intent(getApplicationContext(), RefrigeratorInsideActivity.class);
                    startActivity(rightSideActivity);
                    break;
                case R.id.leftButton:
                    Intent leftSideActivity = new Intent(getApplicationContext(), RefrigeratorFrozenInsideActivity.class);
                    startActivity(leftSideActivity);
                    break;
                case R.id.menu:
                    menuPage.startAnimation(leftAnim);
                    menuPage.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    //fabPlus.setElevation(0);
                    //fabMinus.setElevation(0);
                    menuTransBack.setVisibility(View.VISIBLE);
                    break;
                case R.id.transparentBack:
                    //메뉴 누른 후 뒷배경 버튼 안 먹게 하기 위함.
                    menuPage.startAnimation(rightAnim);
                    break;
                case R.id.urgent_postit:
                    Intent memoActivity1 = new Intent(getApplicationContext(), MemoActivity.class);
                    startActivity(memoActivity1);
                    break;
                case R.id.tobuy_postit:
                    Intent memoActivity2 = new Intent(getApplicationContext(), MemoActivity.class);
                    startActivity(memoActivity2);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (plusBack.getVisibility() == View.VISIBLE) {
            plusBack.setVisibility(View.GONE);
            CameraLayout.setVisibility(View.GONE);
            PencilLayout.setVisibility(View.GONE);
            fabCamera.startAnimation(LayHide);
            fabPencil.startAnimation(LayHide);
            fabPlus.startAnimation(HidePlus);
            //fabMinus.setElevation(10);
        }
        if (minusBack.getVisibility() == View.VISIBLE) {
            minusBack.setVisibility(View.GONE);
            FullLayout.setVisibility(View.GONE);
            MiniLayout.setVisibility(View.GONE);
            fabFull.startAnimation(LayHide);
            fabMini.startAnimation(LayHide);
            fabMinus.startAnimation(HideMinus);
            //fabPlus.setElevation(10);
        } else
            menuPage.startAnimation(rightAnim);


    }

    public void MemoCreate() {
        Log.e("test", "MemoCreate() 들어옴");
        Mapper.updateUrgentMemo();

        urgentList = Mapper.scanUrgentMemo();
        tobuyList = Mapper.scanToBuyMemo();

        // 유통기한 : 최상위 3개만 메모에 보여지기

        ArrayList<DCItem> dnArray = new ArrayList<>();
        for (int i=0; i<urgentList.size(); i++) {
            dnArray.add(new DCItem(Integer.parseInt(urgentList.get(i).getIngredientDuedate()), urgentList.get(i).getIngredientName()));
        }

        Collections.sort(dnArray, new AscendingSort());

        String dueDateInfo="";
        CalculateDate(Integer.toString(dnArray.get(0).getDueDate()));
        if (diffDays < 0)
            dueDateInfo = dnArray.get(0).getName() + " +" + Math.abs(diffDays) + "일";
        else
            dueDateInfo = dnArray.get(0).getName() + " -" + Math.abs(diffDays) + "일";
        txtUrgent1.setText(dueDateInfo);

        CalculateDate(Integer.toString(dnArray.get(1).getDueDate()));
        if (diffDays < 0)
            dueDateInfo = dnArray.get(1).getName() + " +" + Math.abs(diffDays) + "일";
        else
            dueDateInfo = dnArray.get(1).getName() + " -" + Math.abs(diffDays) + "일";
        txtUrgent2.setText(dueDateInfo);

        CalculateDate(Integer.toString(dnArray.get(2).getDueDate()));
        if (diffDays < 0)
            dueDateInfo = dnArray.get(2).getName() + " +" + Math.abs(diffDays) + "일";
        else
            dueDateInfo = dnArray.get(2).getName() + " -" + Math.abs(diffDays) + "일";
        txtUrgent3.setText(dueDateInfo);

        // 장보기
        Log.e("test", "장보기 목록");
        for (int i=0; i<tobuyList.size(); i++)
            Log.d("test", tobuyList.get(i).getIngredientName() + ", " + tobuyList.get(i).getIngredientCount());

        String tobuyStr = "";
        long intCount = 0;
        for (int i=1; i<tobuyList.size()+1; i++) {
            intCount = Math.round(tobuyList.get(i-1).getIngredientCount());
            if (intCount > tobuyList.get(i-1).getIngredientCount())
                tobuyStr += tobuyList.get(i-1).getIngredientName() + "(" + tobuyList.get(i-1).getIngredientCount() + ")";
            else
                tobuyStr += tobuyList.get(i-1).getIngredientName() + "(" + intCount + ")";

            if (i%2==0)
                tobuyStr += "\n";
            else {
                if (i==tobuyList.size())
                    tobuyStr += "";
                else
                    tobuyStr += ", ";
            }
        }

        txtTobuy.setText(tobuyStr);
    }

    public void CalculateDate(String urgentFoodDate){
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        long diff;
        try {
            Date foodDate = formatter.parse(urgentFoodDate);
            Date curDay = formatter.parse(currentDate.getCurrenDate());
            diff = foodDate.getTime() - curDay.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);

        } catch (ParseException e){
            e.printStackTrace();
        }

    }
}

class AscendingSort implements Comparator<DCItem> {

    @Override
    public int compare(DCItem t1, DCItem t2) {
        return t1.getDueDate().compareTo(t2.getDueDate());
    }
}