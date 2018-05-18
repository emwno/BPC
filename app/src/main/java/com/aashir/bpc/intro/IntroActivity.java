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

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.widget.PagingViewPager;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, IntroInterface {

    private IntroPagerAdapter mPagerAdapter;
    private PagingViewPager mPager;
    private boolean showDone = true;

    private List<ImageView> mDots;
    private ImageView mNext;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        mNext = (ImageView) findViewById(R.id.next);
        mNext.setOnClickListener(this);

        mDots = new ArrayList<>();
        mDots.add((ImageView) findViewById(R.id.indicator_zero));
        mDots.add((ImageView) findViewById(R.id.indicator_one));
        mDots.add((ImageView) findViewById(R.id.indicator_two));
        mDots.add((ImageView) findViewById(R.id.indicator_three));
        mDots.add((ImageView) findViewById(R.id.indicator_four));
        mDots.add((ImageView) findViewById(R.id.indicator_five));

        mPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
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
        Intent i = new Intent(this, com.aashir.bpc.MainActivity.class);
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
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("class"));
        if (position == 3)
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("society"));
        if (position == 4)
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("subject"));
        if (position == 5)
            goHome();
    }

    @Override
    public void onLogin(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(IntroActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    if(mPager.getCurrentItem() == 1) mPager.setCurrentItem(2, true);
                } else {
                    LocalBroadcastManager.getInstance(IntroActivity.this).sendBroadcast(new Intent("login").putExtra("error", 1));
                    Toast.makeText(IntroActivity.this, "Unable to Login\nCheck credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClassPick(int aclass) {
        SharedPreferences p = this.getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = p.edit();

        edit.putString(Key.Preferences.ASSIGNMENT, Key.Class.ASSIGNMENT[aclass]);
        edit.putString(Key.Preferences.TIMETABLE, Key.Class.TIMETABLE[aclass]);
        edit.apply();

        subscribe(Key.Class.ASSIGNMENT[aclass], Key.Class.TIMETABLE[aclass], Key.Class.BULLETIN);
        nextPage();
    }

    private void subscribe(String... all) {
        for (String each : all) {
            ParsePush.subscribeInBackground(each);
        }
    }

    @Override
    public void onSocietyPick(String society) {
        SharedPreferences p = this.getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        p.edit().putString(Key.Preferences.SOCIETY, society).apply();
        subscribe(society);
        nextPage();
    }

    @Override
    public void onSubjectPick(Set<String> subjects) {
        SharedPreferences p = this.getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        p.edit().putStringSet(Key.Preferences.SUBJECTS, subjects).apply();
        nextPage();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPosition(position);
        if (position == 5) mNext.setImageDrawable(getDrawableK(R.drawable.ic_check_white_24dp));
    }

    public void selectPosition(int index) {
        int drawableId;
        for (int i = 0; i < 6; i++) {
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