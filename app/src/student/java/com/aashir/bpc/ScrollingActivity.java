package com.aashir.bpc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.aashir.bpc.ui.adapter.HomeAdapter;
import com.aashir.bpc.ui.adapter.PostAdapter;
import com.aashir.bpc.ui.model.AssignmentCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.PostCard;
import com.aashir.bpc.util.Utils;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    private ArrayList<Object> mB = null;
    private ArrayList<Object> mA = null;
    int b = 0, a = 0;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mB = new ArrayList<>(6);
        mA = new ArrayList<>(6);

        mB.add(0, "BULLETIN");
        mA.add(0, "ASSIGNMENTS");

        mAdapter = new HomeAdapter(Glide.with(this), this, new ArrayList<>());
        RecyclerView mListView = (RecyclerView) findViewById(R.id.listview);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);

        ParseQuery<ParseObject> query = new ParseQuery<>(Key.Class.BULLETIN);
        query.orderByDescending(Key.Post.TIME);
        query.setLimit(5);
        query.include(Key.Post.AUTHOR);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setPosts(obj);
                    mAdapter.redoAll(mB, 1, b);
                    geta();
                } else {
                }
            }
        });
    }

    private void geta() {
        ParseQuery<ParseObject> query1 = new ParseQuery<>(Key.Class.ASSIGNMENT[0]);
        query1.orderByAscending(Key.Assignment.DUE_TIME);
        query1.setLimit(5);
        query1.whereLessThanOrEqualTo(Key.Assignment.DUE_TIME, System.currentTimeMillis());

        query1.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setAssignments(obj);
                    mAdapter.redoAll(mA, 7, a);
                }
            }
        });
    }

    private void setAssignments(List<ParseObject> obj) {

        AssignmentCard card;

        Utils mUtils = new Utils();

        for (ParseObject country : obj) {
            card = new AssignmentCard();

            card.setObjectID(country.getObjectId());
            card.setSubject(country.getString(Key.Assignment.SUBJECT));
            card.setTitle(country.getString(Key.Assignment.TITLE));
            card.setContent(country.getString(Key.Assignment.CONTENT));
            card.setPostTime(Utils.getRelativeTimeLong(country.getNumber(Key.Assignment.POST_TIME).longValue()));
            card.setDueTime(Utils.getRelativeTimeLong(country.getNumber(Key.Assignment.DUE_TIME).longValue()));
            long time = country.getNumber(Key.Assignment.DUE_TIME).longValue() - System.currentTimeMillis();
            if (time < 0) time = time * -1;
            card.setTime(mUtils.getAssignmentTime(time));
            mA.add(a + 1, card);
        }
    }

    private void setPosts(List<ParseObject> obj) {
        // ParseUser mUser = ParseUser.getCurrentUser();

        ParseObject author;
        ParseFile authimg;
        ParseFile imagethumb;
        PostCard card;
        Utils mUtils = new Utils();

        for (ParseObject country : obj) {
            author = country.getParseObject(Key.Post.AUTHOR);
            authimg = author.getParseFile(Key.User.IMAGE);
            imagethumb = country.getParseFile(Key.Post.IMAGE_THUMB);

            card = new PostCard();
            card.setObjectID(country.getObjectId());
            card.setAuthor(author.getString(Key.User.NAME));
            card.setContent(country.getString(Key.Post.CONTENT));
            card.setTime(mUtils.getRelativeTime(country.getNumber(Key.Post.TIME).longValue()));
            card.setProfilePicture(authimg.getUrl());

            //     if(mUser != null) if (country.getList("likedBy").contains(mUser.getUsername())) card.setLike(true);

            if (imagethumb != null) {
                card.setImageThumb(imagethumb.getUrl());
                card.setImageID(country.getString(Key.Post.IMAGE));
                card.setHasImage(true);
            } else {
                card.setHasImage(false);
            }

            mB.add(b + 1, card);
        }
    }
}
