package com.example.dldke.foodbox.MyRecipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeBoxFragment extends Fragment {

    public HalfRecipeBoxFragment() {  }

    private RecyclerView recyclerview;
    private MyRecipeBoxHalfRecipeAdapter adapter;
    private ArrayList<RecipeBoxData> data = new ArrayList<>();
    private boolean isRecipe = false;
    private static int isIng;
    private static boolean isPost;
    private static boolean isDetailBack;
    private static View view;
    private static List<String> myrecipeId = new ArrayList<>();

    public void setisDetailBack(boolean isDetailBack){
        this.isDetailBack = isDetailBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isRecipe) {
            view = inflater.inflate(R.layout.recipe_box_fragment_halfrecipe, container, false);
            recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view2);
            recyclerview.setHasFixedSize(true);
            adapter = new MyRecipeBoxHalfRecipeAdapter(data);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerview.setLayoutManager(layoutManager);
            //recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adapter);


            return view;
        }
        else {
            view = inflater.inflate(R.layout.recipe_box_fragment_none, container, false);
            return view;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        if(isPost) {
            enableSwipeToDeleteAndUndo();
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                data.remove(position);
                adapter.notifyDataSetChanged();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerview);
    }

    @Override
    public void onStart(){
       if(isDetailBack){
           data.clear();
            prepareData();
            isDetailBack = false;

            recyclerview = (RecyclerView) view.findViewById(R.id.recycler_view2);
            recyclerview.setHasFixedSize(true);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyRecipeBoxHalfRecipeAdapter(data);
            //recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adapter);
        }
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void prepareData() {

        List<String> myrecipe = Mapper.searchMyCommunity().getMyRecipes();

        for (int i = 0; i < myrecipe.size(); i++) {
            String recipeId = myrecipe.get(i);
            try {

                String foodname = Mapper.searchRecipe(recipeId).getDetail().getFoodName();
                isPost = Mapper.searchRecipe(recipeId).getIsPost();


                if (isPost) {

                    String simpleName = Mapper.searchRecipe(recipeId).getSimpleName();
                    Mapper.updateIngInfo(0, recipeId);
                    data.add(new RecipeBoxData(simpleName, recipeId, 0, isPost));
                    isRecipe = true;
                }

            } catch (NullPointerException e) {
                recipeId = myrecipe.get(i);
                Log.e("recipeID",recipeId);
                String simpleName = Mapper.searchRecipe(recipeId).getSimpleName();
                isIng = Mapper.searchRecipe(recipeId).getIng();
                isPost = false;
                data.add(new RecipeBoxData(simpleName, recipeId, isIng, isPost));
                isRecipe = true;

            }


        }
    }

}
