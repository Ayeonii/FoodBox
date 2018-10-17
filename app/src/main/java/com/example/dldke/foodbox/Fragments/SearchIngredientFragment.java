package com.example.dldke.foodbox.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.Activity.PencilRecipeActivity;
import com.example.dldke.foodbox.Adapter.PencilRecyclerAdapter;
import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.example.dldke.foodbox.Fragments.AllFoodListFragment.*;

public class SearchIngredientFragment extends  android.support.v4.app.Fragment {



    static ArrayList<PencilItem> list = new ArrayList<>();

    static RecyclerView.Adapter adapter;
    static ArrayList<String> foodlist = new ArrayList<String>() ;
    static List<String> foodArray;
    static String searchText;
    static RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search_ingredients, container, false);
        foodlist.addAll(allfoodList);

        for(int i =0 ; i<foodlist.size(); i++)
        {
            Log.e("index:"+i,"내용:"+foodlist.get(i));
        }

        searchText = "";
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


        // 문자 입력시마다 리스트를 지우고 새로 뿌려줌.
        list.clear();

        searchText = charText;
        // 문자 입력이 없을때는 모든 데이터를 보여줌.
        if (charText.length() == 0) {
            Log.e("문자입력 없음2", "문자입력 없음");

        }
        // 문자 입력을 할때.
        else {

            Log.e("문자입력 할때 ", ""+charText);
            // 리스트의 모든 데이터를 검색함.
            for (int i = 0; i < foodlist.size(); i++) {

                //입력단어 수가 리스트단어 수보다 많으면 substring에서 에러발생 일어나기 때문에 필터링.
                if (foodlist.get(i).length() >= charText.length()) {

                    // arraylist의 모든 데이터에 입력받은 단어(charText)가 맨 앞에서부터 포함되어 있으면 true를 반환함.
                    //substring으로 입력단어만큼 잘라주지 않으면, 단어가 중간이나 맨 뒤에 포함되어 있어도 필터링이 되지 않음.
                    if (foodlist.get(i).toLowerCase().substring(0, charText.length()).equals(charText) || foodlist.get(i).toUpperCase().substring(0, charText.length()).equals(charText)) {

                        //Img = getResources().getDrawable( R.drawable.ic_circle_food);//sdk 22이하일 때
                        //검색된 데이터 리스트에 추가
                        list.add(new PencilItem(foodlist.get(i),AllFoodListFragment.Img));
                        //디비에서 이미지 가져올때 까진 Img를 AllFoodListFragment에서 static 으로 가져옴.

                    }
                }
            }
        }

        // 리스트 데이터가 변경되었으므로 어댑터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

}
