package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
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

public class RecipeBoxAdapter extends RecyclerView.Adapter<RecipeBoxAdapter.ViewHolder> {
    public RecipeBoxAdapter(){}

    public String getRecipeId(){
        return recipe_id;
    }

    //등록된 간이레시피 ID 가져오기 위한 설정
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();
    private static String recipe_id;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public ImageView image;
        public Button recipe_into;
        private Context context;

        public ViewHolder(Context context, View view){
            super(view);
            this.name = (TextView)view.findViewById(R.id.title);
            this.image = (ImageView)view.findViewById(R.id.picture);
            this.recipe_into = (Button)view.findViewById(R.id.recipe_into);
            this.context = context;
            recipe_into.setOnClickListener(this);
        }

        //'자세히 보기' 눌렀을 때, 해당 레시피 ID의 식재료 보여주기
        public void onClick(View view){
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) Toast.makeText(context,"포지션"+position, Toast.LENGTH_SHORT).show();
            recipe_id = recipedata.get(position).getRecipeId();
            //recipe_id = recipedata.get(position).getName();
            //Intent RecipeDetailActivity = new Intent(context, RecipeBoxHalfRecipeDetailActivity.class);
            //context.startActivity(RecipeDetailActivity);
            Intent RecipeBoxFullRecipeDetailActivity = new Intent(context, RecipeBoxFullRecipeDetailActivity.class);
            context.startActivity(RecipeBoxFullRecipeDetailActivity);

        }
    }

    public RecipeBoxAdapter(ArrayList<RecipeBoxData> recipedata){
        this.recipedata = recipedata;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipebox_list_item, parent, false);
        Context context = parent.getContext();
        return  new ViewHolder(context,view);
    }

    public void onBindViewHolder(final RecipeBoxAdapter.ViewHolder holder, final int position){
        holder.name.setText(recipedata.get(position).getName());
        holder.image.setImageResource(recipedata.get(position).getImage());
        holder.recipe_into.setOnClickListener(holder);
    }

    public int getItemCount(){
        return recipedata.size();
    }
}
