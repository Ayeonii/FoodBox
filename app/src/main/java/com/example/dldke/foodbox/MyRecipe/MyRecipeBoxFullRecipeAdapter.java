package com.example.dldke.foodbox.MyRecipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.PostDO;
import com.example.dldke.foodbox.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyRecipeBoxFullRecipeAdapter extends RecyclerView.Adapter<MyRecipeBoxFullRecipeAdapter.ViewHolder>{

    private String TAG = "MyRecipeBoxFullRecipeAdapter";
    private ArrayList<RecipeBoxData> recipedata = new ArrayList<>();
    private static String recipe_id;
    private static boolean shared;

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
        String name = recipedata.get(position).getFoodname();
        shared = recipedata.get(position).isShared();

        holder.name.setText(name);
        new DownloadImageTask(holder.image).execute(recipedata.get(position).food_image);

        //holder.image.setImageResource(recipedata.get(position).getImage());
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


    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlImg =urls[0];
            Bitmap foodImg = null;
            try {
                InputStream in = new java.net.URL(urlImg).openStream();
                foodImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodImg;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void removeItem(int position) {
        String recipe_id = recipedata.get(position).getRecipeId();

        recipedata.remove(position);
        notifyItemRemoved(position);
        Mapper.deleteRecipe(recipe_id);

        if(shared){
            String userId=Mapper.getUserId();
            List<PostDO> post =Mapper.searchPost("writer", userId);
            String posting_recipe_id=post.get(position).getRecipeId();
            String post_id = post.get(position).getPostId();

            if(recipe_id.equals(posting_recipe_id))
                Mapper.deletePost(post_id);
        }

    }

}