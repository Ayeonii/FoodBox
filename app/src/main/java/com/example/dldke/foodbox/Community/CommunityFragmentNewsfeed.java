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

import static com.example.dldke.foodbox.Community.CommunityRecyclerAdapter.cnt;

public class CommunityFragmentNewsfeed extends Fragment {
    View view;
    private CommunityRecyclerAdapter communityRecyclerAdapter = new CommunityRecyclerAdapter();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static List<CommunityItem> sharedList = new ArrayList<>();

    /*****************post에 데이터 들어갈대까지만 public **********************************/
    public static ArrayList<CommunityItem> list = new ArrayList<>();
    /*********************DB에서 불러온 후엔 삭제***************************/
    private List<Drawable> foodImg = new ArrayList<>();
    public static boolean isEnterFirst;
    public static List<PostDO> postList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_newsfeed, container, false);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.newsfeed_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommunityRecyclerAdapter(list, view.getContext());
        recyclerView.setAdapter(adapter);
        setData();

        PostAsyncTask mProcessTask = new PostAsyncTask();
        mProcessTask.execute();





        return view;
    }

    public class PostAsyncTask extends AsyncTask< Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int cnt = 6;
            postList = Mapper.scanPost();
            publishProgress(cnt);
            cnt = cnt+6;
            setData();
            return cnt;
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
        /**********임시 데이********/

        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_fried_pork,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food2,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food3,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food4,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food5,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food6,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food7,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food8,null));
        foodImg.add(getContext().getResources().getDrawable(R.drawable.temp_shared_food9,null));


        list.add(new CommunityItem("미인 이아연", postList.get(i).getTitle(), Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()),R.drawable.temp_shared_food+i,R.drawable.temp_profile1, )

                list.add(new CommunityItem("미인 이아연", "매콤달콤한 제육볶음 만들어봐요!", "제육볶음", R.drawable.temp_fried_pork, R.drawable.temp_profile1, false));
                list.add(new CommunityItem("최지원", "일본에서 먹는게 더 맛있을 수도..", "밀푀유 나베", R.drawable.temp_shared_food, R.drawable.temp_profile2, false));
                list.add(new CommunityItem("김태우", "난 별로 안좋아하지만 누군가는 좋아하니깐 올림", "순대볶음", R.drawable.temp_shared_food2, -1, false));
                list.add(new CommunityItem("미녀 이아연", "PC방에 몰래 싸가기 콜라는 덤", "스팸마요덮밥", R.drawable.temp_shared_food3, R.drawable.temp_profile1, false));
                list.add(new CommunityItem("이가영", "입에 넣자마자 없음.", "도토리묵무침", R.drawable.temp_shared_food4, R.drawable.temp_profile3, false));
                list.add(new CommunityItem("송한솔", "바지락 듬뿍 된장찌개", "바지락 된장찌개", R.drawable.temp_shared_food5, R.drawable.temp_profile4, false));
                list.add(new CommunityItem("안승화", "카레카레카레 맛있는 카레", "카레라이스", R.drawable.temp_shared_food6, -1, false));
                list.add(new CommunityItem("큐트 아연..하..", "하..제목 짓기 귀찮..ㅠㅠ", "닭갈비", R.drawable.temp_shared_food7, R.drawable.temp_profile1, false));
                list.add(new CommunityItem("이기현", "술안주로 딱인 꼬막무침", "꼬막무침", R.drawable.temp_shared_food8, -1, false));
                list.add(new CommunityItem("이은비", "매운맛의 최강 낙지볶음", "낙지볶음", R.drawable.temp_shared_food9, -1, false));

                cnt++;
        adapter.notifyDataSetChanged();
    }
}
