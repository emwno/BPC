package com.aashir.bpc.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.AttendeesAdapter;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.UserCard;
import com.aashir.bpc.ui.widget.CircularProgressView;
import com.aashir.bpc.ui.widget.MaterialDialog;
import com.aashir.bpc.util.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Toolbar mToolbar;
    private GoogleMap mMap;

    private Bundle mData;
    private ArrayList<UserCard> mAttendees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_event_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        ImageView mImage = (ImageView) findViewById(R.id.detail_event_image);
        TextView mContent = (TextView) findViewById(R.id.detail_event_content);
        TextView mTime = (TextView) findViewById(R.id.detail_event_time);
        TextView mAddress = (TextView) findViewById(R.id.location_address);
        TextView mVenue = (TextView) findViewById(R.id.detail_event_venue);
        TextView mPhone = (TextView) findViewById(R.id.detail_event_phone);

        setActionBar();

        mData = getIntent().getExtras();

        Glide.with(this).load(mData.getString("image")).into(mImage);
        mCollapsingToolbar.setTitle(mData.getString("title"));
        mContent.setText(mData.getString("content"));
        mTime.setText(mData.getString("time"));
        mAddress.setText(mData.getString("address"));
        mVenue.setText(mData.getString("venue"));
        mPhone.setText(mData.getString("phone"));

        mPhone.setOnClickListener(this);
        findViewById(R.id.event_fab).setOnClickListener(this);
        findViewById(R.id.k).setOnClickListener(this);
        ((ViewStub) findViewById(R.id.map_stub)).inflate();

        setUpMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.clear();
        mData = null;
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        /*if (mMap != null) {
            LatLng lat = new LatLng(mData.getDouble("latitude"), mData.getDouble("longitude"));
            Marker marker = mMap.addMarker(new MarkerOptions().position(lat).title(mData.getString("venue")));
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
        }*/
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_event_phone:
                try {
                    String s = mData.getString("phone").replaceAll("\\D+", "");
                    long phone = Long.parseLong(s);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + 0 + phone));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Can't make a call", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.k:
                MaterialDialog j = new MaterialDialog(this);
                j.show();
                getEvent(j.getView());
                break;

            case R.id.event_fab:
                if (Utils.gotInternet(this)) getEvent(null);
                break;
        }
    }

    private void getEvent(final View v) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Class.EVENT);
        query.getInBackground(mData.getString("object_id"), new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    if (v == null) {
                        isAttending(obj);
                    } else {
                        getAttendees(v, obj);
                    }
                } else {
                    showToast("An Error Occurred\n" + e.getMessage());
                }
            }

        });
    }

    private void isAttending(final ParseObject object) {
        final ParseRelation<ParseObject> mRelation = object.getRelation(Key.Event.ATTENDEES);
        mRelation.getQuery().whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId())
                .getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            showToast("Not Attending Event");
                            mRelation.remove(ParseUser.getCurrentUser());
                        } else {
                            showToast("Attending Event");
                            mRelation.add(ParseUser.getCurrentUser());
                        }
                        object.saveEventually();
                    }
                });
    }

    private void getAttendees(View v, ParseObject object) {
        final ParseRelation<ParseObject> mRelation = object.getRelation(Key.Event.ATTENDEES);
        final CircularProgressView progress = (CircularProgressView) v.findViewById(R.id.event_attendees_progress);
        final ListView lists = (ListView) v.findViewById(R.id.event_attendees_list);

        mRelation.getQuery().findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    progress.setVisibility(View.GONE);
                    setAttendees(list);
                    AttendeesAdapter adapter = new AttendeesAdapter(DetailEventActivity.this, mAttendees);
                    lists.setAdapter(adapter);
                } else {
                    showToast("An Error Occurred\n" + e.getMessage());
                }
            }

        });
    }

    private void setAttendees(List<ParseObject> results) {
        mAttendees.clear();

        ParseFile authimg;
        UserCard card;

        for (ParseObject country : results) {
            card = new UserCard();
            authimg = country.getParseFile(Key.User.IMAGE);

            card.setName(country.getString(Key.User.NAME));
            card.setProfilePicture(authimg.getUrl());

            mAttendees.add(card);
        }
    }

    private void showToast(String message) {
        Toast.makeText(DetailEventActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng lat = new LatLng(mData.getDouble("latitude"), mData.getDouble("longitude"));
        Marker marker = googleMap.addMarker(new MarkerOptions().position(lat).title(mData.getString("venue")));
        marker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
    }
}