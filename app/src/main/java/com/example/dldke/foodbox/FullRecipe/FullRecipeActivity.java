package com.example.dldke.foodbox.FullRecipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxHalfRecipeDetailActivity;
import com.example.dldke.foodbox.PencilRecipe.PencilRecipeActivity;
import com.example.dldke.foodbox.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FullRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    //데이터 유지가 필요한 변수 foodtitle, mArrayList, foodimg, spinner

    static EditText foodtitle;
    static Spinner spinner;

    static ArrayList<FullRecipeData> mArrayList;
    String testStr = "";

    private static FullRecipeAdapter mAdapter;
    private static RecyclerView fullrecipeRecyclerView, recipe_ingredient_view;
    private FullRecipeIngredientAdapter recipeIngredientAdapter;

    private static List<RecipeDO.Ingredient> specIngredientList = new ArrayList<>();
    private static List<RecipeDO.Spec> specList = new ArrayList<>();

    //간이레시피함에서 선택된 레시피 아이디 받아오기
    private RecipeBoxHalfRecipeDetailActivity recipeBoxHalfRecipeDetailActivity = new RecipeBoxHalfRecipeDetailActivity();
    private String recipeId = recipeBoxHalfRecipeDetailActivity.getRecipeId();
    private List<RecipeDO.Ingredient> data;
    //private String recipeId;

    //전체요리 이미지, 단계별 이미지 가져오기
    private final int CAMERA_CODE = 1;
    private final int GALLERY_CODE = 2;
    static ImageView food_img;
    static ImageView food_img_real;
    private String imagePath;

    private final String TAG = "FullRecipe DB Test";

    private static List<String> specDescription = new ArrayList<>();

    private PencilRecipeActivity pencilRecipeActivity = new PencilRecipeActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);

        foodtitle = (EditText) findViewById(R.id.food_title);
        String title = Mapper.searchRecipe(recipeId).getSimpleName();
        foodtitle.setText(title);

        /*
            갤러리에서 이미지 가져오기
         */
        food_img = (ImageView)findViewById(R.id.food_img);
        food_img_real = (ImageView)findViewById(R.id.food_img_real);
        food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGallery();
            }
        });

        /*
        요리 카테고리 만드는 spinner
         */
        spinner = (Spinner) findViewById(R.id.food_spinner);
        String[] foodrecipe_list = new String[]{"메인요리", "국/찌개", "반찬", "간식"};
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, foodrecipe_list);
        spinner.setAdapter(spinnerAdapter);

        final Button ingredient_add = (Button)findViewById(R.id.ingredient_add);
        ingredient_add.setOnClickListener(this);
        Button get_recipe = (Button)findViewById(R.id.get_recipe);
        get_recipe.setOnClickListener(this);

        /*
            풀레시피 단계 list 보여주기
         */
        fullrecipeRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        fullrecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new FullRecipeAdapter(this, mArrayList);
        fullrecipeRecyclerView.setAdapter(mAdapter);



        /*
        선택된 재료 보여주기
         */
        recipe_ingredient_view = (RecyclerView) findViewById(R.id.recipe_ingredient_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recipe_ingredient_view.setLayoutManager(mLayoutManager);

        data = Mapper.searchRecipe(recipeId).getIngredient();
        recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
        recipe_ingredient_view.setAdapter(recipeIngredientAdapter);



        /*
        풀레시피 detail 작성 팝업창
         */
        Button ingredient_btn = (Button) findViewById(R.id.button_main_insert);
        ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //레시피 다이얼로그 build
                AlertDialog.Builder builder = new AlertDialog.Builder(FullRecipeActivity.this);
                builder.setTitle("레시피를 작성하세요");

                final List<String> specItems = new ArrayList<>();
                final List<String> tempItems = new ArrayList<>();
                //다이얼로그 안에 체크박스 만들기
                for(int i = 0; i<data.size(); i++){
                    specItems.add(data.get(i).getIngredientName());
                }
                final CharSequence[] items =  specItems.toArray(new String[specItems.size()]);  //체크박스 만들기
                final List SelectedItems  = new ArrayList();

                builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        //선택된 재료 저장
                        if(isChecked) {
                            SelectedItems.add(i);
                            tempItems.add(specItems.get(i));//레시피 단계 작성후에 text로 보여지는 재료 이걸로 쓰자

                            Log.e(TAG, "재료 이름 가져와 "+tempItems);
                        }
                        else{
                            tempItems.remove(specItems.get(i));
                        }
                    }
                });

                //다이얼로그 안에 스피너 만들기
                View view = LayoutInflater.from(FullRecipeActivity.this).inflate(R.layout.fullrecipe_step_popup, null, false);
                builder.setView(view);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.done_btn);
                final Button ButtonCancel = (Button) view.findViewById(R.id.cancel_btn);
                final Spinner method_sp = (Spinner) view.findViewById(R.id.method_spinner);
                final Spinner minute_sp = (Spinner) view.findViewById(R.id.minute_spinner);
                final Spinner fire_sp = (Spinner) view.findViewById(R.id.fire_spinner);


                //방법, 시간, 불세기 spinner
                String[] methodStr = getResources().getStringArray(R.array.MethodSpinner);
                ArrayAdapter<String> madapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, methodStr);
                method_sp.setAdapter(madapter);

                String[] minuteStr = getResources().getStringArray(R.array.MinuteSpinner);
                ArrayAdapter<String> minadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, minuteStr);
                minute_sp.setAdapter(minadapter);

                String[] fireStr = getResources().getStringArray(R.array.FireSpinner);
                ArrayAdapter<String> fireadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fireStr);
                fire_sp.setAdapter(fireadapter);


                //FullRecipe Detail 작성 후, 화면과 데이터베이스에 삽입
                ButtonSubmit.setText("삽입");
                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        String step_descriptoin="";
                        List<RecipeDO.Ingredient> ingredientsSize = Mapper.searchRecipe(recipeId).getIngredient();

                        for(int i = 0; i< tempItems.size(); i++){
                            for(int j =0 ; j<ingredientsSize.size(); j++){
                                if(tempItems.get(i).equals(ingredientsSize.get(j).getIngredientName())){
                                    specIngredientList.add(ingredientsSize.get(j));
                                }
                            }
                            Log.e(TAG, ""+Mapper.searchRecipe(recipeId).getIngredient().get(i).getIngredientName());
                        }

                        String method = method_sp.getSelectedItem().toString();
                        String minute = minute_sp.getSelectedItem().toString();
                        Integer minuteInt = Integer.parseInt(minute);
                        String fire = fire_sp.getSelectedItem().toString();

                        //FullRecipeData dict = new FullRecipeData(method, minute, fire);
                        String clickedIngre ;

                        if(tempItems.size() >1){
                            clickedIngre = tempItems.get(0);
                            for(int i =1 ; i< tempItems.size() ;i ++){
                             clickedIngre = clickedIngre+", "+tempItems.get(i);
                            }
                        }else{
                            clickedIngre = tempItems.get(0);
                        }

                        if(minute.equals("0") || fire.equals("없음")){
                            step_descriptoin = clickedIngre+" 을/를 \r\n"+method;
                        }else{
                            step_descriptoin = clickedIngre+" 을/를 \r\n"+minute+" 분 동안 \r\n"+method+" (불 세기: "+fire+" )";
                        }
                        FullRecipeData dict = new FullRecipeData(step_descriptoin);

                        //mArrayList.add(0, dict); //첫 줄에 삽입
                        mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        //FullRecipe step DB 저장
                        RecipeDO.Spec spec = Mapper.createSpec(specIngredientList, method, fire, minuteInt);
                        specList.add(spec);
                        Log.d(TAG, ""+step_descriptoin);

                        specIngredientList.clear();
                        testStr = "";
                        dialog.dismiss();
                    }
                });


                ButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });

        /*
        풀레시피 작성 완료 시, 데이터에 저장하기
         */
        Button ok_btn = (Button) findViewById(R.id.recipe_ok);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FoodTitle = foodtitle.getText().toString();
                Mapper.createFullRecipe(recipeId, FoodTitle, specList);
                Mapper.attachRecipeImage(recipeId, imagePath);
                //내 커뮤니티에 풀레시피 등록
                //Mapper.addRecipeInMyCommunity(recipeId);
                Log.e(TAG, "등록된 레시피 이름 : "+FoodTitle+" 등록된 레시피 아이디 : "+recipeId);
                Intent RefrigeratorActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                startActivity(RefrigeratorActivity);
            }
        });
        specList.clear();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingredient_add:
                pencilRecipeActivity.setIsFull(true);
                Intent PencilRecipeActivity = new Intent(getApplicationContext(), PencilRecipeActivity.class);
                startActivity(PencilRecipeActivity);

            case R.id.get_recipe:
                Intent MyRecipeActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                startActivity(MyRecipeActivity);
            default:
                break;
        }

    }

    public void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch(requestCode){
                case GALLERY_CODE:
                    sendPicture(data.getData());
                    break;
                default:
                    break;
            }
        }
    }

    private void sendPicture(Uri imgUri){
        imagePath = getRealPathFromURI(imgUri);
        ExifInterface exif = null;;
        try{
            exif = new ExifInterface(imagePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        //int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        //int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //food_img.setImageBitmap(rotate(bitmap, exifDegree));
        food_img.setVisibility(View.INVISIBLE);
        food_img_real.setVisibility(View.VISIBLE);
        food_img_real.setImageBitmap(bitmap);

    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

}
