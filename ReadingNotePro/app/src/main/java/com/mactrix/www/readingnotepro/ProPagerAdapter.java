package com.mactrix.www.readingnotepro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

/**
 * Created by Mactrix on 8/31/2018.
 */

public class ProPagerAdapter extends FragmentPagerAdapter {

    int tabNumber;


    public ProPagerAdapter(FragmentManager fm, int numberofTab) {
        super(fm);
        tabNumber = numberofTab;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment library = new LibraryFragment();
        Fragment voiceSearch = new VoiceSearchFragment();
        Fragment recent = new RecentFragment();

        switch(i){
            case 0: return library;
            case 1: return voiceSearch;
            case 2: return recent;
            default: return library;
        }

    }

    @Override
    public int getCount() {

        return tabNumber;
    }
}
