package com.example.dldke.foodbox;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class RefrigeratorMainActivity extends AppCompatActivity {


    private static final int LAYOUT = R.layout.activity_refrigerator;
    Animation ShowPlus, HidePlus,LayHide,ShowMinus,HideMinus, LayShow;
    private FloatingActionButton fabPlus, fabCamera, fabPencil, fabMinus, fabFull, fabMini;
    LinearLayout CameraLayout, PencilLayout, FullLayout, MiniLayout;

    //설정창에 들어갈 리스트
    static final String[] LIST_MENU = {"내 정보", "로그아웃", "설정"} ;

    //냉장고 오른쪽/왼쪽 부분
    Button leftDoor;
    Button rightDoor;

    //슬라이드 열기/닫기 플래그
    boolean isPageOpen = false;
    //슬라이드 열기/닫기 애니메이션
    Animation leftAnim, rightAnim;
    //슬라이드 레이아웃
    LinearLayout optionPage;
    //리스트뷰
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        leftDoor = (Button) findViewById(R.id.leftButton);
        leftDoor = (Button) findViewById(R.id.rightButton);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        listview = (ListView) findViewById(R.id.optionList) ;
        listview.setAdapter(adapter) ;




        optionPage = (LinearLayout)findViewById(R.id.optionPage);

        leftAnim = AnimationUtils.loadAnimation(this, R.anim.go_left);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.go_right);

        /*애니메이션 이벤트*/
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        leftAnim.setAnimationListener(animationListener);
        rightAnim.setAnimationListener(animationListener);




        ShowPlus= AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_button);
        HidePlus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_button);
        ShowMinus= AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_minus_button);
        HideMinus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_minus_button);
        LayShow = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_layout);
        LayHide = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_layout);

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

        /*플러스 플로팅 버튼*/
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CameraLayout.getVisibility() == View.VISIBLE && PencilLayout.getVisibility() == View.VISIBLE) {
                    CameraLayout.setVisibility(View.GONE);
                    PencilLayout.setVisibility(View.GONE);
                    fabCamera.startAnimation(LayHide);
                    fabPencil.startAnimation(LayHide);
                    fabPlus.startAnimation(HidePlus);
                } else {
                    CameraLayout.setVisibility(View.VISIBLE);
                    PencilLayout.setVisibility(View.VISIBLE);
                    fabCamera.startAnimation(LayShow);
                    fabPencil.startAnimation(LayShow);
                    fabPlus.startAnimation(ShowPlus);

                }
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent,4321);

            }
        });
        fabPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RefrigeratorMainActivity.this, "직접입력 누름", Toast.LENGTH_SHORT).show();
            }
        });

        /*마이너스 플로팅 버튼*/
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullLayout.getVisibility() == View.VISIBLE && MiniLayout.getVisibility() == View.VISIBLE) {
                    FullLayout.setVisibility(View.GONE);
                    MiniLayout.setVisibility(View.GONE);
                    fabFull.startAnimation(LayHide);
                    fabMini.startAnimation(LayHide);
                    fabMinus.startAnimation(HideMinus);
                } else {
                    FullLayout.setVisibility(View.VISIBLE);
                    MiniLayout.setVisibility(View.VISIBLE);
                    fabFull.startAnimation(LayShow);
                    fabMini.startAnimation(LayShow);
                    fabMinus.startAnimation(ShowMinus);
                }
            }
        });
        fabFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RefrigeratorMainActivity.this, "풀 레시피 누름", Toast.LENGTH_SHORT).show();
            }
        });
        fabMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RefrigeratorMainActivity.this, "간이 레시피 누름", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //닫기
                if(isPageOpen){
                    //애니메이션 시작

                    optionPage.startAnimation(rightAnim);

                }
                //열기
                else{
                    optionPage.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    optionPage.startAnimation(leftAnim);
                    if(!isPageOpen) {
                        leftDoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                optionPage.startAnimation(rightAnim);
                            }
                        });
                    }
                }



            }
        });


        final ImageView postit = (ImageView) findViewById(R.id.postit);
        postit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RefrigeratorMainActivity.this, "포스트잇 눌림", Toast.LENGTH_SHORT).show();

            }
        });


        //리스트뷰 리스너임
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                Toast.makeText(RefrigeratorMainActivity.this, strText+"눌렸어용", Toast.LENGTH_SHORT).show();
            }
        }) ;


    }


    //애니메이션 리스너
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            //슬라이드 열기->닫기
            if(isPageOpen){
                optionPage.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                isPageOpen = false;
            }
            //슬라이드 닫기->열기
            else{

                isPageOpen = true;
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        @Override
        public void onAnimationStart(Animation animation) {

        }
    }


}
