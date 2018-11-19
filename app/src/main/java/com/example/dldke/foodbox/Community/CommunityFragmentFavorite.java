package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class CommunityFragmentFavorite extends Fragment {
    private CommunityRecyclerAdapter communityRecyclerAdapter = new CommunityRecyclerAdapter();
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static ArrayList<CommunityItem> list = new ArrayList<>();
    private static ArrayList<CommunityItem> favorite_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_favorite, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommunityRecyclerAdapter(favorite_list, view.getContext());
        recyclerView.setAdapter(adapter);

        Log.e("communityRecycler.......size()", ""+communityRecyclerAdapter.getFavoriteList().size());

        for(int i =0 ; i < communityRecyclerAdapter.getFavoriteList().size(); i++) {
            favorite_list.add(communityRecyclerAdapter.getFavoriteList().get(i));
        }
        communityRecyclerAdapter.setStarBtn(true);
        adapter.notifyDataSetChanged();
        return view;
    }

}
