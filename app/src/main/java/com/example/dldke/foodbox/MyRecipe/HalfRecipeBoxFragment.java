package com.example.dldke.foodbox.MyRecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.MyRecipe.RecipeBoxAdapter;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxData;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeBoxFragment extends Fragment {

    public HalfRecipeBoxFragment(){}

    private RecyclerView recyclerview;
    private RecipeBoxAdapter adapter;
    private ArrayList<RecipeBoxData> data = new ArrayList<>();

    private String TAG = "HalfRecipeBox";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_half_recipe_box, container, false);

        recyclerview = (RecyclerView)view.findViewById(R.id.recycler_view2);
        recyclerview.setHasFixedSize(true);
        adapter = new RecipeBoxAdapter(data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);

        return view;
    }

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prepareData();
    }

    //Detail이 없으면 간이레시피, Detail이 있으면 풀레시피 fragment로 보여지기 위한 작업
    private void prepareData(){

        List<String> myrecipe = Mapper.searchMyCommunity().getMyRecipes();
        for(int i =0 ; i<myrecipe.size(); i++){
           try{

                String foodname = Mapper.searchRecipe(myrecipe.get(i)).getDetail().getFoodName();

            }catch(NullPointerException e){

               Log.d(TAG,"음식이름이 없습니다." );
               Log.e(TAG, ""+myrecipe.get(i));
               data.add(new RecipeBoxData(myrecipe.get(i), R.drawable.strawberry, myrecipe.get(i)));
            }
        }
    }


}
