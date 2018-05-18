package com.aashir.bpc.generic;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aashir.bpc.ui.model.EventCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEventFragment extends Fragment {

    private ArrayList<EventCard> mEvents = null;

    protected abstract void onDataReceived(ArrayList<EventCard> cards);

    public void getEvents(boolean fromPin) {
        if (fromPin) {
            getEventsFromPin(Key.Class.EVENT);
        } else {
            getEventsFromParse(Key.Class.EVENT);
        }
    }

    private void getEventsFromPin(final String className) {
        if (mEvents == null) mEvents = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(className);
        query.orderByDescending(Key.Post.TIME);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setEvents(obj);
                    onDataReceived(mEvents);
                    if (Utils.gotInternet(getActivity()))
                        getEventsFromParse(className);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Utils.gotInternet(getActivity()))
                        getEventsFromParse(className);
                }
            }
        });
    }

    private void getEventsFromParse(final String className) {
        if (mEvents == null) mEvents = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.orderByDescending(Key.Post.TIME);
        query.include(Key.Post.AUTHOR);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setEvents(obj);
                    ParseObject.unpinAllInBackground(className);
                    ParseObject.pinAllInBackground(className, obj);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                onDataReceived(mEvents);
            }
        });
    }

    private void setEvents(List<ParseObject> obj) {
        mEvents.clear();

        EventCard card;
        ParseFile image;

        for (ParseObject country : obj) {
            card = new EventCard();
            image = country.getParseFile(Key.Event.IMAGE);

            card.setObjectID(country.getObjectId());
            card.setTitle(country.getString(Key.Event.TITLE));
            card.setContent(country.getString(Key.Event.CONTENT));
            card.setTime(Utils.getRelativeTimeLong(country.getNumber(Key.Event.TIME).longValue()));
            card.setImage(image.getUrl());
            card.setVenue(country.getString(Key.Event.VENUE));
            card.setPhone(country.getString((Key.Event.PHONE)));
            card.setAddress(country.getString(Key.Event.ADDRESS));

            ParseGeoPoint point = country.getParseGeoPoint(Key.Event.LOCATION);
            card.setLatitude(point.getLatitude());
            card.setLongitude(point.getLongitude());

            mEvents.add(card);
        }
    }
}
