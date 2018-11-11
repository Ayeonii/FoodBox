package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.dldke.foodbox.Fragments.AllFoodListFragment.*;

public class SearchIngredientFragment extends  android.support.v4.app.Fragment {

    private AllFoodListFragment allList = new AllFoodListFragment();
    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각 자음 마다 가지는 글자수
    //자음
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    private static List<String[]> allfoodList = new ArrayList<String[]>();

    static ArrayList<PencilItem> list = new ArrayList<>();

    static RecyclerView.Adapter adapter;
    static String searchText;
    static RecyclerView recyclerView;
    static String foodImg;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_ingredients, container, false);
        allfoodList = allList.getAllFoodList();
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycler);
        recyclerView.setHasFixedSize(true);
        //어댑터 연결
        adapter = new PencilRecyclerAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(context,5));
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "SearchFrag");

        return view;
    }


    /********************Searching method *****************************/
    static public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려주기 위함.
        list.clear();
        searchText = charText;

        // 문자 입력이 없을때
        if (charText.length() == 0) {
        }
        // 문자 입력을 할때.
        else {
            // 리스트의 모든 데이터를 검색함.
            for (int i = 0; i < allfoodList.size(); i++) {
                    if (matchString(allfoodList.get(i)[0],charText)) {
                        //검색된 데이터 리스트에 추가
                        //디비에서 이미지 가져올때 까진 Img를 AllFoodListFragment에서 static 으로 가져옴.
                        foodImg = "file:///storage/emulated/0/Download/"+allfoodList.get(i)+".jpg";
                        list.add(new PencilItem(allfoodList.get(i)[0],Uri.parse(foodImg),allfoodList.get(i)[1]));
                    }
            }
        }
        // 리스트 데이터가 변경되었으므로 어댑터 갱신.
        adapter.notifyDataSetChanged();
    }

    /**초성인지 검사**/
    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }


    /**해당 문자의 자음 얻기**/
    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }


    /** 해당 문자가 한글인지 검사**/
    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }


    /**초성 검색**/
    public static boolean matchString(String value, String search){
        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if(seof < 0)
            return false; //검색어가 더 길면 false를 리턴.
        for(int i = 0;i <= seof;i++){
            t = 0;
            while(t < slen){
                if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))){
                    //만약 현재 char이 초성이고 value가 한글이면
                    if(getInitialSound(value.charAt(i+t))==search.charAt(t))
                        //각각의 초성끼리 같은지 비교한다
                        t++;
                    else
                        break;

                } else {
                    //char이 초성이 아니라면
                    if(value.charAt(i+t)==search.charAt(t))
                        //그냥 같은지 비교한다.
                        t++;
                    else
                        break;

                }

            }
            if(t == slen)
                return true; //모두 일치한 결과를 찾으면 true를 리턴한다.
        }
        return false; //일치하는 것을 찾지 못했으면 false를 리턴한다.
    }


}
