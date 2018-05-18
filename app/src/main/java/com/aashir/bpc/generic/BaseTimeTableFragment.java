package com.aashir.bpc.generic;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.TimeTableCard;
import com.aashir.bpc.util.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTimeTableFragment extends Fragment {

    private ArrayList<TimeTableCard> mTimeTable = null;

    protected abstract void onDataReceived(ArrayList<TimeTableCard> cards);

    public void getTimeTable(String className, String day, ArrayList<String> subjects, boolean fromPin) {
        if (fromPin) {
            getTimeTableFromPin(className, className + "_" + day, day, subjects);
        } else {
            getTimeTableFromParse(className, className + "_" + day, day, subjects);
        }
    }

    private void getTimeTableFromPin(final String className, final String pinName, final String day, final ArrayList<String> subjects) {
        if (mTimeTable == null) mTimeTable = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(pinName);
        query.orderByAscending(Key.TimeTable.FROM_TIME);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setTimeTable(obj);
                    onDataReceived(mTimeTable);
                    if (Utils.gotInternet(getActivity()))
                        getTimeTableFromParse(className, pinName, day, subjects);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Utils.gotInternet(getActivity()))
                        getTimeTableFromParse(className, pinName, day, subjects);
                }

            }
        });
    }

    public void getTimeTableFromParse(final String className, final String pinName, final String day, final ArrayList<String> subjects) {
        if (mTimeTable == null) mTimeTable = new ArrayList<>();

        ArrayList<String> days = new ArrayList<>();
        days.add(day);
        days.add("Week");

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.whereContainedIn(Key.TimeTable.ON_DAY, days);
        query.whereContainedIn(Key.TimeTable.SUBJECT, subjects);
        query.orderByAscending(Key.TimeTable.FROM_TIME);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setTimeTable(obj);
                    ParseObject.unpinAllInBackground(pinName);
                    ParseObject.pinAllInBackground(pinName, obj);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                onDataReceived(mTimeTable);
            }
        });
    }

    private void setTimeTable(List<ParseObject> obj) {
        mTimeTable.clear();

        TimeTableCard card;

        for (ParseObject country : obj) {
            card = new TimeTableCard();
            card.setSubject(country.getString(Key.Assignment.SUBJECT));
            card.setTime(country.getString(Key.TimeTable.FROM_TIME) + " - " + country.getString(Key.TimeTable.TO_TIME));
            mTimeTable.add(card);
        }
    }

}
