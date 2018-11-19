package com.example.dldke.foodbox.PencilRecipe;


import android.support.v4.app.FragmentStatePagerAdapter;


public class PencilPagerAdapter extends FragmentStatePagerAdapter {

    public PencilPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new AllFoodListFragment();
            case 1:
                return new MeatListFragment();
            case 2:
                return new FreshListFragment();
            case 3:
                return new EtcListFragment();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "전체";
            case 1:
                return "육류/수산물";
            case 2:
                return "과일/채소";
            case 3:
                return "음료/유제품";
            default:
                return null;
        }
    }
    @Override
    public int getCount()
    {
        return 5;
    }

}


