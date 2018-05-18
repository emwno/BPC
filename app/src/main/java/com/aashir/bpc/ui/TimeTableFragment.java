package com.aashir.bpc.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BaseTimeTableFragment;
import com.aashir.bpc.ui.adapter.TimeTableAdapter;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.TimeTableCard;

import java.util.ArrayList;

public class TimeTableFragment extends BaseTimeTableFragment {

    private RecyclerView mListView;
    private TimeTableAdapter mAdapter;
    private int mTabNo;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        mTabNo = getArguments().getInt("timetable_tab");

        SharedPreferences pref = getActivity().getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        final ArrayList<String> mSubjects = new ArrayList<>(pref.getStringSet(Key.Preferences.SUBJECTS, null));
        final String mClass = pref.getString(Key.Preferences.TIMETABLE, Key.Class.TIMETABLE[0]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTabNo == 1) {
                    getTimeTable(mClass, "Monday", mSubjects, true);
                } else if (mTabNo == 2) {
                    getTimeTable(mClass, "Tuesday", mSubjects, true);
                } else if (mTabNo == 3) {
                    getTimeTable(mClass, "Wednesday", mSubjects, true);
                } else if (mTabNo == 4) {
                    getTimeTable(mClass, "Thursday", mSubjects, true);
                } else if (mTabNo == 5) {
                    getTimeTable(mClass, "Friday", mSubjects, true);
                }
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, container, false);

        mAdapter = new TimeTableAdapter(new ArrayList<TimeTableCard>());

        mListView = (RecyclerView) rootView.findViewById(R.id.listview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected void onDataReceived(ArrayList<TimeTableCard> cards) {
        if (isAdded()) {
            mAdapter.redoAll(cards);
        }
    }
}