package com.aashir.bpc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.PagerAdapter;

import java.util.Calendar;

public class PagerFragment extends Fragment {

    private String[] mAssTitles = {"ON GOING", "OVERDUE", "COMPLETE"};
    private String[] mTtTitles = {"M", "T", "W", "T", "F"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pager, container, false);

        ViewPager mPager = (ViewPager) rootView.findViewById(R.id.assignment_pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.assignment_tabs);

        PagerAdapter mAdapter;
        if (getArguments().getInt("which") == 2) {
            mAdapter = new PagerAdapter(getChildFragmentManager(), 2, mAssTitles);
            mPager.setAdapter(mAdapter);
            tabLayout.setupWithViewPager(mPager);
        } else {
            mAdapter = new PagerAdapter(getChildFragmentManager(), 3, mTtTitles);
            mPager.setAdapter(mAdapter);
            mPager.setOffscreenPageLimit(2);
            int w = whatDayIsIt();
            mPager.setCurrentItem(w);
            tabLayout.setupWithViewPager(mPager);
            tabLayout.setScrollPosition(w, 0, true);
        }

        return rootView;
    }

    private int whatDayIsIt() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
            case Calendar.SUNDAY:
            case Calendar.SATURDAY:
                return 0;

            case Calendar.TUESDAY:
                return 1;

            case Calendar.WEDNESDAY:
                return 2;

            case Calendar.THURSDAY:
                return 3;

            case Calendar.FRIDAY:
                return 4;

            default:
                return 0;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 777) {
            int tab = data.getIntExtra("tab_no", -1);
            if (tab >= 0) {
                getChildFragmentManager().getFragments().get(tab).onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}