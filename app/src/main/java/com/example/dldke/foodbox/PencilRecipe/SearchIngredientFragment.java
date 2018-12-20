package com.example.dldke.foodbox.PencilRecipe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.CloudVision.PopupDialog;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.MyRefrigeratorInside.RefrigeratorFrozenInsideActivity;
import com.example.dldke.foodbox.MyRefrigeratorInside.RefrigeratorInsideActivity;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class SearchIngredientFragment extends  android.support.v4.app.Fragment {


    private PencilRecyclerAdapter pencilRecycler = new PencilRecyclerAdapter();
    private static AllFoodListFragment allList = new AllFoodListFragment();
    private static RefrigeratorFrozenInsideActivity frozenActivity = new RefrigeratorFrozenInsideActivity();
    private static RefrigeratorInsideActivity refriInsideActivity = new RefrigeratorInsideActivity();
    private static PopupDialog popup = new PopupDialog();

    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각 자음 마다 가지는 글자수
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    private static List<PencilItem> allfoodList = new ArrayList<>();
    private static List<LocalRefrigeratorItem> frozenList = new ArrayList<>();
    private static List<RefrigeratorDO.Item> refriList = new ArrayList<>();
    private static List<PencilItem> visionList = new ArrayList<>();
    private static boolean isFromRefri = false;

    static ArrayList<PencilItem> list = new ArrayList<>();
    static ArrayList<LocalRefrigeratorItem> allRefriList = new ArrayList<>();
    static RecyclerView.Adapter adapter;
    static RecyclerView.Adapter refriAdapter;
    static String searchText;
    static RecyclerView recyclerView;
    static String foodImg;
    private static Context context ;

    public SearchIngredientFragment(){}

    public void setFromRefri(boolean isFromRefri) {
        this.isFromRefri = isFromRefri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pencilrecipe_fragment_search, container, false);
        context = getContext();
        allfoodList = allList.getAllFoodListWithFrozen();
        frozenList = frozenActivity.getFrozenList();
        refriList =refriInsideActivity.getRefrigeratorItem();
        visionList = popup.getVisionList();

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycler);
        recyclerView.setHasFixedSize(true);
        //어댑터 연결
        adapter = new PencilRecyclerAdapter(list,view.getContext());
        refriAdapter = new PencilRecyclerAdapter(view.getContext(),allRefriList);

        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        if(!pencilRecycler.getIsRefri()) {
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setAdapter(refriAdapter);
        }

        return view;
    }

    /********************Searching method *****************************/
    static public void search(String charText, boolean isFromPencil,boolean isFromFrozen, boolean isFromRefri, boolean isFromVision) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려주기 위함.
        list.clear();
        allRefriList.clear();
        searchText = charText;

        if (charText.length() == 0) {
        }
        else {
            if (isFromPencil) {
                // 리스트의 모든 데이터를 검색함.
                Log.e("Refri", "" + allfoodList.size());
                for (int i = 0; i < allfoodList.size(); i++) {
                    if (matchString(allfoodList.get(i).getFoodName(), charText)) {
                        foodImg = "file://"+context.getFilesDir()+allfoodList.get(i).getFoodName()+".jpg";
                        Log.e("url",""+foodImg);
                        list.add(new PencilItem(allfoodList.get(i).getFoodName(), Uri.parse(foodImg), allfoodList.get(i).getFoodSection(), allfoodList.get(i).getIsFrozen()));
                        Log.e("Refri", "listSize" + list.size());
                    }
                }
                if (list.size() == 0) {
                    //검색된 것이 아무것도 없을때,
                    Log.e("Refri", "없음" + list.size());
                    foodImg = "file://"+context.getFilesDir()+"default.jpg";
                    Log.e("url",""+foodImg);
                    list.add(new PencilItem(searchText, Uri.parse(foodImg)));
                }
                adapter.notifyDataSetChanged();
            } else if (isFromFrozen) {

                for (int i = 0; i < frozenList.size(); i++) {

                    if (matchString(frozenList.get(i).getName(), charText)) {
                        allRefriList.add(new LocalRefrigeratorItem(frozenList.get(i).getName()
                                , frozenList.get(i).getCount()
                                , frozenList.get(i).getDueDate()
                                , frozenList.get(i).getImg()
                                , frozenList.get(i).getSection()));
                    }
                }
                if (frozenList.size() == 0) {
                    SearchIngredientFragment searchIngredientFragment = new SearchIngredientFragment();
                    searchIngredientFragment.setFromRefri(false);
                }
                refriAdapter.notifyDataSetChanged();

            } else if (isFromRefri) {
                for (int i = 0; i < refriList.size(); i++) {
                    if (matchString(refriList.get(i).getName(), charText)) {
                        foodImg = "file://"+context.getFilesDir()+refriList.get(i).getName()+".jpg";
                        allRefriList.add(new LocalRefrigeratorItem(refriList.get(i).getName()
                                , refriList.get(i).getCount()
                                , refriList.get(i).getDueDate()
                                , Uri.parse(foodImg)
                                , refriList.get(i).getSection()));
                    }
                }
                if (refriList.size() == 0) {
                    SearchIngredientFragment searchIngredientFragment = new SearchIngredientFragment();
                    searchIngredientFragment.setFromRefri(false);
                }

                refriAdapter.notifyDataSetChanged();

            } else if (isFromVision) {
                // 리스트의 모든 데이터를 검색함.
                for (int i = 0; i < visionList.size(); i++) {
                    if (matchString(visionList.get(i).getFoodName(), charText)) {
                        foodImg = "file://"+context.getFilesDir()+ visionList.get(i).getFoodName()+".jpg";
                        list.add(new PencilItem(visionList.get(i).getFoodName(), Uri.parse(foodImg), visionList.get(i).getFoodSection(), visionList.get(i).getIsFrozen()));
                    }
                }
                if (list.size() == 0) {
                    //검색된 것이 아무것도 없을때,
                    foodImg = "file://"+context.getFilesDir()+"default.jpg";
                    list.add(new PencilItem(searchText, Uri.parse(foodImg)));
                }
                adapter.notifyDataSetChanged();
            }
        }




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
