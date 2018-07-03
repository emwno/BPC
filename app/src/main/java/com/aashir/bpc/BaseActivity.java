package com.aashir.bpc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.intro.IntroActivity;
import com.aashir.bpc.ui.AssignmentFragment;
import com.aashir.bpc.ui.BaseBulletinFragment;
import com.aashir.bpc.ui.ContactFragment;
import com.aashir.bpc.ui.EventFragment;
import com.aashir.bpc.ui.PagerFragment;
import com.aashir.bpc.ui.BaseSocietyFragment;
import com.aashir.bpc.ui.TeacherFragment;
import com.aashir.bpc.ui.model.Key;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.Set;

public abstract class BaseActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    protected String[] mNavigationTitles;
    protected DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    protected int mNavigationSelectedID;

    protected Fragment mFragment;

    protected abstract void allSet(ParseUser user);
    protected abstract void selectItem(int id);
    protected abstract Activity getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser mUser = ParseUser.getCurrentUser();
        allSet(mUser);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNavigationTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        LayoutInflater in = LayoutInflater.from(this);
        final ViewGroup header = (ViewGroup) in.inflate(R.layout.header_navigation, mNavigationView, false);

        ImageView pic = (ImageView) header.findViewById(R.id.user_pic);
        TextView name = (TextView) header.findViewById(R.id.user_name);

        if (mUser != null) {
            name.setText(mUser.getString(Key.User.NAME));
            ParseFile pf = mUser.getParseFile(Key.User.IMAGE);
            if(pf != null) Glide.with(this).load(pf.getUrl()).placeholder(R.drawable.placeholder_user).into(pic);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mNavigationView.addHeaderView(header);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mTitle.equals(mNavigationTitles[5])) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getContext().getCurrentFocus().getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (savedInstanceState == null) {
            selectItem(R.id.navigation_bulletin);
        } else {
            mNavigationSelectedID = savedInstanceState.getInt("nav_pos");
            mNavigationView.getMenu().findItem(mNavigationSelectedID).setChecked(true);
            setTitle(savedInstanceState.getString("title"));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(mTitle);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = (String) title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", mTitle);
        outState.putInt("nav_pos", mNavigationSelectedID);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void logout() {
        SharedPreferences pref = getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        String ass = pref.getString(Key.Preferences.ASSIGNMENT, null);
        String tt = pref.getString(Key.Preferences.TIMETABLE, null);
        String soc = pref.getString(Key.Preferences.SOCIETY, Key.Class.SOCIETY[0]);

        unsubscribe(ass, tt, soc, Key.Class.BULLETIN);
        pref.edit().clear().apply();
        ParseUser.logOutInBackground();
        toLogin();
    }

    protected void toLogin() {
        Intent i = new Intent(this, IntroActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void unsubscribe(String... all) {
        for (String each : all) {
            ParsePush.unsubscribeInBackground(each);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        selectItem(menuItem.getItemId());
        menuItem.setChecked(true);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
