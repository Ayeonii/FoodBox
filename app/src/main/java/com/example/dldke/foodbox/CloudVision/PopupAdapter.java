package com.example.dldke.foodbox.CloudVision;

import android.app.Dialog;
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
import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ItemViewHolder> {

    private NotMatchAdapter notMatchAdapter = new NotMatchAdapter();
    private SearchIngredientFragment searchIngredientFragment = new SearchIngredientFragment();
    private static List<PencilItem> allFoodItems = new ArrayList<>();
    private static ArrayList<PencilCartItem> changeItem = new ArrayList<>();
    private GregorianCalendar cal = new GregorianCalendar();
    private static Date inputDBDate ;
    private static String inputDBDateString;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String TAG="PopupAdapter";
    private RecyclerView notmatch;
    private int index;
    private List<String> notMatchingInfo;
    private Dialog dlg;
    private static List<String[]> newOldName = new ArrayList<String[]>();
    private int matchSize = VisionReturnActivity.matchSize;


    public PopupAdapter(){}


    public PopupAdapter (List<PencilItem> allfoodList,RecyclerView notmatch, int index, List<String> notMatchingInfo, Dialog dlg){
        this.allFoodItems = allfoodList;
        this.index = index;
        this.notmatch = notmatch;
        this.notMatchingInfo = notMatchingInfo;
        this.dlg = dlg;
        searchIngredientFragment.setFromRefri(false);
    }

    public void setNewOldName(int removedPosition){
        if(removedPosition>=matchSize) {
            int idx = removedPosition - matchSize;
            Log.e(TAG, "idx"+idx+"removedPosition: "+removedPosition+"matchSize = "+matchSize);
            Log.e(TAG, ""+newOldName.get(idx));
            newOldName.remove(idx);
        }

    }

    public List<String[]> getNewOldName(){
        return newOldName;
    }

    public void setNewOldNameClear(){
        newOldName.clear();
    }

    public List<PencilCartItem> getChangeItem(){
        return changeItem;
    }

    public void setChangeItemClear(){
        changeItem.clear();
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

                changeItem.add(new PencilCartItem(allFoodItems.get(position).getFoodName()
                                                , allFoodItems.get(position).getFoodImg()
                                                , inputDBDateString
                                                , 1
                                                , allFoodItems.get(position).getFoodSection()
                                                , isFrozen
                                                , dueDate));

                Log.e(TAG, "선택한 재료 : "+allFoodItems.get(position).getFoodName());
                Log.e(TAG, "삭제될 재료 : "+notMatchingInfo.get(index));
                newOldName.add(new String[]{notMatchingInfo.get(index),allFoodItems.get(position).getFoodName()});
                notMatchingInfo.remove(index);
                for(int i = 0; i<notMatchingInfo.size(); i++){
                    Log.e(TAG, "재료 : "+notMatchingInfo.get(i));
                }
                VisionReturnActivity visionReturnActivity = new VisionReturnActivity();
                visionReturnActivity.notMatchingIngredient(notmatch, notMatchingInfo);
                visionReturnActivity.matchingIngredient();
                notMatchAdapter.notifyDataSetChanged();
                //cancel();
               dlg.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
            return allFoodItems.size();
    }


}
