package com.aashir.bpc.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.CommentsAdapter;
import com.aashir.bpc.ui.model.CommentCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.Utils;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailPostActivity extends AppCompatActivity implements OnClickListener, OnRefreshListener {

    private Toolbar mToolbar;
    private ListView mCommentList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageButton mLike;
    private EditText mComment;

    private ArrayList<CommentCard> mComments = new ArrayList<>();

    private String mObjectID;
    private String mImageID;
    private boolean mLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_post_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCommentList = (ListView) findViewById(R.id.detail_bulletin_comment_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        initDetails();
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);

        ActionBar aB = getSupportActionBar();
        aB.setDisplayHomeAsUpEnabled(true);
        aB.setDisplayShowTitleEnabled(false);
        aB.setDisplayUseLogoEnabled(false);
        aB.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    private void initDetails() {
        LayoutInflater in = LayoutInflater.from(this);

        View header = in.inflate(R.layout.header_comments, mCommentList, false);
        TextView mContent = (TextView) header.findViewById(R.id.bulletin_content);
        ImageView mImage = (ImageView) header.findViewById(R.id.detail_bulletin_image);

        mCommentList.addHeaderView(header);

        TextView mAuthor = (TextView) header.findViewById(R.id.bulletin_author);
        TextView mTime = (TextView) header.findViewById(R.id.bulletin_time);
        ImageView mPP = (ImageView) header.findViewById(R.id.bulletin_profilepicture);
        ImageButton mPostComment = (ImageButton) findViewById(R.id.detail_comment_post);
        mComment = (EditText) findViewById(R.id.detail_comment_edittext);
        mLike = (ImageButton) findViewById(R.id.bulletin_like);

        mContent.setEllipsize(null);
        mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.MAX_VALUE)});

        Bundle mCard = this.getIntent().getExtras();

        mAuthor.setText(mCard.getString("author"));
        mContent.setText(mCard.getString("content"));
        mTime.setText(mCard.getString("time"));
        Glide.with(this).load(mCard.getString("pp")).placeholder(R.drawable.placeholder_user).into(mPP);
        boolean mHasImage = mCard.getBoolean("hasImage");
        mObjectID = mCard.getString("objectid");

        if (mHasImage) {
            String url = mCard.getString("image_thumb_url");
            mImageID = mCard.getString("image_id");
            Glide.with(this).load(url).placeholder(R.drawable.placeholder_image).centerCrop().into(mImage);
            mImage.setOnClickListener(this);
        } else {
            mImage.setVisibility(View.GONE);
        }

        mLike.setOnClickListener(this);
        mPostComment.setOnClickListener(this);

        findViewById(R.id.bulletin_comment).setOnClickListener(this);
        findViewById(R.id.bulletin_share).setOnClickListener(this);

        Resources mRes = getResources();
        if (mCard.getBoolean("like")) {
            mLike.setSelected(true);
            mLike.setBackground(ResourcesCompat.getDrawable(mRes, R.drawable.social_selected, null));
            mLike.setImageDrawable(ResourcesCompat.getDrawable(mRes, R.drawable.ic_thumb_up_white_18dp, null));
        } else {
            mLike.setSelected(false);
        }

        CommentsAdapter mAdapter = new CommentsAdapter(DetailPostActivity.this, mComments);
        mCommentList.setAdapter(mAdapter);

        String pinName = Key.Class.COMMENTS + "_" + mObjectID;
        mSwipeRefreshLayout.setRefreshing(true);
        if (Utils.gotInternet(this)) {
            loadCommentsFromParse(mObjectID, pinName);
        } else {
            loadCommentsFromPin(pinName);
        }
    }

    private void loadCommentsFromParse(String id, final String pinName) {
        mLoading = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Class.COMMENTS);
        query.whereEqualTo(Key.Comment.PARENT, id);
        query.orderByAscending(Key.Comment.TIME);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    setComments(results);

                    ParseObject.unpinAllInBackground(pinName);
                    ParseObject.pinAllInBackground(pinName, results);

                    if (mComments.size() > 0) {
                        CommentsAdapter mAdapter = new CommentsAdapter(DetailPostActivity.this, mComments);
                        mCommentList.setAdapter(mAdapter);
                    }
                } else {
                    showToast("Error Loading Comments" + "\n" + e.getMessage());
                }

                mLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    private void loadCommentsFromPin(String pinName) {
        mLoading = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Class.COMMENTS);
        query.fromPin(pinName);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    setComments(results);
                    if (mComments.size() > 0) {
                        CommentsAdapter mAdapter = new CommentsAdapter(DetailPostActivity.this, mComments);
                        mCommentList.setAdapter(mAdapter);
                    }
                } else {
                    showToast("Error Loading Comments" + "\n" + e.getMessage());
                }

                mLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    private void setComments(List<ParseObject> results) {
        mComments.clear();

        ParseObject author;
        ParseFile authimg;
        CommentCard card;
        Utils mUtils = new Utils();

        for (ParseObject country : results) {
            author = country.getParseObject(Key.Comment.AUTHOR);
            authimg = author.getParseFile(Key.User.IMAGE);
            card = new CommentCard();

            card.setAuthor(author.getString(Key.User.NAME));
            card.setContent(country.getString(Key.Comment.CONTENT));
            card.setTime(mUtils.getRelativeTime(country.getNumber(Key.Comment.TIME).longValue()));
            card.setProfilePicture(authimg.getUrl());

            mComments.add(card);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_bulletin_image:
                Intent i = new Intent(DetailPostActivity.this, DetailImageActivity.class);
                i.putExtra("image_id", mImageID);
                DetailPostActivity.this.startActivity(i);
                break;
            case R.id.bulletin_like:
                Resources mRes = getResources();
                if (mLike.isSelected()) {
                    mLike.setSelected(false);
                    mLike.setBackground(ResourcesCompat.getDrawable(mRes, R.drawable.social_unselected, null));
                    mLike.setImageDrawable(ResourcesCompat.getDrawable(mRes, R.drawable.ic_thumb_up_grey600_18dp, null));
                } else {
                    mLike.setSelected(true);
                    mLike.setBackground(ResourcesCompat.getDrawable(mRes, R.drawable.social_selected, null));
                    mLike.setImageDrawable(ResourcesCompat.getDrawable(mRes, R.drawable.ic_thumb_up_white_18dp, null));
                }
                break;
            case R.id.bulletin_comment:
                mComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mComment, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.bulletin_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getIntent().getExtras().getString("content")
                        + "\n" + "~" + getIntent().getExtras().getString("author"));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;

            case R.id.detail_comment_post:
                postComment();
                break;
        }
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
    public void onRefresh() {
        if (!mLoading) {
            String pinName = Key.Class.COMMENTS + "_" + mObjectID;
            if (Utils.gotInternet(this)) {
                loadCommentsFromParse(mObjectID, pinName);
            } else {
                loadCommentsFromPin(pinName);
            }
        }
    }

    private void postComment() {
        if (mComment.getText().length() == 0) {
            showToast("Enter a comment before posting");
            return;
        }

        ParseObject comment = new ParseObject(Key.Class.COMMENTS);
        comment.put(Key.Comment.PARENT, mObjectID);
        comment.put(Key.Comment.AUTHOR, ParseUser.getCurrentUser());
        comment.put(Key.Comment.CONTENT, mComment.getText().toString());
        comment.put(Key.Comment.TIME, System.currentTimeMillis());

        comment.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e != null) {
                    showToast("Unable to post comment" + "\n" + e.getMessage());
                } else {

                    mComment.getText().clear();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
                    mComment.clearFocus();
                    showToast("Comment posted");
                    if (!mLoading) {
                        String pinName = Key.Class.COMMENTS + "_" + mObjectID;
                        if (Utils.gotInternet(DetailPostActivity.this)) {
                            loadCommentsFromParse(mObjectID, pinName);
                        } else {
                            loadCommentsFromPin(pinName);
                        }
                    }
                }
            }

        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
