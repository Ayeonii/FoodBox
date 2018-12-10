package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class MyRecipeBoxHalfRecipeAdapter extends RecyclerView.Adapter<MyRecipeBoxHalfRecipeAdapter.ViewHolder> {

    public MyRecipeBoxHalfRecipeAdapter(){}

    public String getRecipeId(){
        return recipe_id;
    }

    public static boolean IsPost() { return isPost; }

    //등록된 간이레시피 ID 가져오기 위한 설정
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();
    private static String recipe_id;
    private String TAG="MyRecipeBoxHalfRecipeAdapter";
    private static boolean isPost;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private Context context;
        private TextView isIng;
        private ImageView post;

        public ViewHolder(Context context, View view){
            super(view);
            this.name = (TextView)view.findViewById(R.id.half_recipe_title);
            this.context = context;
            this.isIng = (TextView) view.findViewById(R.id.ing);
            this.post = (ImageView) view.findViewById(R.id.recipe_post);
            view.setOnClickListener(this);
        }

        //'자세히 보기' 눌렀을 때, 해당 레시피 ID의 식재료 보여주기
        public void onClick(View view){
            int position = getAdapterPosition();
            isPost = recipedata.get(position).isPost();
            recipe_id = recipedata.get(position).getRecipeId();

            if(isPost){
                Intent FullRecipeDetailActivity = new Intent(context,RecipeBoxFullRecipeDetailActivity.class);
                context.startActivity(FullRecipeDetailActivity);
            }
            else {
                Intent RecipeDetailActivity = new Intent(context, RecipeBoxHalfRecipeDetailActivity.class);
                context.startActivity(RecipeDetailActivity);
            }

        }
    }

    public MyRecipeBoxHalfRecipeAdapter(ArrayList<RecipeBoxData> recipedata){
        this.recipedata = recipedata;
        Log.e(TAG, "recipedata");
        for(int i = 0; i<recipedata.size(); i++){
            Log.e(TAG, "simplename : "+ recipedata.get(i).getFoodname());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_halfrecipe_list_item, parent, false);
        Context context = parent.getContext();
        return  new ViewHolder(context,view);
    }

    public void onBindViewHolder(final MyRecipeBoxHalfRecipeAdapter.ViewHolder holder, final int position){
        int isIng = recipedata.get(position).isIng();
        isPost = recipedata.get(position).isPost();
        Log.e(TAG, "isPost : "+isPost);

        holder.name.setText(recipedata.get(position).getSimpleName());
        holder.post.setVisibility(View.GONE);
        if(isPost){
            holder.post.setVisibility(View.VISIBLE);
        }

        if(isIng==0){
            holder.isIng.setText("작성 완료");
            holder.isIng.setTextColor(Color.BLUE);
        } else if(isIng==1){
            holder.isIng.setText("작성중");
            holder.isIng.setTextColor(Color.RED);
        }
        else if(isIng==2){
            holder.isIng.setText("");
        }

    }


    public int getItemCount(){
        return recipedata.size();
    }
}