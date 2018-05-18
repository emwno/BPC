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

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BaseAssignmentFragment;
import com.aashir.bpc.ui.adapter.AssignmentAdminAdapter;
import com.aashir.bpc.ui.model.AssignmentCard;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.util.Utils;

import java.util.ArrayList;

public class AssignmentAdminFragment extends BaseAssignmentFragment implements SwipeRefreshLayout.OnRefreshListener, AssignmentAdminAdapter.AssignmentAdminInterface {

    private RecyclerView mListView;
    private TextView mEmptyText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AssignmentAdminAdapter mAdapter;
    private String mClass;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        SharedPreferences pref = getActivity().getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        mClass = pref.getString(Key.Preferences.ASSIGNMENT, Key.Class.ASSIGNMENT[0]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getCompletedAssignments(mClass, true);
            }
        }, 100);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_refresh, container, false);

        mAdapter = new AssignmentAdminAdapter(this, new ArrayList<AssignmentCard>());

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
        getCompletedAssignments(mClass, fromPin);
    }

    private void showEmptyView(int visibility) {
        mEmptyText.setVisibility(visibility);
        mEmptyText.setText("No Assignments");
    }

    @Override
    public void onEdit(int position) {

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
        i.putExtra("tab_no", -1);
        i.putExtra("subject", mCard.getSubject());
        i.putExtra("content", mCard.getContent());
        i.putExtra("time", mCard.getTime());
        i.putExtra("title", mCard.getTitle());
        i.putExtra("teacher", mCard.getTeacher());
        i.putExtra("post_time", mCard.getPostTime());
        i.putExtra("due_time", mCard.getDueTime());

        getActivity().startActivity(i);
    }
}