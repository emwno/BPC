package com.aashir.bpc.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.widget.CircularProgressView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DetailImageActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SubsamplingScaleImageView mImageView;
    private CircularProgressView mProgress;
    private ActionBar mActionBar;

    private boolean hidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_image_activity);

        mToolbar = (Toolbar) findViewById(R.id.detail_image_toolbar);
        mImageView = (SubsamplingScaleImageView) findViewById(R.id.imageView);
        mProgress = (CircularProgressView) findViewById(R.id.detail_image_progress);

        setActionBar();
        String id = getIntent().getExtras().getString("image_id");
        loadImageFromPin(id);
    }

    private void loadImageFromPin(final String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Class.IMAGES);
        query.fromPin(Key.Class.IMAGES + "_" + id);
        query.getInBackground(id, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    ParseFile d = obj.getParseFile(Key.Image.IMAGE);
                    initImage(d.getUrl());
                } else {
                    loadImageFromParse(id);
                }
            }

        });
    }

    private void loadImageFromParse(final String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Key.Class.IMAGES);
        query.getInBackground(id, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    obj.pinInBackground(Key.Class.IMAGES + "_" + id);
                    ParseFile d = obj.getParseFile(Key.Image.IMAGE);
                    initImage(d.getUrl());
                } else {
                    Toast.makeText(DetailImageActivity.this, "Unable to load image\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    private void initImage(final String url) {

        SimpleTarget mTarget = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                mProgress.setVisibility(View.GONE);
                mImageView.setImage(ImageSource.bitmap(bitmap));
                //bitmap.recycle();
            }

        };

        Glide.with(DetailImageActivity.this).load(url).asBitmap().into(mTarget);

        mImageView.setMinimumDpi(50);
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hidden) {
                    hidden = false;
                    mActionBar.show();
                } else {
                    hidden = true;
                    mActionBar.hide();
                }
            }

        });
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
}
