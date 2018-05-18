package com.aashir.bpc.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aashir.bpc.ui.AssignmentFragment;
import com.aashir.bpc.ui.TimeTableFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mWhich;
    private String[] mTitles;

    public PagerAdapter(FragmentManager fm, int which, String[] titles) {
        super(fm);
        mWhich = which;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {

        if (mWhich == 2) {
            return getAssignmentFragment(position);
        } else {
            return getTimeTableFragment(position);
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    private Fragment getAssignmentFragment(int position) {
        Bundle b = new Bundle();
        AssignmentFragment tab = new AssignmentFragment();

        if (position == 0) {
            b.putInt("assignment_tab", 0);
        } else if (position == 1) {
            b.putInt("assignment_tab", 1);
        } else {
            b.putInt("assignment_tab", 2);
        }

        tab.setArguments(b);

        return tab;
    }

    private Fragment getTimeTableFragment(int position) {
        Bundle b = new Bundle();
        TimeTableFragment tab = new TimeTableFragment();

        if (position == 0) {
            b.putInt("timetable_tab", 1);
        } else if (position == 1) {
            b.putInt("timetable_tab", 2);
        } else if (position == 2) {
            b.putInt("timetable_tab", 3);
        } else if (position == 3) {
            b.putInt("timetable_tab", 4);
        } else {
            b.putInt("timetable_tab", 5);
        }

        tab.setArguments(b);

        return tab;
    }

}