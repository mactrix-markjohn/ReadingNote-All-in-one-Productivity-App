package com.mactrix.www.readingnotepro;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mactrix on 4/26/2018.
 */

public class TabAdapter extends FragmentPagerAdapter {

    int tab;

    public TabAdapter(FragmentManager fm,int numberOfTab) {
        super(fm);
        tab = numberOfTab;


    }

    @Override
    public Fragment getItem(int position) {

        Fragment audiostore = new AudioStore();
        Fragment videostore = new VideoStore();

        switch (position){

            case 0: return audiostore;
            case 1: return  videostore;
            default: return audiostore;

        }


       // return null;
    }

    @Override
    public int getCount() {
        return tab;
    }
}
