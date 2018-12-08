package com.example.dldke.foodbox.PencilRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.MyRefrigeratorInside.InsideDialog;
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
        private static ArrayList<PencilCartItem> clickFood = new ArrayList<>();
        private static Date inputDBDate ;
        private static String inputDBDateString;
        private Context context;
        private static int clickCnt = 0;

        private static boolean isRefri = false;

        private ArrayList<PencilItem> mItems;

        private ArrayList <LocalRefrigeratorItem> reItems;

        private ArrayList <String> reItemsString = new ArrayList<>();

        private GregorianCalendar cal = new GregorianCalendar();

        private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        public PencilRecyclerAdapter(){ }

        public PencilRecyclerAdapter(Context context, ArrayList<LocalRefrigeratorItem> items){
            reItems = items;
            this.context = context;
        }

        public PencilRecyclerAdapter(ArrayList<PencilItem> items , Context context){
            mItems = items;
            this.context = context;
        }

        public void setIsRefri(boolean isRefri){
            this.isRefri = isRefri;
        }

        public boolean getIsRefri(){
            return isRefri;
        }

        public ArrayList<PencilCartItem> getClickFood(){
            return clickFood;
        }

        public int getClickCnt(){
            return clickCnt;
        }

        public void setClickCnt(int num){this.clickCnt = num;}

        // 새로운 뷰 홀더 생성
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pencilrecipe_list_ingredient,parent,false);
            return new ItemViewHolder(view);
        }

        // View 의 내용을 해당 포지션의 데이터로 바꿈.
        @Override
        public void onBindViewHolder(final ItemViewHolder holder,   final int position) {

            if(!isRefri) {
                holder.food_name.setText(mItems.get(position).getFoodName());
                holder.food_img.setImageURI(mItems.get(position).getFoodImg());
                if(mItems.get(position).getFoodName().length()>6) {
                    holder.food_name.setTextSize(12);
                }
            }
            else{
                holder.food_name.setText(reItems.get(position).getName());
                holder.food_img.setImageURI(reItems.get(position).getImg());
                if(reItems.get(position).getName().length()>6) {
                    holder.food_name.setTextSize(12);
                }
            }

            holder.food_img.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isRefri) {
                            String foodName = mItems.get(position).getFoodName();
                            View view = View.inflate(context, R.layout.pencilrecipe_custom_toast, null);
                            TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                            Toast toast = new Toast(context);
                            makeToast(view, toastText, toast);
                            //중복처리
                            double foodCnt;
                            for (int i = 0; i < clickFood.size(); i++) {
                                if (foodName.equals(clickFood.get(i).getFoodName())) {
                                    foodCnt = clickFood.get(i).getFoodCount() + 1;
                                    isAgain = true;
                                    clickFood.get(i).setFoodCount(foodCnt);
                                }
                            }
                            //중복이 아닐 때
                            if (!isAgain) {
                                int dueDate;
                                try {
                                    dueDate = Mapper.searchFood(mItems.get(position).getFoodName(), mItems.get(position).getFoodSection()).getDueDate();
                                } catch (NullPointerException e) {
                                    dueDate = 0;
                                }
                                Log.e("유통기한", "" + dueDate);
                                cal.add(cal.DATE, dueDate);
                                inputDBDate = cal.getTime(); //연산된 날자를 생성.
                                inputDBDateString = formatter.format(inputDBDate);

                                //clickFoodOnly.add(mItems.get(position).getFoodName());
                                //clickFoodString.add(mItems.get(position).getFoodName());
                                clickFood.add(new PencilCartItem(mItems.get(position).getFoodName()
                                        , mItems.get(position).getFoodImg()
                                        , inputDBDateString
                                        , 1
                                        , mItems.get(position).getFoodSection()
                                        , mItems.get(position).getIsFrozen()
                                        , dueDate));
                            }
                            isAgain = false;
                        }else{
                            for(int i =0 ; i<reItems.size(); i ++){
                                reItemsString.add(reItems.get(i).getName());
                            }
                            Log.e("position",": "+position);
                            InsideDialog insideDialog = new InsideDialog(context, reItems.get(position).getSection(), false, reItems, reItemsString);
                            insideDialog.showItemDialog(position);
                        }
                    }
            });

        }


        // 데이터 셋의 크기를 리턴
        @Override
        public int getItemCount() {
            if(!isRefri)
                return mItems.size();
            else
                return reItems.size();
        }

        // 커스텀 뷰홀더
        // item layout 에 존재하는 위젯들을 바인딩
        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView food_name;
            private ImageView food_img;

            public ItemViewHolder(View itemView) {
                super(itemView);
                food_name = (TextView) itemView.findViewById(R.id.foodText);
                food_img = (ImageView) itemView.findViewById(R.id.userImg);
            }
        }

        public void makeToast(View view, TextView toastText, Toast toast){
            toast.setView(view);
            toastText.setText("+"+(clickCnt+1));
            toast.setGravity(Gravity.TOP ,300,55);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            clickCnt++;
        }

}