package com.example.dldke.foodbox.Memo;

import android.support.v4.app.FragmentStatePagerAdapter;

public class MemoPagerAdapter extends FragmentStatePagerAdapter {
    public MemoPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new MemoFragmentUrgent();
            case 1:
                return new MemoFragmentToBuy();
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
                return "유통기한 임박";
            case 1:
                return "장보기 목록";
            default:
                return null;
        }
    }
    @Override
    public int getCount()
    {
        return 2;
    }

}
