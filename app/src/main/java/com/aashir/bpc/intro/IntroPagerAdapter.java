package com.aashir.bpc.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aashir.bpc.R;

public class IntroPagerAdapter extends FragmentStatePagerAdapter {

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        Bundle b = new Bundle();

        switch (position) {
            case 0:
                frag = new SimpleFragment();
                b.putInt("layout_id", R.layout.intro_fragment_intro);
                frag.setArguments(b);
                break;

            case 1:
                frag = new LoginFragment();
                break;

            case 2:
                frag = new PickClassFragment();
                break;

            case 3:
                frag = new PickSocietyFragment();
                break;

            case 4:
                frag = new PickSubjectFragment();
                break;

            case 5:
                frag = new SimpleFragment();
                b.putInt("layout_id", R.layout.intro_fragment_complete);
                frag.setArguments(b);
                break;
        }

        return frag;
    }

    @Override
    public int getCount() {
        return 6;
    }


}