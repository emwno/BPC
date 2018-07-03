package com.aashir.bpc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;

import com.aashir.bpc.ui.BaseBulletinFragment;
import com.aashir.bpc.ui.BaseSocietyFragment;
import com.aashir.bpc.ui.ContactFragment;
import com.aashir.bpc.ui.EventFragment;
import com.aashir.bpc.ui.PagerFragment;
import com.aashir.bpc.ui.TeacherFragment;
import com.aashir.bpc.ui.model.Key;
import com.parse.ParseUser;

import java.util.Set;

public class MainActivity extends BaseActivity {

    @Override
    protected void allSet(ParseUser user) {
        SharedPreferences pref = getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        String timetable = pref.getString(Key.Preferences.TIMETABLE, null);
        String society = pref.getString(Key.Preferences.SOCIETY, null);
        Set<String> tempset = pref.getStringSet(Key.Preferences.SUBJECTS, null);

        if (user == null || timetable == null || society == null || tempset == null) toLogin();
    }

    @Override
    protected void selectItem(int id) {
        mNavigationSelectedID = id;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        Bundle b = new Bundle();

        switch (id) {
            case R.id.navigation_bulletin:
                mFragment = new BaseBulletinFragment();
                setTitle(mNavigationTitles[0]);
                break;

            case R.id.navigation_society:
                mFragment = new BaseSocietyFragment();
                setTitle(mNavigationTitles[1]);
                break;

            case R.id.navigation_assignment:
                b.putInt("which", 2);
                mFragment = new PagerFragment();
                mFragment.setArguments(b);
                setTitle(mNavigationTitles[2]);
                break;

            case R.id.navigation_timetable:
                b.putInt("which", 3);
                mFragment = new PagerFragment();
                mFragment.setArguments(b);
                setTitle(mNavigationTitles[3]);
                break;

            case R.id.navigation_event:
                mFragment = new EventFragment();
                setTitle(mNavigationTitles[4]);
                break;

            case R.id.navigation_teachers:
                mFragment = new TeacherFragment();
                setTitle(mNavigationTitles[5]);
                break;

            case R.id.navigation_contact:
                mFragment = new ContactFragment();
                setTitle(mNavigationTitles[6]);
                break;

            case R.id.navigation_logout:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                logout();
                break;
        }

        if (mFragment != null) {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, mFragment);
            ft.commit();
        }
    }

    @Override
    protected Activity getContext() {
        return this;
    }
}