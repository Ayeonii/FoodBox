package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.Adapter.RecipeBoxAdapter;
import com.example.dldke.foodbox.R;
import com.example.dldke.foodbox.RecipeBoxData;

import java.util.ArrayList;

public class FullRecipeBoxFragment extends Fragment {
    public FullRecipeBoxFragment(){ }

    private RecyclerView recyclerview;
    private RecyclerView.Adapter adapter;
    private ArrayList<RecipeBoxData> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_recipe_box, container, false);

        recyclerview = (RecyclerView)view.findViewById(R.id.recycler_view);
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

    private void prepareData(){
        data.add(new RecipeBoxData("풀레시피1", R.drawable.strawberry));
        data.add(new RecipeBoxData("풀레시피2", R.drawable.strawberry));
        data.add(new RecipeBoxData("풀레시피3", R.drawable.strawberry));
    }
}
