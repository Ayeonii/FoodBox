package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecyclerAdapter;
import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorFrozenInsideActivity extends AppCompatActivity implements View.OnClickListener{

    FrameLayout frag;
    EditText frozenSearchBar;
    ImageButton deleteButton;

    private PencilRecyclerAdapter pencilRecyclerAdapter = new PencilRecyclerAdapter();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static ArrayList<LocalRefrigeratorItem> frozenListStatic = new ArrayList();
    private ArrayList<LocalRefrigeratorItem> frozenList = new ArrayList<>();
    private static List<RefrigeratorDO.Item> refriList = new ArrayList<>();

    public RefrigeratorFrozenInsideActivity(){}

    public List<LocalRefrigeratorItem> getFrozenList(){
        Log.e("","getFrozenList 들어옴 " );
        return frozenListStatic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frozen);

        pencilRecyclerAdapter.setIsRefri(true);
        frozenSearchBar = (EditText)findViewById(R.id.frozen_searchBar);
        deleteButton = (ImageButton) findViewById(R.id.delete_button2);
        frag = (FrameLayout)findViewById(R.id.frozen_fragment_container); //검색시 나오는 화면
        frag.setVisibility(View.GONE);


        recyclerView = (RecyclerView)findViewById(R.id.frozenRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        adapter = new PencilRecyclerAdapter(RefrigeratorFrozenInsideActivity.this,frozenList);
        recyclerView.setAdapter(adapter);
        Log.e("Frozen", "Frozen");

        frozenSearchBar.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        refriList = Mapper.scanRefri();

        /****************search bar input *****************************/
        frozenSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text;
                text = frozenSearchBar.getText().toString();
                if (text.length() == 0) {

                    frag.setVisibility(View.GONE);
                }
                else {

                    frag.setVisibility(View.VISIBLE);
                }
                SearchIngredientFragment.search(text, false,true,false, false);
            }
        });


        /****************delete button***********************/


        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frozen_searchBar:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Log.e("frozen ", "onclick은 들어오냐 ");
                SearchIngredientFragment SearchFragment = new SearchIngredientFragment();
                transaction.replace(R.id.frozen_fragment_container, SearchFragment);
                transaction.commit();
                break;
            case R.id.delete_button2:
                Log.e("frozen","deleteButton");
                if (frozenSearchBar.getText().length() != 0) {
                    frozenSearchBar.setHint(" 재료명을 입력하세요.");
                    frozenSearchBar.setText(null);
                }
                break;

        }
    }

    public void setData(){
        frozenListStatic.clear();
        for(int i =0 ; i<refriList.size(); i++){
            if(refriList.get(i).getIsFrozen() == true){
                String foodImg = "file:///storage/emulated/0/Download/"+refriList.get(i).getName()+".jpg";
                frozenList.add(new LocalRefrigeratorItem(refriList.get(i).getName()
                                                      ,refriList.get(i).getCount()
                                                      ,refriList.get(i).getDueDate()
                                                      ,Uri.parse(foodImg)
                                                      ,refriList.get(i).getSection()));
            }
            frozenListStatic = frozenList;
        }
        adapter.notifyDataSetChanged();
    }

}
