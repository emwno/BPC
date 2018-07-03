package com.aashir.bpc.generic;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aashir.bpc.ui.model.AssignmentCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAssignmentFragment extends Fragment {

    private ArrayList<AssignmentCard> mAssignments = null;

    protected abstract void onDataReceived(ArrayList<AssignmentCard> cards);

    public boolean isTeacher() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            if (user.getString("role") != null) return user.getString("role").equals("Teacher");
        }
        return false;
    }

    public void getAllAssignments(String className, boolean fromPin) {
        if (fromPin) {
            getAssignmentsFromPin(className, className + "_ALL", true, false, true);
        } else {
            getAssignmentsFromParse(className, className + "_ALL", true, false, true);
        }
    }

    public void getOnGoingAssignments(String className, boolean fromPin) {
        if (fromPin) {
            getAssignmentsFromPin(className, className + "_ONGOING", true, false, false);
        } else {
            getAssignmentsFromParse(className, className + "_ONGOING", true, false, false);
        }
    }

    public void getOverdueAssignments(String className, boolean fromPin) {
        if (fromPin) {
            getAssignmentsFromPin(className, className + "_OVERDUE", true, true, false);
        } else {
            getAssignmentsFromParse(className, className + "_OVERDUE", true, true, false);
        }
    }

    public void getCompletedAssignments(String className, boolean fromPin) {
        if (fromPin) {
            getAssignmentsFromPin(className, className + "_COMPLETED", false, false, false);
        } else {
            getAssignmentsFromParse(className, className + "_COMPLETED", false, false, false);
        }
    }

    private void getAssignmentsFromPin(final String className, final String pinName, final boolean notComplete, final boolean overdue, final boolean all) {
        if (mAssignments == null) mAssignments = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(pinName);
        query.orderByAscending(Key.Assignment.DUE_TIME);
        query.include(Key.Assignment.TEACHER);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setAssignments(obj);
                    onDataReceived(mAssignments);
                    if (Utils.gotInternet(getActivity()))
                        getAssignmentsFromParse(className, pinName, notComplete, overdue, all);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Utils.gotInternet(getActivity()))
                        getAssignmentsFromParse(className, pinName, notComplete, overdue, all);
                }

            }
        });

    }

    private void getAssignmentsFromParse(String className, final String pinName, boolean notComplete, boolean overdue, boolean all) {
        if (mAssignments == null) mAssignments = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.orderByAscending(Key.Assignment.DUE_TIME);
        if(!all) {
            if (notComplete) {
                query.whereNotEqualTo(Key.Assignment.COMPLETED_BY, ParseUser.getCurrentUser());
                if (overdue)
                    query.whereLessThanOrEqualTo(Key.Assignment.DUE_TIME, System.currentTimeMillis());
                else
                    query.whereGreaterThanOrEqualTo(Key.Assignment.DUE_TIME, System.currentTimeMillis());
            } else {
                query.whereEqualTo(Key.Assignment.COMPLETED_BY, ParseUser.getCurrentUser());
            }
        }
        query.include(Key.Assignment.TEACHER);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setAssignments(obj);
                    ParseObject.unpinAllInBackground(pinName);
                    ParseObject.pinAllInBackground(pinName, obj);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                onDataReceived(mAssignments);
            }
        });
    }

    private void setAssignments(List<ParseObject> obj) {
        mAssignments.clear();

        AssignmentCard card;
        ParseObject teacher;

        Utils mUtils = new Utils();

        for (ParseObject country : obj) {
            card = new AssignmentCard();
            teacher = country.getParseObject(Key.Assignment.TEACHER);

            card.setObjectID(country.getObjectId());
            card.setSubject(country.getString(Key.Assignment.SUBJECT));
            card.setTitle(country.getString(Key.Assignment.TITLE));
            card.setContent(country.getString(Key.Assignment.CONTENT));
            card.setTeacher(teacher.getString(Key.Teacher.NAME));
            card.setPostTime(Utils.getRelativeTimeLong(country.getNumber(Key.Assignment.POST_TIME).longValue()));
            card.setDueTime(Utils.getRelativeTimeLong(country.getNumber(Key.Assignment.DUE_TIME).longValue()));
            long time = country.getNumber(Key.Assignment.DUE_TIME).longValue() - System.currentTimeMillis();
            if (time < 0) time = time * -1;
            card.setTime(mUtils.getAssignmentTime(time));
            mAssignments.add(card);
        }
    }

}
