package com.example.dldke.foodbox.FullRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import com.example.dldke.foodbox.PencilRecipe.PencilCartAdapter;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeStepAdapter extends RecyclerView.Adapter<FullRecipeStepAdapter.ViewHolder> {
    private static List<RecipeDO .Ingredient> ingredients = new ArrayList<>();
    private List<FullRecipeIngredientData> ingredientData = new ArrayList<>();
    private ArrayList<PencilCartItem> clickItems;
    final List<String> SelectedItems  = new ArrayList();
    final List<String> tempItems = new ArrayList<>();

    private FullRecipeActivity fullRecipeActivity = new FullRecipeActivity();
    private boolean isChecked;
    private boolean isHalfRecipe = fullRecipeActivity.getIsHalfRecipe();

    public boolean getChecked(){
        return isChecked;
    }

    public void setChecked(boolean checked){
        this.isChecked = checked;
    }

    String TAG = "FullRecipeStepAdapter";


    public FullRecipeStepAdapter(List<RecipeDO.Ingredient> ingredientList){
        this.ingredients = ingredientList;
    }

    public FullRecipeStepAdapter(ArrayList<PencilCartItem> items){
        this.clickItems = items;
    }

    public List<String> getTempItems(){
        return tempItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox1;
        public TextView textView1;

        public ViewHolder(View view){
            super(view);
            checkBox1 = (CheckBox) view.findViewById(R.id.checkbox);
            textView1 = (TextView) view.findViewById(R.id.ingredient_text);

            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(isHalfRecipe){
                        if(b){
                            checkBox1.setChecked(b);
                            tempItems.add(ingredients.get(getAdapterPosition()).getIngredientName());
                        }
                        else {
                            checkBox1.setChecked(b);
                            tempItems.remove(ingredients.get(getAdapterPosition()).getIngredientName());
                        }
                        Log.e(TAG, "체크된 재료들!!! "+tempItems);
                    }
                    else{
                        if(b){
                            checkBox1.setChecked(b);
                            tempItems.add(clickItems.get(getAdapterPosition()).getFoodName());
                        }
                        else{
                            checkBox1.setChecked(b);
                            tempItems.remove(clickItems.get(getAdapterPosition()).getFoodName());
                        }
                        Log.e(TAG, "체크된 재료들 "+tempItems);
                    }

                }
            });
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fullrecipe_step_dialog_ingredient, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name;
        if(isHalfRecipe) {
            name = ingredients.get(position).getIngredientName();
        }
        else{
            name = clickItems.get(position).getFoodName();
        }
        holder.textView1.setText(name);
        holder.checkBox1.setChecked(false);


    }

    public int getItemCount(){
        if(isHalfRecipe){
            return ingredients.size();
        }
        else{
            return clickItems.size();
        }

    }
}
