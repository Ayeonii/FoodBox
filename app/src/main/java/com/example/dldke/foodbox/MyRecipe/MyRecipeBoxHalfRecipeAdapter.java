package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class MyRecipeBoxHalfRecipeAdapter extends RecyclerView.Adapter<MyRecipeBoxHalfRecipeAdapter.ViewHolder> {
    public MyRecipeBoxHalfRecipeAdapter(){}

    public String getRecipeId(){
        return recipe_id;
    }

    //등록된 간이레시피 ID 가져오기 위한 설정
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();
    private static String recipe_id;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private Context context;
        private TextView isIng;

        public ViewHolder(Context context, View view){
            super(view);
            this.name = (TextView)view.findViewById(R.id.half_recipe_title);
            this.context = context;
            this.isIng = (TextView) view.findViewById(R.id.ing);
            view.setOnClickListener(this);
        }

        //'자세히 보기' 눌렀을 때, 해당 레시피 ID의 식재료 보여주기
        public void onClick(View view){
            int position = getAdapterPosition();

            //포지션 확인(추후에는 지울것임 - test용)
            //if(position != RecyclerView.NO_POSITION) Toast.makeText(context,"포지션"+position, Toast.LENGTH_SHORT).show();

            recipe_id = recipedata.get(position).getRecipeId();
            Intent RecipeDetailActivity = new Intent(context, RecipeBoxHalfRecipeDetailActivity.class);
            context.startActivity(RecipeDetailActivity);
        }
    }

    public MyRecipeBoxHalfRecipeAdapter(ArrayList<RecipeBoxData> recipedata){
        this.recipedata = recipedata;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_box_halfrecipe_list_item, parent, false);
        Context context = parent.getContext();
        return  new ViewHolder(context,view);
    }

    public void onBindViewHolder(final MyRecipeBoxHalfRecipeAdapter.ViewHolder holder, final int position){
        boolean isIng = recipedata.get(position).isIng();
        holder.name.setText(recipedata.get(position).getSimpleName());

        if(isIng){
            holder.isIng.setText("작성중");
        }
        else{
            holder.isIng.setText("작성 완료");
            holder.isIng.setTextColor(Color.BLUE);
        }
    }

    public int getItemCount(){
        return recipedata.size();
    }
}