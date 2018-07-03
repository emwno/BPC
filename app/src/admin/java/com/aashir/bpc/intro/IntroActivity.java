package com.aashir.bpc.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aashir.bpc.MainActivity;
import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.widget.PagingViewPager;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, IntroInterface {

    private PagingViewPager mPager;
    private List<ImageView> mDots;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        ImageView mNext = (ImageView) findViewById(R.id.next);
        mNext.setOnClickListener(this);

        mDots = new ArrayList<>();
        mDots.add((ImageView) findViewById(R.id.indicator_zero));
        mDots.add((ImageView) findViewById(R.id.indicator_one));
        mDots.add((ImageView) findViewById(R.id.indicator_two));

        IntroPagerAdapter mPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
        mPager = (PagingViewPager) findViewById(R.id.view_pager);
        mPager.setPagingEnabled(false);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPager.removeOnPageChangeListener(this);
    }

    private void nextPage() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
    }

    private void goHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        int position = mPager.getCurrentItem();
        if (position == 0) nextPage();
        if (position == 1)
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("login").putExtra("error", 0));
        if (position == 2)
            goHome();
    }

    @Override
    public void onLogin(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if (isTeacher(user)) {
                        Toast.makeText(IntroActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                        if (mPager.getCurrentItem() == 1) mPager.setCurrentItem(2, true);
                        SharedPreferences p = IntroActivity.this.getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = p.edit();
                        edit.putString(Key.Preferences.SOCIETY, user.getString("society"));
                        edit.putString(Key.Preferences.SUBJECTS, user.getString("subject"));
                        edit.apply();
                        subscribe(Key.Class.BULLETIN);
                    } else {
                        ParseUser.logOutInBackground();
                        Toast.makeText(IntroActivity.this, "You're a student, download the other app", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LocalBroadcastManager.getInstance(IntroActivity.this).sendBroadcast(new Intent("login").putExtra("error", 1));
                    Toast.makeText(IntroActivity.this, "Unable to Login\nCheck credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isTeacher(ParseUser user) {
        return user.getString("role") != null;
    }

    private void subscribe(String... all) {
        for (String each : all) {
            ParsePush.subscribeInBackground(each);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPosition(position);
    }

    public void selectPosition(int index) {
        int drawableId;
        for (int i = 0; i < 3; i++) {
            drawableId = (i == index) ? (R.drawable.indicator_dot_white) : (R.drawable.indicator_dot_grey);
            mDots.get(i).setImageDrawable(getDrawableK(drawableId));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private Drawable getDrawableK(int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return this.getDrawable(drawableId);
        return this.getResources().getDrawable(drawableId);
    }
}