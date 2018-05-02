package com.example.aida.finalproj.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aida.finalproj.Fragments.Fragment7;
import com.example.aida.finalproj.Fragments.Fragment8;
import com.example.aida.finalproj.Fragments.Fragment9;

/**
 * Created by Aida on 23.04.2018..
 */

public class SalonViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SalonViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment7 tab1 = new Fragment7();
                return tab1;
            case 1:
                Fragment8 tab2 = new Fragment8();
                return tab2;
            case 2:
                Fragment9 tab3 = new Fragment9();
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
