package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;


public class CommunityFragmentNewsfeed extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static ArrayList<CommunityItem> list = new ArrayList<>();
    private static List<PostDO> postList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_newsfeed, container, false);

        Context context = view.getContext();
        list.clear();
        recyclerView = (RecyclerView) view.findViewById(R.id.newsfeed_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommunityRecyclerAdapter(list, context);
        recyclerView.setAdapter(adapter);

        postList = Mapper.searchPost("title"," ");
        setData();
        return view;
    }

    public class PostAsyncTask extends AsyncTask< Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            Log.d("PostAsyncTask", params + " % ");

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            }
        }




    private void setData(){
        for(int i =0 ; i<postList.size(); i++) {
            list.add(new CommunityItem(postList.get(i).getWriter()
                                        ,postList.get(i).getTitle()
                                        ,Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                                        ,R.drawable.temp_shared_food
                                        ,R.drawable.temp_profile1
                                        ,Mapper.matchFavorite(postList.get(i).getPostId())
                                        ,postList.get(i).getPostId()
            ));
        }

        adapter.notifyDataSetChanged();
    }
}
