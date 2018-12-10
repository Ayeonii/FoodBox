package com.example.dldke.foodbox.FullRecipe;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dldke.foodbox.CloudVision.PermissionUtils;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxHalfRecipeDetailActivity;
import com.example.dldke.foodbox.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.example.dldke.foodbox.CloudVision.VisionActivity.FILE_NAME;

public class FullRecipeAdapter extends RecyclerView.Adapter<FullRecipeAdapter.FullRecipeViewHolder> {

    private ArrayList<FullRecipeData> mList;
    private Context mContext;
    private List<RecipeDO.Ingredient> data;
    private RecipeBoxHalfRecipeDetailActivity recipeBoxHalfRecipeDetailActivity = new RecipeBoxHalfRecipeDetailActivity();
    private String recipeId = recipeBoxHalfRecipeDetailActivity.getRecipeId();

    private List<RecipeDO.Ingredient> ingredients = new ArrayList<>();
    private FullRecipeStepAdapter adapter;
    private List<String> items = new ArrayList<>();
    private List<RecipeDO.Ingredient> specIngredient = new ArrayList<>();
    private List<RecipeDO.Spec> specList = new ArrayList<>();
    private Dialog dialog;

    public FullRecipeAdapter(Context context, ArrayList<FullRecipeData> list) {
        mList = list;
        mContext = context;
    }

    public class FullRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        protected TextView StepDetail;

        public FullRecipeViewHolder(View view) {
            super(view);
            this.StepDetail = (TextView) view.findViewById(R.id.full_recipe_step_detail);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
            data = Mapper.searchRecipe(recipeId).getIngredient();
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:

                        dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.fullrecipe_step_dialog);
                        RecyclerView ingredient_view = (RecyclerView) dialog.findViewById(R.id.check_recycler_view);
                        final Button button_submit = (Button) dialog.findViewById(R.id.done_btn);
                        final Button button_cancel = (Button) dialog.findViewById(R.id.cancel_btn);
                        final Spinner method = (Spinner) dialog.findViewById(R.id.method_spinner);
                        final Spinner minute = (Spinner) dialog.findViewById(R.id.minute_spinner);
                        final Spinner fire = (Spinner) dialog.findViewById(R.id.fire_spinner);

                        ingredients = Mapper.searchRecipe(recipeId).getIngredient();
                        ingredient_view.setHasFixedSize(true);
                        ingredient_view.setLayoutManager(new LinearLayoutManager(mContext));
                        adapter = new FullRecipeStepAdapter(ingredients);
                        ingredient_view.setAdapter(adapter);

                        //방법, 시간, 불세기 spinner
                        String[] methodStr = mContext.getResources().getStringArray(R.array.MethodSpinner);
                        ArrayAdapter<String> madapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, methodStr);
                        method.setAdapter(madapter);

                        String[] minuteStr = mContext.getResources().getStringArray(R.array.MinuteSpinner);
                        ArrayAdapter<String> minadapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, minuteStr);
                        minute.setAdapter(minadapter);

                        String[] fireStr = mContext.getResources().getStringArray(R.array.FireSpinner);
                        ArrayAdapter<String> fireadapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, fireStr);
                        fire.setAdapter(fireadapter);


                        button_submit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                String step_description="";
                                String method_st = method.getSelectedItem().toString();
                                Integer minute_int = Integer.parseInt(minute.getSelectedItem().toString());
                                String fire_st = fire.getSelectedItem().toString();
                                items = adapter.getTempItems();

                                String ingredient="";
                                ingredient = items.get(0);
                                for(int i = 1; i<items.size(); i++){
                                    ingredient = ingredient+","+items.get(i);
                                    for(int j = 0; j<ingredients.size(); j++){
                                        if(items.get(i).equals(ingredients.get(j).getIngredientName()))
                                            specIngredient.add(ingredients.get(j));
                                    }
                                }
                                if(minute_int.equals(0) || fire_st.equals("없음")){
                                    step_description = ingredient+"을/를 \r\n"+method_st;
                                }
                                else{
                                    step_description = ingredient+"을/를 \r\n"+minute_int+"분 동안 \r\n"+method_st+" (불 세기: "+fire_st+")";
                                }

                                RecipeDO.Spec spec = Mapper.createSpec(specIngredient, method_st, fire_st, minute_int);

                                specList.add(spec);

                                Log.e(TAG, ""+step_description);

                                FullRecipeData data = new FullRecipeData(step_description);
                                mList.set(getAdapterPosition(), data);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;

                    case 1002:
                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());
                        break;
                }
                return true;
            }
        };
    }


    @Override
    public FullRecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fullrecipe_step_list_item, viewGroup, false);
        FullRecipeViewHolder viewHolder = new FullRecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FullRecipeViewHolder viewholder, int position) {
        viewholder.StepDetail.setText(mList.get(position).getStepDescription());
        viewholder.StepDetail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewholder.StepDetail.setGravity(Gravity.CENTER);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}