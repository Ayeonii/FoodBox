package com.example.dldke.foodbox.Adapter;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.CurrentDate;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.PencilCartItem;
import com.example.dldke.foodbox.PencilItem;
import com.example.dldke.foodbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


/*
 * 직접입력 RecyclerAdapter
 */
public class PencilRecyclerAdapter extends RecyclerView.Adapter<PencilRecyclerAdapter.ItemViewHolder> {
        boolean isAgain = false;
        private static CurrentDate currentDate = new CurrentDate();
        private static ArrayList<String> clickFoodString = new ArrayList<>();
        private static ArrayList<PencilCartItem> clickFood = new ArrayList<>();
        private static Date inputDBDate ;
        private static String inputDBDateString;
        //public static ArrayList<String> clickFoodOnly = new ArrayList<>();

    private ArrayList<PencilItem> mItems;

        private GregorianCalendar cal = new GregorianCalendar();
        private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");




    public PencilRecyclerAdapter (){ }

        public PencilRecyclerAdapter(ArrayList<PencilItem> items){
            mItems = items;
        }
        public ArrayList<PencilCartItem> getClickFood(){
            return clickFood;
        }

    // 새로운 뷰 홀더 생성
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_ingredient_list,parent,false);
        return new ItemViewHolder(view);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder( ItemViewHolder holder,   final int position) {
        holder.food_name.setText(mItems.get(position).getFoodName());
        holder.food_img.setImageURI(mItems.get(position).getFoodImg());
        if(mItems.get(position).getFoodName().length()>6) {
            holder.food_name.setTextSize(12);
        }
        holder.food_img.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String foodName = mItems.get(position).getFoodName();
                    Log.e("foodName", ""+foodName);
                    Log.e("position", ""+position);

                    //중복처리
                    double foodCnt;
                    for(int i =0 ; i<clickFood.size(); i++){
                        if(foodName.equals(clickFood.get(i).getFoodName())){
                            foodCnt = clickFood.get(i).getFoodCount()+1;
                            isAgain = true;
                            clickFood.get(i).setFoodCount(foodCnt);
                        }
                    }
                    //중복이 아닐 때
                    if(!isAgain){
                        int dueDate;
                        try {
                             dueDate = Mapper.searchFood(mItems.get(position).getFoodName(), mItems.get(position).getFoodSection()).getDueDate();
                        } catch (NullPointerException e) {
                             dueDate = 0;
                        }
                        Log.e("유통기한", ""+dueDate);
                        cal.add(cal.DATE,dueDate);
                        inputDBDate = cal.getTime(); //연산된 날자를 생성.
                        inputDBDateString = formatter.format(inputDBDate);

                        //clickFoodOnly.add(mItems.get(position).getFoodName());
                        //clickFoodString.add(mItems.get(position).getFoodName());
                        clickFood.add(new PencilCartItem(mItems.get(position).getFoodName()
                                ,mItems.get(position).getFoodImg()
                                ,inputDBDateString
                                ,1
                                ,mItems.get(position).getFoodSection()
                                ,dueDate));
                    }
                    isAgain = false;
                }
        });
    }

    // 데이터 셋의 크기를 리턴
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private ImageView food_img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.foodText);
            food_img = (ImageView) itemView.findViewById(R.id.foodImg);
        }
    }



}