package com.example.dldke.foodbox.Community;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.widget.TextView;


public class CommunityFragmentFavorite extends android.support.v4.app.Fragment implements CommunityLoadingAdapter.OnLoadMoreListener {

    private CommunityLoadingAdapter mAdapter;
    private ArrayList<CommunityItem> itemList;
    private static ArrayList<CommunityItem> favoriteList = new ArrayList<>();
    private static List<PostDO> postList;
    private TextView noneFavorite;

    public CommunityFragmentFavorite() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_favorite, container, false);

        noneFavorite = (TextView) view.findViewById(R.id.noneFavorite);
        itemList = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.favorite_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommunityLoadingAdapter(this, getContext());
        mAdapter.setLinearLayoutManager(mLayoutManager);
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class PostAsync extends AsyncTask<Void, Void, List<PostDO>> {

        protected void onPreExecute() { //2

            super.onPreExecute();
            mAdapter.setProgressMore(true);
        }
        protected List<PostDO> doInBackground(Void... params) {
            postList = Mapper.scanFavorite();
            return postList;
        }

        protected void onPostExecute(List result) {
            if(postList.size() != 0 ){
                mAdapter.setProgressMore(false);

                loadData();
            }else{
                mAdapter.setProgressMore(false);
                mAdapter.setMoreLoading(false);
                noneFavorite.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new PostAsync().execute();

    }


    //스크롤이 끝에 도달하였을 때 실행 내용
    @Override
    public void onLoadMore() {


        mAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                itemList.clear();
                mAdapter.setProgressMore(false);

                int start = mAdapter.getItemCount() - 1;
                int end = start + 2;

                Log.e("postList", "postList : " + postList.size() + "end : " + end);


                try {
                    if (end >= postList.size()) {
                        end = start + (end - postList.size());
                    }
                    for (int i = start + 1; i <= end + 1; i++) {
                        String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                        Bitmap bm = new DownloadImageTask().execute(imgUrl).get();

                        String profileUrl = Mapper.getImageUrlUser(postList.get(i).getWriter());
                        Bitmap userBitmap = new DownloadImageTask().execute(profileUrl).get();

                        itemList.add(new CommunityItem(postList.get(i).getWriter()
                                , postList.get(i).getTitle()
                                , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                                , bm
                                , userBitmap
                                , Mapper.matchFavorite(postList.get(i).getPostId())
                                , postList.get(i).getPostId()
                                , postList.get(i).getRecipeId()
                        ));
                    }

                } catch (Exception e) {

                }
                mAdapter.addItemMore(itemList);
                mAdapter.setMoreLoading(false);

            }
        }, 2000);

    }

    private void loadData() {
        itemList.clear();
        Log.e("MainActivity_", "loadData");

        int end;
        if (postList.size() < 4)
            end = postList.size();
        else
            end = 4;
        try {
            for (int i = 0; i <= end; i++) {

                //비동기
                String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                Bitmap bm = new DownloadImageTask().execute(imgUrl).get();

                String profileUrl = Mapper.getImageUrlUser("lay2");
                Bitmap userBitmap = new DownloadImageTask().execute(profileUrl).get();

                itemList.add(new CommunityItem(postList.get(i).getWriter()
                        , postList.get(i).getTitle()
                        , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                        , bm
                        , userBitmap
                        , Mapper.matchFavorite(postList.get(i).getPostId())
                        , postList.get(i).getPostId()
                        , postList.get(i).getRecipeId()
                ));

                Log.e("load", "" + postList.get(i).getTitle());
            }

        } catch (Exception e) {

        }
        mAdapter.addAll(itemList);
    }





    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
        }
    }


}




/*
public class CommunityFragmentFavorite extends android.support.v4.app.Fragment {
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
                    ,true
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
*/