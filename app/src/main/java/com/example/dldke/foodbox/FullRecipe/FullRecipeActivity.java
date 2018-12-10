package com.example.dldke.foodbox.FullRecipe;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.CloudVision.PermissionUtils;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;

import static com.example.dldke.foodbox.CloudVision.VisionActivity.CAMERA_IMAGE_REQUEST;
import static com.example.dldke.foodbox.CloudVision.VisionActivity.CAMERA_PERMISSIONS_REQUEST;
import static com.example.dldke.foodbox.CloudVision.VisionActivity.FILE_NAME;
import static com.example.dldke.foodbox.DataBaseFiles.Mapper.createIngredient;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxHalfRecipeDetailActivity;
import com.example.dldke.foodbox.PencilRecipe.PencilCartAdapter;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecipeActivity;
import com.example.dldke.foodbox.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FullRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private final int CAMERA_CODE = 1;
    private final int GALLERY_CODE = 2;
    private static final int MAX_DIMENSION = 1200;

    private static boolean isCookingClass, isHalfRecipe;

    private String imagePath, recipeId;
    private static String FoodTitle;
    private Toolbar toolbar;
    private EditText foodtitle;
    private Spinner spinner;
    private Button ingredient_add, spec_add, ok_btn;
    private ImageView food_img_real;
    private RecyclerView fullrecipeRecyclerView, recipe_ingredient_view;

    private List<RecipeDO.Ingredient> data = new ArrayList<>();
    static ArrayList<FullRecipeData> mArrayList;
    static FullRecipeAdapter mAdapter;
    private FullRecipeIngredientAdapter recipeIngredientAdapter;
    private PencilCartAdapter pencilCartAdapter = new PencilCartAdapter();
    private ArrayList<PencilCartItem> clickItems;

    private RecipeBoxHalfRecipeDetailActivity recipeBoxHalfRecipeDetailActivity = new RecipeBoxHalfRecipeDetailActivity();
    private PencilRecipeActivity pencilRecipeActivity = new PencilRecipeActivity();
    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private FullRecipeStepDialog stepDialog;

    public FullRecipeActivity(){}

    public void setIsHalfRecipe(boolean isHalfRecipe){
        this.isHalfRecipe = isHalfRecipe;
    }
    public boolean getIsHalfRecipe(){
        return isHalfRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);
        isCookingClass = refrigeratorMainActivity.getisCookingClass();

        toolbar = (Toolbar) findViewById(R.id.fullrecipe_toolbar);
        foodtitle = (EditText) findViewById(R.id.food_title);
        food_img_real = (ImageView)findViewById(R.id.food_img_real);
        ingredient_add = (Button)findViewById(R.id.ingredient_add);
        spec_add = (Button) findViewById(R.id.spec_insert_btn);
        ok_btn = (Button) findViewById(R.id.recipe_ok);
        spinner = (Spinner) findViewById(R.id.food_spinner);
        recipe_ingredient_view = (RecyclerView) findViewById(R.id.recipe_ingredient_recyclerview);
        fullrecipeRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성


        if(isCookingClass && !isHalfRecipe){
            //쿠킹 클래스 풀레시피 작성
            ingredient_add.setVisibility(View.VISIBLE);
            clickItems = pencilCartAdapter.getCartItems();
            try{
                for(int i=0; i<clickItems.size(); i++){
                    data.add(createIngredient(clickItems.get(i).getFoodName(), clickItems.get(i).getFoodCount()));
                }
            }catch(NullPointerException e){}

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recipe_ingredient_view.setLayoutManager(mLayoutManager);
            recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
            recipe_ingredient_view.setAdapter(recipeIngredientAdapter);

        }
        else{
            //간이 레시피에서 풀레시피로 작성
            recipeId = recipeBoxHalfRecipeDetailActivity.getRecipeId();
            FoodTitle = Mapper.searchRecipe(recipeId).getSimpleName();
            foodtitle.setText(FoodTitle);
            ingredient_add.setVisibility(View.INVISIBLE);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recipe_ingredient_view.setLayoutManager(mLayoutManager);
            data = Mapper.searchRecipe(recipeId).getIngredient();
            recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
            recipe_ingredient_view.setAdapter(recipeIngredientAdapter);
        }


        /*
        요리 카테고리 만드는 spinner
         */
        String[] foodrecipe_list = new String[]{"메인요리", "국/찌개", "반찬", "간식"};
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, foodrecipe_list);
        spinner.setAdapter(spinnerAdapter);


        /*
        FullRecipe SpecList Init
        */
        fullrecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new FullRecipeAdapter(this, mArrayList);
        fullrecipeRecyclerView.setAdapter(mAdapter);


        food_img_real.setOnClickListener(this);
        ingredient_add.setOnClickListener(this);
        spec_add.setOnClickListener(this);
        ok_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_img_real:
                imageDialog();
                break;

            case R.id.spec_insert_btn:
                if(isCookingClass && !isHalfRecipe){
                    //stepDialog = new FullRecipeStepDialog(this, clickItems);
                    stepDialog = new FullRecipeStepDialog(this, data);
                    stepDialog.show();
                }
                else{
                    stepDialog = new FullRecipeStepDialog(this, recipeId);
                    stepDialog.show();
                }
                break;

            case R.id.recipe_ok:
                registerSpec();
                break;

            case R.id.ingredient_add:
                pencilRecipeActivity.setIsFull(true);
                Intent PencilRecipeActivity = new Intent(getApplicationContext(), PencilRecipeActivity.class);
                startActivity(PencilRecipeActivity);
                break;
            default:
                break;
        }

    }

    public void imageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 업로드");
        builder.setMessage("업로드 방법을 선택하세요");
        builder.setPositiveButton("album", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectGallery();
            }
        });
        builder.setNegativeButton("camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectPhoto();
            }
        });
        builder.show();
    }

    public void registerSpec(){
        List<RecipeDO.Spec> specList;

        FoodTitle= foodtitle.getText().toString();
        specList = stepDialog.getSpecList();


        if(isCookingClass && !isHalfRecipe){
            recipeId = Mapper.createChefRecipe(FoodTitle, specList);
            Mapper.addRecipeInMyCommunity(recipeId);
            Mapper.updateIngredient(stepDialog.getIngredients(), recipeId);
        }
        else{
            Mapper.createFullRecipe(recipeId, FoodTitle, specList);
        }

        Mapper.attachRecipeImage(recipeId, imagePath);
        Mapper.updatePointInfo(10);

        Intent RefrigeratorActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
        startActivity(RefrigeratorActivity);
        specList.clear();
    }

    public void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void selectPhoto(){
        if (PermissionUtils.requestPermission(this, CAMERA_CODE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_CODE);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData()).start(this);

        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            CropImage.activity(photoUri).start(this);
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                String real_path = uri.getPath();
                imagePath = real_path;
                Bitmap bitmap = scaleBitmapDown( MediaStore.Images.Media.getBitmap(getContentResolver(), uri), MAX_DIMENSION);
                food_img_real.setImageBitmap(bitmap);

            } catch (IOException e) {
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_CODE:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_CODE, grantResults)) {
                    selectPhoto();
                }
                break;
            case GALLERY_CODE:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_CODE, grantResults)) {
                    selectGallery();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.Activity.RefrigeratorMainActivity.class);
            RefrigeratorMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(RefrigeratorMainActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}
