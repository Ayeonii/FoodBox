package com.example.dldke.foodbox.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.Adapter.FullRecipeAdapter;
import com.example.dldke.foodbox.FullRecipeDictionary;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class FullRecipeActivity extends AppCompatActivity  {

    private ArrayList<FullRecipeDictionary> mArrayList;
    private FullRecipeAdapter mAdapter;
    private RecyclerView mRecyclerView, mHorizontalView;
    private HorizontalAdapter hAdapter;
    private LinearLayoutManager mLayoutManager;

    private int MAX_ITEM_COUNT=10;

    private final String TAG="FullRecipe DB Test";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);

        //DB에 사용자 풀레시피 만들기

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();

        mAdapter = new FullRecipeAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        Button ingredient_btn = (Button)findViewById(R.id.button_main_insert);
        ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FullRecipeActivity.this);
                View view = LayoutInflater.from(FullRecipeActivity.this)
                        .inflate(R.layout.fullrecipe_edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_method);
                final EditText editTextEnglish = (EditText) view.findViewById(R.id.edittext_dialog_minute);
                final EditText editTextKorean = (EditText) view.findViewById(R.id.edittext_dialog_fire);

                ButtonSubmit.setText("삽입");


                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String strID = editTextID.getText().toString();
                        String strEnglish = editTextEnglish.getText().toString();
                        String strKorean = editTextKorean.getText().toString();

                        FullRecipeDictionary dict = new FullRecipeDictionary(strID, strEnglish, strKorean );

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        //mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mHorizontalView = (RecyclerView)findViewById(R.id.recyclerview_horizontal_list);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mHorizontalView.setLayoutManager(mLayoutManager);

        hAdapter = new HorizontalAdapter();

        ArrayList<HorizontalData> data = new ArrayList<>();

        int i = 0;
        while(i<MAX_ITEM_COUNT){
            data.add(new HorizontalData(R.drawable.food, i+"번째 데이터"));
            i++;
        }

        hAdapter.setData(data);
        mHorizontalView.setAdapter(hAdapter);

    }
}
class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder>{

    private ArrayList<HorizontalData> HorizontalDatas;

    public void setData(ArrayList<HorizontalData> list){
        HorizontalDatas = list;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 사용할 아이템의 뷰를 생성해준다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fullrecipe_ingredient_list, parent, false);

        HorizontalViewHolder holder = new HorizontalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder holder, int position) {
        HorizontalData data = HorizontalDatas.get(position);

        holder.description.setText(data.getText());
        holder.icon.setImageResource(data.getImg());

    }

    @Override
    public int getItemCount() {
        return HorizontalDatas.size();
    }
}

class HorizontalViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public TextView description;

    public HorizontalViewHolder(View itemView) {
        super(itemView);

        icon = (ImageView) itemView.findViewById(R.id.horizon_icon);
        description = (TextView) itemView.findViewById(R.id.horizon_description);

    }
}

class HorizontalData {

    private int img;
    private String text;

    public HorizontalData(int img, String text){
        this.img = img;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public int getImg() {
        return this.img;
    }
}
