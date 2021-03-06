package com.example.dldke.foodbox.HalfRecipe;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.InfoDO;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RefrigeratorDO;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class HalfRecipeIngreAdapter extends RecyclerView.Adapter<HalfRecipeIngreAdapter.ItemViewHolder> {

    ArrayList<HalfRecipeIngreItem> mItems;
    List<InfoDO> infoItems = new ArrayList<>();
    List<RefrigeratorDO.Item> refriItems = new ArrayList<>();
    List<RefrigeratorDO.Item> refriSideItems = new ArrayList<>();
    List<RefrigeratorDO.Item> refriFrozenItems = new ArrayList<>();
    private Boolean[] checkIngre;
    String ingreType;
    private Context context;
    private String[] foodImgUri;

    public HalfRecipeIngreAdapter(ArrayList<HalfRecipeIngreItem> mItems, int arraySize, Boolean[] check, String ingreType) {
        this.mItems = mItems;
        checkIngre = new Boolean[arraySize];
        foodImgUri = new String[arraySize];
        System.arraycopy(check, 0, this.checkIngre, 0, arraySize);
        this.ingreType = ingreType;
    }

    public Boolean[] getCheckIngre() {
        return checkIngre;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.halfrecipe_ingredient_item, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        String foodName = mItems.get(position).getName();
        for (int i=0; i<mItems.size(); i++) {
            foodImgUri[i]="file://"+context.getFilesDir() + "default.jpg";
        }

        if (ingreType.equals("sideDish")) {
            infoItems = Mapper.scanKindof("frozen");
            for (int i=0; i<mItems.size(); i++) {
                for (int j = 0; j< infoItems.size(); j++) {
                    if (mItems.get(i).getName().equals(infoItems.get(j).getName())) {
                        foodImgUri[i] = "file://"+context.getFilesDir() + foodName + ".jpg";
                        break;
                    }
                }
            }
        } else if(ingreType.equals("frozen")) {
            refriItems = Mapper.scanRefri();
            for (int i = 0; i < refriItems.size(); i++) {
                if (refriItems.get(i).getSection().equals("sideDish")) {
                    refriSideItems.add(refriItems.get(i));
                }
                else {
                    refriFrozenItems.add(refriItems.get(i));
                }
            }
            for (int i = 0; i < mItems.size(); i++) {
                for (int j = 0; j < refriFrozenItems.size(); j++) {
                    if (mItems.get(i).getName().equals(refriFrozenItems.get(j).getName())) {
                        foodImgUri[i] = "file://" + context.getFilesDir() + foodName + ".jpg";
                        break;
                    }
                }
            }
        } else {
            for (int i=0; i<mItems.size(); i++) {
                foodImgUri[i]="file://"+context.getFilesDir() + foodName+ ".jpg";
            }
        }

        holder.mNameTv.setText(foodName);
        holder.food_Img.setImageURI(Uri.parse(foodImgUri[position]));

        if (!checkIngre[position]) {
            holder.ivCheck.setVisibility(View.GONE);
        } else {
            holder.ivCheck.setVisibility(View.VISIBLE);
        }

        holder.food_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "food clicked!");
                holder.ivCheck.setVisibility(View.VISIBLE);
                checkIngre[position] = true;
            }
        });

        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "check clicked!");
                holder.ivCheck.setVisibility(View.GONE);
                checkIngre[position] = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTv;
        private ImageView ivCheck;
        private ImageView food_Img;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.itemNameTv);
            ivCheck = (ImageView) itemView.findViewById(R.id.img_check);
            food_Img = (ImageView) itemView.findViewById(R.id.img_food);
        }
    }
}
