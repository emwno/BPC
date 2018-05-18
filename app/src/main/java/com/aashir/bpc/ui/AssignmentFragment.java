package com.aashir.bpc.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BaseAssignmentFragment;
import com.aashir.bpc.ui.adapter.AssignmentAdapter;
import com.aashir.bpc.ui.model.AssignmentCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.Utils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;

public class AssignmentFragment extends BaseAssignmentFragment implements SwipeRefreshLayout.OnRefreshListener, AssignmentAdapter.AssignmentInterface {

    private RecyclerView mListView;
    private TextView mEmptyText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AssignmentAdapter mAdapter;
    private int mTabNo;
    private String mLocalExtension;
    private String mClass;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        mTabNo = getArguments().getInt("assignment_tab");
        SharedPreferences pref = getActivity().getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        mClass = pref.getString(Key.Preferences.ASSIGNMENT, Key.Class.ASSIGNMENT[0]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mTabNo == 0) {
                    mLocalExtension = "_ONGOING";
                    getOnGoingAssignments(mClass, true);
                } else if (mTabNo == 1) {
                    mLocalExtension = "_OVERDUE";
                    getOverdueAssignments(mClass, true);
                } else if (mTabNo == 2) {
                    mLocalExtension = "_COMPLETED";
                    getCompletedAssignments(mClass, true);
                }

            }
        }, 100);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_refresh, container, false);

        mAdapter = new AssignmentAdapter(this, new ArrayList<AssignmentCard>(), mTabNo);

        mEmptyText = (TextView) rootView.findViewById(R.id.empty_text);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (RecyclerView) rootView.findViewById(R.id.listview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected void onDataReceived(ArrayList<AssignmentCard> cards) {
        if (isAdded()) {
            if (cards.size() > 0) {
                mAdapter.redoAll(cards);
                showEmptyView(View.GONE);
            } else {
                showEmptyView(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        boolean fromPin = !Utils.gotInternet(getActivity());
        if (mTabNo == 0) {
            getOnGoingAssignments(mClass, fromPin);
        } else if (mTabNo == 1) {
            getOverdueAssignments(mClass, fromPin);
        } else if (mTabNo == 2) {
            getCompletedAssignments(mClass, fromPin);
        }
    }

    @Override
    public void onToggle(int position) {
        AssignmentCard mCard;

        try {
            mCard = mAdapter.getAssignment(position);
        } catch(IndexOutOfBoundsException e) {
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(mClass);
        query.fromPin(mClass + mLocalExtension);
        query.getInBackground(mCard.getObjectID(), new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    ParseRelation<ParseObject> relation = obj.getRelation("completedBy");
                    if (mTabNo == 2) {
                        relation.remove(ParseUser.getCurrentUser());
                    } else {
                        relation.add(ParseUser.getCurrentUser());
                    }

                    /* A little hack to "fix" local datastore objects which
                     * were not being updated (via relations) */
                    String to;
                    if (mTabNo == 2) {
                        if (obj.getNumber(Key.Assignment.DUE_TIME).longValue() <= System.currentTimeMillis()) {
                            to = "_OVERDUE";
                        } else {
                            to = "_ONGOING";
                        }
                    } else {
                        to = "_COMPLETED";
                    }
                    obj.pinInBackground(mClass + to);
                    obj.unpinInBackground(mClass + mLocalExtension);

                    obj.saveEventually();
                } else {
                    Toast.makeText(getActivity(), mClass + mLocalExtension + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

        mAdapter.removeAssignment(position);
        if (mAdapter.getItemCount() == 0) showEmptyView(View.VISIBLE);
    }

    @Override
    public void onAssignmentSelected(int position) {
        final Intent i = new Intent(getActivity(), DetailAssignment.class);

        AssignmentCard mCard;

        try {
            mCard = mAdapter.getAssignment(position);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        i.putExtra("position", position);
        i.putExtra("tab_no", mTabNo);
        i.putExtra("subject", mCard.getSubject());
        i.putExtra("content", mCard.getContent());
        i.putExtra("time", mCard.getTime());
        i.putExtra("title", mCard.getTitle());
        i.putExtra("teacher", mCard.getTeacher());
        i.putExtra("post_time", mCard.getPostTime());
        i.putExtra("due_time", mCard.getDueTime());
        if (mTabNo == 2) {
            i.putExtra("is_completed", true);
        } else {
            i.putExtra("is_completed", false);
        }

        getParentFragment().startActivityForResult(i, 21);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int tab = data.getIntExtra("tab_no", -1);
        if (mTabNo == tab) {
            int pos = data.getIntExtra("position", -1);
            if (pos >= 0) onToggle(pos);
        }
    }

    private void showEmptyView(int visibility) {
        mEmptyText.setVisibility(visibility);
        if (mTabNo == 0) {
            mEmptyText.setText("You're all good!");
        } else if (mTabNo == 1) {
            mEmptyText.setText("Nothing is overdue");
        } else if (mTabNo == 2) {
            mEmptyText.setText("Nothing has been completed");
        }
    }
}