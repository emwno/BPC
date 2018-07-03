package com.aashir.bpc.intro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.Key;

public class PickSocietyFragment extends Fragment {

    private String[] mSociety = {"Art", "IT", "Media"};
    private ListView lv;
    private IntroInterface mListener;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (lv.getCheckedItemCount() > 0) {
                mListener.onSocietyPick(Key.Class.SOCIETY[lv.getCheckedItemPosition()]);
            } else {
                if (isAdded())
                    Toast.makeText(getActivity().getApplicationContext(), "Pick a Society", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("society"));
            mListener = (IntroInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IntroInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.intro_fragment_subject, container, false);

        lv = (ListView) v.findViewById(R.id.intro_fragment_subject);
        TextView title = (TextView) v.findViewById(R.id.title);

        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        ChoiceAdapter mAdapter = new ChoiceAdapter(getActivity(), R.layout.intro_fragment_society_item, mSociety);
        title.setText("Society");
        lv.setAdapter(mAdapter);

        return v;
    }

}
