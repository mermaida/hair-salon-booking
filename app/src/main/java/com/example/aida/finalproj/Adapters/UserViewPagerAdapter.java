package com.example.aida.finalproj.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aida.finalproj.Fragments.Fragment2;
import com.example.aida.finalproj.Fragments.Fragment4;
import com.example.aida.finalproj.Fragments.Fragment5;
import com.example.aida.finalproj.Fragments.Fragment6;

/**
 * Created by Aida on 23.04.2018..
 */

public class UserViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public UserViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment4 tab1 = new Fragment4();
                return tab1;
            case 1:
                Fragment5 tab2 = new Fragment5();
                return tab2;
            case 2:
                Fragment6 tab3 = new Fragment6();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
