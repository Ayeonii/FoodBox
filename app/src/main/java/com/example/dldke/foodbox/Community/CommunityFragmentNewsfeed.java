package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
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



//public class CommunityFragmentNewsfeed extends Fragment implements CommunityLoadingAdapter.OnLoadMoreListener {

public class CommunityFragmentNewsfeed extends Fragment implements CommunityLoadingAdapter.OnLoadMoreListener {

    private CommunityLoadingAdapter mAdapter;
    private ArrayList<CommunityItem> itemList;
    private static ArrayList<CommunityItem> favoriteList  = new ArrayList<>();
    private static List<PostDO> postList;
    private  boolean isFalse = true;

    public CommunityFragmentNewsfeed(){}

    public List<PostDO> getPostList(){
        return postList;
    }


    public List<CommunityItem> getFavoriteList(){
        return favoriteList;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_newsfeed, container, false);

        itemList = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.newsfeed_recycler);
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
           // mAdapter.setProgressMore(true);
        }
        protected List<PostDO> doInBackground(Void... params) {
            postList = Mapper.scanPost();
            return postList;
        }

        protected void onPostExecute(List result) {
            Log.e("size:","끝");
            // setData(3);

            if(postList.size() != 0 ){
                loadData();
            }
            else{
               // mAdapter.setProgressMore(false);
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

                int start = mAdapter.getItemCount() -1 ;
                int end = start + 2;

                try {

                    if (end >= postList.size()) {
                        end = start + (end - postList.size());
                        isFalse = false;
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

                    mAdapter.addItemMore(itemList);
                    mAdapter.setMoreLoading(isFalse);

                } catch (Exception e) {

                }

            }
        }, 2000);

    }

    private void loadData() {
        itemList.clear();
        int end ;
        if (postList.size() < 4)
            end = postList.size();
        else
            end = 4;
        try {
            Log.e("","postsize"+postList.size());
            for (int i = 0; i <=end; i++) {

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

                Log.e("load", "i"+i + ": "+ postList.get(i).getTitle());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("load", "catch ");
        }
        mAdapter.addAll(itemList);
    }

    public  class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
