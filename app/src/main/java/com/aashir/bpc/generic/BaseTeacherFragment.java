package com.aashir.bpc.generic;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.TeacherCard;
import com.aashir.bpc.util.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTeacherFragment extends Fragment {

    private ArrayList<TeacherCard> mTeachers = null;

    protected abstract void onDataReceived(ArrayList<TeacherCard> cards);

    public void getTeachers(String filter) {
        if (filter == null) {
            getTeachersFromPin(Key.Class.TEACHERS);
        } else {
            searchTeachers(Key.Class.TEACHERS, filter);
        }
    }

    private void searchTeachers(String className, String filter) {
        if (mTeachers == null) mTeachers = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(className);
        query.whereMatches(Key.Teacher.NAME, "(" + filter + ")", "i");
        query.orderByAscending(Key.Teacher.NAME);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setTeachers(obj);
                    if(mTeachers.size() == 0) mTeachers.add(new TeacherCard());
                    onDataReceived(mTeachers);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTeachersFromPin(final String className) {
        if (mTeachers == null) mTeachers = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(className);
        query.orderByAscending(Key.Teacher.NAME);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setTeachers(obj);
                    onDataReceived(mTeachers);
                    if (Utils.gotInternet(getActivity()))
                        getTeachersFromParse(className);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Utils.gotInternet(getActivity()))
                        getTeachersFromParse(className);
                }
            }
        });
    }

    private void getTeachersFromParse(final String className) {
        if (mTeachers == null) mTeachers = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.orderByAscending(Key.Teacher.NAME);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setTeachers(obj);
                    ParseObject.unpinAllInBackground(className);
                    ParseObject.pinAllInBackground(className, obj);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                onDataReceived(mTeachers);
            }
        });
    }

    private void setTeachers(List<ParseObject> obj) {
        mTeachers.clear();

        TeacherCard card;
        ParseFile image;

        for (ParseObject country : obj) {
            card = new TeacherCard();
            image = country.getParseFile(Key.Teacher.IMAGE);

            card.setName(country.getString(Key.Teacher.NAME));
            card.setProfilePicture(image.getUrl());
            card.setSubject(country.getString(Key.Teacher.SUBJECT));
            card.setPhone(country.getString(Key.Teacher.PHONE));
            card.setEmail(country.getString(Key.Teacher.EMAIL));

            mTeachers.add(card);
        }
    }
}