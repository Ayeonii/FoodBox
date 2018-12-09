package com.example.dldke.foodbox.CloudVision;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilItem;
import com.example.dldke.foodbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ItemViewHolder> {

    private static List<PencilItem> allFoodItems = new ArrayList<>();
    private List<PencilCartItem> changeItem = new ArrayList<>();
    private GregorianCalendar cal = new GregorianCalendar();
    private static Date inputDBDate ;
    private static String inputDBDateString;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String TAG="PopupAdapter";

    public PopupAdapter (List<PencilItem> allfoodList){
        this.allFoodItems = allfoodList;
        //Log.e(TAG, "AllFoodItems"+ allFoodItems.get(0).getFoodName());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private ImageView food_img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.vision_foodText);
            food_img = (ImageView) itemView.findViewById(R.id.vision_userImg);
        }
    }

    @Override
    public PopupAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vision_ingredient_item,parent,false);
        return new PopupAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final PopupAdapter.ItemViewHolder holder, final int position) {

        holder.food_name.setText(allFoodItems.get(position).getFoodName());
        holder.food_img.setImageURI(allFoodItems.get(position).getFoodImg());
        if (allFoodItems.get(position).getFoodName().length() > 6) {
            holder.food_name.setTextSize(12);
        }
        holder.food_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dueDate;
                boolean isFrozen;
                try{
                    dueDate = Mapper.searchFood(allFoodItems.get(position).getFoodName(), allFoodItems.get(position).getFoodSection()).getDueDate();
                }catch (NullPointerException e){
                    dueDate = 0;
                }
                cal.add(cal.DATE, dueDate);
                inputDBDate = cal.getTime();
                inputDBDateString = formatter.format(inputDBDate);

                if(allFoodItems.get(position).getIsFrozen()){
                    isFrozen = true;
                }
                else {
                    isFrozen = false;
                }

                changeItem.add(new PencilCartItem(allFoodItems.get(position).getFoodName(), allFoodItems.get(position).getFoodImg(), inputDBDateString, 1, allFoodItems.get(position).getFoodSection(), isFrozen, dueDate));
                Log.e(TAG, "선택한 재료 : "+allFoodItems.get(position).getFoodName());

            }
        });
    }


    @Override
    public int getItemCount() {
            return allFoodItems.size();
    }


}
