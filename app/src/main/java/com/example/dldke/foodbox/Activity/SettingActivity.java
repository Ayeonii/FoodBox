package com.example.dldke.foodbox.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.CloudVision.PermissionUtils;
import com.example.dldke.foodbox.Community.CommunityFragmentNewsfeed;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static String user_nickname, business_number;
    boolean isCook = false;

    private String imagePath;
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final String FILE_NAME = "temp.jpg";
    private static final int MAX_DIMENSION = 1200;

    private Toolbar toolbar;
    private Button setting_ok;
    private LinearLayout business_license_number, point_layout;
    private TextView business_N1, business_N2, business_N3;
    private TextView nickname, setting_title;
    private String TAG = "SettingActivity";
    private CircleImageView profile;
    private static boolean profileChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setting_title = (TextView) findViewById(R.id.setting_title);
        TextView user_id = (TextView) findViewById(R.id.user_name);
        TextView point = (TextView) findViewById(R.id.point);
        Switch cooking_class = (Switch) findViewById(R.id.cooking_class_btn);
        setting_ok = (Button) findViewById(R.id.setting_ok_btn);
        profile = (CircleImageView) findViewById(R.id.user_profile);
        point_layout = (LinearLayout) findViewById(R.id.point_linear);
        business_license_number = (LinearLayout) findViewById(R.id.business_linear);
        business_N1 = (TextView) findViewById(R.id.business_number1);
        business_N2 = (TextView) findViewById(R.id.business_number2);
        business_N3 = (TextView) findViewById(R.id.business_number3);
        nickname = (TextView) findViewById(R.id.nickname);
        user_id.setText(Mapper.getUserId());
        String userId = user_id.getText().toString();

        try {
            String imgUrl = Mapper.getImageUrlUser(userId);
            if(imgUrl.equals("default")){
                profile.setImageDrawable(getApplication().getResources().getDrawable(R.drawable.ic_person, null));
            }
            else{
                new DownloadImageTask(profile).execute(imgUrl);
            }

        } catch (Exception e){

        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성


        user_id.setText(Mapper.getUserId());
        point.setText(String.valueOf(Mapper.searchUserInfo().getPoint()));
        isCook = Mapper.searchUserInfo().getIsCookingClass();

        String nicknameStr = Mapper.searchUserInfo().getNickname();

        if(nicknameStr != null){
            nickname.setText(nicknameStr);
        }

        if(isCook){
            cooking_class.setChecked(true);
            business_license_number.setVisibility(View.VISIBLE);
            try{
                String temp = Mapper.searchUserInfo().getRegisterNumber();
                business_N1.setText(temp.substring(0,3));
                business_N2.setText(temp.substring(3, 5));
                business_N3.setText(temp.substring(5, 8));
            }
            catch (NullPointerException e){ }

        }
        else{
            cooking_class.setChecked(false);
        }

        setting_ok.setOnClickListener(this);
        cooking_class.setOnClickListener(this);
        nickname.setOnClickListener(this);
        business_license_number.setOnClickListener(this);
        profile.setOnClickListener(this);

    }

    public  class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView profile;
        public DownloadImageTask(CircleImageView profile){this.profile = profile;}

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
        protected void onPostExecute(Bitmap result){
            profile.setImageBitmap(result);
        }
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.setting_ok_btn:
                user_nickname = nickname.getText().toString();
                business_number = business_N1.getText().toString() + business_N2.getText().toString() + business_N3.getText().toString();
                imagePath = Mapper.getImageUrlUser(Mapper.getUserId());

                /******** 고치기 *********/
                if(imagePath.equals("default")){
                    imagePath = "file:///storage/emulated/0/Download/default.jpg";
                }

                if(user_nickname.equals("") || user_nickname.equals("닉네임이 없습니다.") || business_number == null) {
                    Toast.makeText(getApplicationContext(), "입력란을 채워주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                else{
                    Mapper.updateUserInfo(user_nickname, isCook, business_number);
                    if(profileChange){
                        Mapper.uploadUserImage(imagePath);
                    }

                    Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
                    startActivity(RefrigeratorMainActivity);
                    break;
                }

            case R.id.cooking_class_btn:
                business_license_number.setVisibility(View.VISIBLE);
                isCook = true;

            case R.id.nickname:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText et = new EditText(SettingActivity.this);
                builder.setTitle("닉네임 추가");
                builder.setMessage("닉네임을 입력해 주세요");
                builder.setView(et);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        user_nickname = et.getText().toString();
                        nickname.setText(user_nickname);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

            case R.id.business_linear:
                AlertDialog.Builder business_builder = new AlertDialog.Builder(this);
                final EditText business_et = new EditText(SettingActivity.this);
                business_et.setText("000-00-000");
                business_builder.setTitle("사업자 번호 추가");
                business_builder.setMessage("사업자 번호를 입력해주세요");
                business_builder.setView(business_et);
                business_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String temp = business_et.getText().toString();
                        if(temp.equals("")){
                            Toast.makeText(getApplicationContext(), "사업자 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            business_N1.setText(temp.substring(0,3));
                            business_N2.setText(temp.substring(4,6));
                            business_N3.setText(temp.substring(7,10));
                            dialog.dismiss();
                        }
                    }
                });
                business_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                business_builder.create().show();
                break;

            case R.id.user_profile:
                AlertDialog.Builder profile_builder = new AlertDialog.Builder(this);
                profile_builder.setMessage("사진을 등록하세요")
                        .setPositiveButton("갤러리", (dialog, i) -> startGalleryChooser())
                        .setNegativeButton("카메라", (dialogInterface, i) -> startCamera());
                profile_builder.create().show();
            default: break;
        }
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"), GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData()).start(this);

        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            CropImage.activity(photoUri).start(this);
        }
    }

    public void uploadImage(Uri uri) {
        profileChange = true;
        if (uri != null) {
            try {
                String real_path = uri.getPath();
                imagePath = real_path;
                Bitmap bitmap = scaleBitmapDown( MediaStore.Images.Media.getBitmap(getContentResolver(), uri), MAX_DIMENSION);
                profile.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
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
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults))
                    startCamera();
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults))
                    startGalleryChooser();
                break;
        }
    }

    public void ThemeSetting(){
        String theme = Mapper.searchUserInfo().getTheme();

        if(theme == "블랙"){
            toolbar.setBackgroundColor(Color.BLACK);
            setting_title.setTextColor(Color.WHITE);
            setting_ok.setBackgroundColor(Color.BLACK);
            setting_ok.setTextColor(Color.WHITE);
        }
        else if(theme == "베이지"){

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
