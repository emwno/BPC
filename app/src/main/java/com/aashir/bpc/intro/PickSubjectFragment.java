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

import java.util.HashSet;
import java.util.Set;

public class PickSubjectFragment extends Fragment {

    ListView lv;
    private String[] mSubject = {"Physics", "Maths", "Biology", "Chemistry", "Sociology", "Computers", "Urdu", "Islamiyat"};
    private IntroInterface mListener;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (lv.getCheckedItemCount() > 3) {
                mListener.onSubjectPick(getSubjects());
            } else {
                if (isAdded())
                    Toast.makeText(getActivity(), "Pick at least 4 Subjects ", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IntroInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IntroInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("subject"));
        View v = inflater.inflate(R.layout.intro_fragment_subject, container, false);

        lv = (ListView) v.findViewById(R.id.intro_fragment_subject);
        TextView title = (TextView) v.findViewById(R.id.title);

        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ChoiceAdapter mAdapter = new ChoiceAdapter(getActivity(), R.layout.intro_fragment_subject_item, mSubject);
        title.setText("Subjects");

        lv.setAdapter(mAdapter);
        return v;
    }

    private Set<String> getSubjects() {
        Set<String> s = new HashSet<>();

        for (int i = 0; i <= lv.getCheckedItemPositions().size() - 1; i++) {
            if (lv.getCheckedItemPositions().valueAt(i)) s.add(mSubject[i]);
        }
        return s;
    }
}
