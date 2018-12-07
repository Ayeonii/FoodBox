package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.PostDO;
import com.example.dldke.foodbox.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragmentRecommend extends Fragment implements CommunityLoadingAdapter.OnLoadMoreListener {
    private CommunityLoadingAdapter mAdapter;
    private ArrayList<CommunityItem> itemList;
    private static List<PostDO> postList;
    private TextView noneRecommend;
    private boolean isEmpty = false;

    private static int cnt = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_recommendation, container, false);

        noneRecommend = (TextView)view.findViewById(R.id.noneRecommend);
        itemList = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recommend_recycler);
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

                return postList;

        }

        protected void onPostExecute(List result) {
            Log.e("size:","끝");
            // setData(3);
            loadData();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        new PostAsync().execute();
        postList = Mapper.recommendRecipe();

        Log.e("RecommendFrag", "onStart");
    }

    //스크롤이 끝에 도달하였을 때 실행 내용
    @Override
    public void onLoadMore() {

                Log.e("Recoomend", "onLoadMore==================");
                mAdapter.setProgressMore(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("MainActivity_", "onLoadMoreRUN!!!==================");
                        itemList.clear();
                        mAdapter.setProgressMore(false);

                        int start = mAdapter.getItemCount();
                        int end = start + 1;

                        try {
                            for (int i = start + 1; i <= end; i++) {
                                String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                                Bitmap bm = new CommunityFragmentNewsfeed.DownloadImageTask().execute(imgUrl).get();
                                Log.e("start:end", "" + start + ":" + end);
                                Log.e("loadMore", "" + postList.get(i).getTitle());

                                itemList.add(new CommunityItem(postList.get(i).getWriter()
                                        , postList.get(i).getTitle()
                                        , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                                        , bm
                                        , R.drawable.temp_profile4
                                        , Mapper.matchFavorite(postList.get(i).getPostId())
                                        , postList.get(i).getPostId()
                                        , postList.get(i).getRecipeId()
                                ));
                                cnt = 1;
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
        Log.e("Recoomend", "loadData==============");
        try {
            if (postList.size() == 0) {
            } else {
                try {
                    for (int i = 0; i < 2; i++) {
                        String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                        Bitmap bm = new DownloadImageTask().execute(imgUrl).get();

                        itemList.add(new CommunityItem(postList.get(i).getWriter()
                                , postList.get(i).getTitle()
                                , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                                , bm
                                , R.drawable.temp_profile4
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
        } catch(NullPointerException e){
            isEmpty = true;
        }
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
