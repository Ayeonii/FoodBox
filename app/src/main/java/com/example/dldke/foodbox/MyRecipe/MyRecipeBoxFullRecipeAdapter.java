package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class MyRecipeBoxFullRecipeAdapter extends RecyclerView.Adapter<MyRecipeBoxFullRecipeAdapter.ViewHolder> {

    private String TAG = "MyRecipeBoxFullRecipeAdapter";
    //등록된 간이레시피 ID 가져오기 위한 설정
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();
    private static String recipe_id;

    public MyRecipeBoxFullRecipeAdapter(){}

    public MyRecipeBoxFullRecipeAdapter(ArrayList<RecipeBoxData> recipedata){
        this.recipedata = recipedata;
    }

    public String getRecipeId(){
        return recipe_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, share;
        public ImageView image;
        private Context context;

        public ViewHolder(Context context, View view){
            super(view);
            this.name = (TextView)view.findViewById(R.id.full_recipe_title);
            this.share = (TextView)view.findViewById(R.id.share);
            this.image = (ImageView)view.findViewById(R.id.picture);
            this.context = context;
            view.setOnClickListener(this);
        }

        //'자세히 보기' 눌렀을 때, 해당 레시피 ID의 식재료 보여주기
        public void onClick(View view){
            int position = getAdapterPosition();
            recipe_id = recipedata.get(position).getRecipeId();
            Intent RecipeBoxFullRecipeDetailActivity = new Intent(context, RecipeBoxFullRecipeDetailActivity.class);
            context.startActivity(RecipeBoxFullRecipeDetailActivity);

        }
    }



    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_fullrecipe_list_item, parent, false);
        Context context = parent.getContext();
        return  new ViewHolder(context,view);
    }

    public void onBindViewHolder(final MyRecipeBoxFullRecipeAdapter.ViewHolder holder, final int position){
        //Log.e(TAG,"이름 가져와!!!!!"+recipedata.get(position).getFoodname());
        String name = recipedata.get(position).getFoodname();
        boolean shared = recipedata.get(position).isShared();

        holder.name.setText(name);
        holder.image.setImageResource(recipedata.get(position).getImage());
        if(shared){
            holder.share.setVisibility(View.VISIBLE);
        }
        else {
            holder.share.setVisibility(View.INVISIBLE);
        }
    }

    public int getItemCount(){
        return recipedata.size();
    }
}