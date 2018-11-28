package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.PostDO;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragmentFavorite extends Fragment {
    private CommunityRecyclerAdapter communityRecyclerAdapter = new CommunityRecyclerAdapter();
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static ArrayList<CommunityItem> list = new ArrayList<>();
    private static List<PostDO> favorite_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_favorite, container, false);

        list.clear();
        Context context = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommunityRecyclerAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);

        favorite_list = Mapper.scanFavorite();
        adapter.notifyDataSetChanged();
        setData();

        return view;
    }



    private void setData(){
        try{
        for(int i =0 ; i<favorite_list.size(); i++) {
            String imgUrl = Mapper.getImageUrlRecipe(favorite_list.get(i).getRecipeId());
            Bitmap bm = new CommunityFragmentNewsfeed.DownloadImageTask().execute(imgUrl).get();
            list.add(new CommunityItem(favorite_list.get(i).getWriter()
                    ,favorite_list.get(i).getTitle()
                    ,Mapper.searchRecipe(favorite_list.get(i).getRecipeId()).getDetail().getFoodName()
                    ,bm
                    ,R.drawable.temp_profile4
                    ,Mapper.matchFavorite(favorite_list.get(i).getPostId())
                    ,favorite_list.get(i).getPostId()
                    ,favorite_list.get(i).getRecipeId()
            ));
        }
            adapter.notifyDataSetChanged();
        }
        catch (Exception e){
            adapter.notifyDataSetChanged();
        }

    }

}
