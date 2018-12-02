package com.example.dldke.foodbox.HalfRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.MyRecipe.MyRecipeBoxActivity;
import com.example.dldke.foodbox.R;

import java.io.File;
import java.util.ArrayList;

public class HalfRecipeIngActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOk;
    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private ArrayList<HalfRecipeRecipeItem> mItems = new ArrayList<>();
    private ArrayList<HalfRecipeRecipeItem> needArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_recipe_complete);

        btnOk = (Button) findViewById(R.id.btn_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnOk.setOnClickListener(this);

//        Intent intent = getIntent();
//        needArray = (ArrayList<HalfRecipeRecipeItem>) getIntent().getSerializableExtra("need");

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new HalfRecipeRecipeAdapter(mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < needArray.size(); i++) {
            String foodImgUri ;
            File file = new File("/storage/emulated/0/Download/" + needArray.get(i).getName() + ".jpg");
            if(!file.exists())
                foodImgUri = "file:///storage/emulated/0/Download/default.jpg";
            else
                foodImgUri = "file:///storage/emulated/0/Download/"+needArray.get(i).getName()+".jpg";

            mItems.add(new HalfRecipeRecipeItem(1, needArray.get(i).getName(), needArray.get(i).getNeedCount(), Uri.parse(foodImgUri)));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:   // 확인을 누르면 레시피함으로 간다.
                Intent myRecipeActivity = new Intent(getApplicationContext(), MyRecipeBoxActivity.class);
                myRecipeActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myRecipeActivity);
                break;
        }
    }
}