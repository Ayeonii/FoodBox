package com.example.dldke.foodbox.Community;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.PostDO;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxFullRecipeDetailAdapter;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxFullRecipeDetailItem;
import com.example.dldke.foodbox.PencilRecipe.CurrentDate;
import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommunityDetailActivity extends AppCompatActivity {
    private CommunityLoadingAdapter communityLoadingAdapter = new CommunityLoadingAdapter();
    private String recipe_id, post_id;
    private RecipeDO.Detail detail;
    private RecyclerView detail_recyclerview;
    private RecyclerView ingre_recyclerview;
    private RecyclerView comment_recyclerview;
    private RecyclerView.Adapter detail_adapter;
    private RecyclerView.Adapter ingre_adapter;
    private RecyclerView.Adapter comment_adapter;

    private List<RecipeDO.Ingredient> ingreList = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    List<RecipeDO.Spec> specList;
    List<RecipeDO.Ingredient> specIngredientList;


    private String searchText;
    private EditText commentBar;
    private ImageView okBtn;
    private ArrayList<CommunityCommentItem> detailList = new ArrayList<>();
    private List<PostDO.Comment> commentDBList = new ArrayList<>();
    private static boolean isComment = false;
    private static String comment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        searchText = "";
        commentBar = (EditText)findViewById(R.id.community_commentBar);
        okBtn = (ImageView)findViewById(R.id.community_ok_btn);
        Toolbar toolbar = (Toolbar)findViewById(R.id.community_detail_toolbar);
        ImageView mainImg = (ImageView)findViewById(R.id.community_detail_foodimg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipe_id = communityLoadingAdapter.getClickedRecipeId();
        detail = Mapper.searchRecipe(recipe_id).getDetail();
        post_id = communityLoadingAdapter.getClickedPostId();
        specList = detail.getSpecList();

        String imgUrl = Mapper.getImageUrlRecipe(recipe_id);
        new GetImage(mainImg).execute(imgUrl);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.community_detail_collasping_toolbar);
        String foodName = detail.getFoodName();
        collapsingToolbarLayout.setTitle(foodName);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        detail_recyclerview = (RecyclerView)findViewById(R.id.community_detail_view);
        detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        detail_adapter = new CommunityCommentAdapter(detailList, getApplicationContext());
        detail_recyclerview.setAdapter(detail_adapter);

        ingreList = Mapper.searchRecipe(recipe_id).getIngredient();
        ingre_recyclerview = (RecyclerView)findViewById(R.id.community_ingre_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ingre_recyclerview.setLayoutManager(mLayoutManager);

        for(int i =0 ; i<ingreList.size(); i++) {
            list.add(ingreList.get(i).getIngredientName());
        }
        ingre_adapter = new CommunityIngreAdapter(list,getApplicationContext());
        ingre_recyclerview.setAdapter(ingre_adapter);

        try{
        commentDBList = Mapper.searchPost("postId", post_id).get(0).getCommentList();
        }catch (NullPointerException e){
        }

       AddStep(specList);

        /****************search bar input *****************************/

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentBar.getWindowToken(), 0);
                comment = commentBar.getText().toString();
                //텍스트가 존재 시, 댓글 등록
                if (commentBar.getText().length() != 0) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd, hh:mm a", Locale.KOREA);
                    String date = df.format(new Date());
                    Mapper.createComment(post_id, comment);
                    commentBar.setText("");
                    commentBar.setHint(" 댓글을 입력하세요");
                    detailList.add(new CommunityCommentItem(Mapper.getUserId()
                                ,R.drawable.ic_person
                                ,comment
                                ,date
                                ,0
                                ,null
                                ,CommunityCommentItem.ItemType.ONE_ITEM));
                }
                detail_adapter.notifyDataSetChanged();

            }
        });

    }
    public void AddStep(List<RecipeDO.Spec> specList){
        for(int i = 0; i<specList.size(); i++){
            String result ="";
            specIngredientList = specList.get(i).getSpecIngredient();
            for(int j = 0; j<specIngredientList.size(); j++){
                String specingredientName = specIngredientList.get(j).getIngredientName();
                result = result.concat(specingredientName);
            }
            int number = i+1;
            String descrip = number+". "+result+" 을/를 "+specList.get(i).getSpecMinute()+"분 동안 "+specList.get(i).getSpecMethod()+".\r\n"+"불 세기는 "+specList.get(i).getSpecFire();
            detailList.add(new CommunityCommentItem(null
                                                    ,0
                                                    ,null
                                                    ,null
                                                    ,R.drawable.strawberry
                                                    ,descrip
                                                    ,CommunityCommentItem.ItemType.TWO_ITEM));

            specIngredientList.clear();
        }
        setCommentData();
    }

    //이미지 가져오기 비동기
    private class GetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlImg =urls[0];
            Bitmap foodImg = null;
            try {
                InputStream in = new java.net.URL(urlImg).openStream();
                foodImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodImg;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void setCommentData(){

        for(int i =0; i <commentDBList.size(); i ++){
            detailList.add(new CommunityCommentItem(commentDBList.get(i).getUserId()
                                                    ,R.drawable.ic_person
                                                    ,commentDBList.get(i).getContent()
                                                    ,commentDBList.get(i).getDate()
                                                    ,R.drawable.strawberry
                                                    ,null
                                                    ,CommunityCommentItem.ItemType.ONE_ITEM));
        }

        detail_adapter.notifyDataSetChanged();
    }
}
