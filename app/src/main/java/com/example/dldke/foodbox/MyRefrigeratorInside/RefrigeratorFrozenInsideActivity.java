package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecyclerAdapter;
import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorFrozenInsideActivity extends AppCompatActivity {

    private FrameLayout frag;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<PencilItem> frozenlist = new ArrayList<>();
    private static List<RefrigeratorDO.Item> refriList = new ArrayList<>();
    private EditText searchBar;
    private ImageButton deleteButton;

    public RefrigeratorFrozenInsideActivity(){}

    public List<RefrigeratorDO.Item> getRefriList(){
        return refriList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frozen);

        searchBar = (EditText)findViewById(R.id.frozen_searchBar);
        deleteButton = (ImageButton) findViewById(R.id.delete_button2);
        frag = (FrameLayout)findViewById(R.id.frozen_fragment_container); //검색시 나오는 화면
        frag.setVisibility(View.GONE);


        recyclerView = (RecyclerView)findViewById(R.id.frozenRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        adapter = new RefrigeratorFrozenAdapter(frozenlist, getApplicationContext());
        recyclerView.setAdapter(adapter);
        Log.e("Frozen", "Frozen");

        refriList = Mapper.scanRefri();




        /****************search bar input *****************************/
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text;
                text = searchBar.getText().toString();
                if (text.length() == 0)
                    frag.setVisibility(View.GONE);
                else
                    frag.setVisibility(View.VISIBLE);
                SearchIngredientFragment.search(text, false,true,false, false);
            }
        });


        /****************delete button***********************/

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //텍스트가 존재 시, 모두 지운다.
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 재료명을 입력하세요.");
                    searchBar.setText(null);
                }
            }
        });

        setData();
    }

    public void setData(){
        for(int i =0 ; i<refriList.size(); i++){
            if(refriList.get(i).getIsFrozen() == true){
                String foodImg = "file:///storage/emulated/0/Download/"+refriList.get(i).getName()+".jpg";
                frozenlist.add(new PencilItem(refriList.get(i).getName(), Uri.parse(foodImg), refriList.get(i).getSection()));
            }
        }
        adapter.notifyDataSetChanged();
    }

}
