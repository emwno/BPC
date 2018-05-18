package com.aashir.bpc.generic;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.PostCard;
import com.aashir.bpc.util.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePostFragment extends Fragment {

    private ArrayList<PostCard> mFeed = null;

    protected abstract void onDataReceived(ArrayList<PostCard> cards);

    public boolean isAdmin() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            if (user.getString("role") != null) return user.getString("role").equals("Admin");
        }
        return false;
    }

    public void getBulletinPosts(boolean fromPin) {
        if (fromPin) {
            getPostsFromPin(Key.Class.BULLETIN);
        } else {
            getPostsFromParse(Key.Class.BULLETIN);
        }
    }

    public void getSocietyPosts(String className, boolean fromPin) {
        if (fromPin) {
            getPostsFromPin(className);
        } else {
            getPostsFromParse(className);
        }
    }

    private void getPostsFromPin(final String className) {
        if (mFeed == null) mFeed = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.fromPin(className);
        query.orderByDescending(Key.Post.TIME);
        query.include(Key.Post.AUTHOR);

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setPosts(obj);
                    onDataReceived(mFeed);
                    if (Utils.gotInternet(getActivity()))
                        getPostsFromParse(className);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    if (Utils.gotInternet(getActivity()))
                        getPostsFromParse(className);
                }
            }
        });
    }

    private void getPostsFromParse(final String className) {
        if (mFeed == null) mFeed = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<>(className);
        query.orderByDescending(Key.Post.TIME);
        query.include(Key.Post.AUTHOR);
        //  query.include("likedBy");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> obj, ParseException e) {
                if (e == null) {
                    setPosts(obj);
                    ParseObject.unpinAllInBackground(className);
                    ParseObject.pinAllInBackground(className, obj);
                } else {
                    if (isAdded())
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                onDataReceived(mFeed);
            }
        });
    }

    private void setPosts(List<ParseObject> obj) {
        mFeed.clear();
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

            mFeed.add(card);
        }
    }
}
