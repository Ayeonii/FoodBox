package com.example.dldke.foodbox.MyRecipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RecipeBoxPagerAdapter extends FragmentStatePagerAdapter {
    private int fragmentCount;

    public RecipeBoxPagerAdapter(FragmentManager fm, int fragCount){
        super(fm);
        this.fragmentCount = fragCount;
    }
    public Fragment getItem(int position){
        switch(position){
            case 0:
                HalfRecipeBoxFragment half_recipe_box_frag = new HalfRecipeBoxFragment();
                return half_recipe_box_frag;
            case 1:
                FullRecipeBoxFragment full_recipe_box_frg = new FullRecipeBoxFragment();
                return full_recipe_box_frg;

            default:
                return null;
        }
    }

    public int getCount(){
        return fragmentCount;
    }
}
