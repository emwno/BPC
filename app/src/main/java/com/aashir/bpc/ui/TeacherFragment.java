package com.aashir.bpc.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BaseTeacherFragment;
import com.aashir.bpc.ui.adapter.TeacherAdapter;
import com.aashir.bpc.ui.model.TeacherCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TeacherFragment extends BaseTeacherFragment implements SearchView.OnQueryTextListener {

    private RecyclerView mListView;
    private TextView mEmptyText;
    private TeacherAdapter mAdapter;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        setHasOptionsMenu(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTeachers(null);
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, container, false);
        mAdapter = new TeacherAdapter(getActivity(), Glide.with(this), new ArrayList<TeacherCard>());

        mEmptyText = (TextView) rootView.findViewById(R.id.empty_text);

        mListView = (RecyclerView) rootView.findViewById(R.id.listview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected void onDataReceived(ArrayList<TeacherCard> cards) {
        if (isAdded()) {
            if (cards.size() > 0) {
                mAdapter.redoAll(cards);
                showEmptyView(View.GONE);
            } else {
                showEmptyView(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            getTeachers("");
        } else {
            getTeachers(newText);
        }
        return true;
    }

    private void showEmptyView(int visibility) {
        mEmptyText.setVisibility(visibility);
        if (visibility == View.VISIBLE) mEmptyText.setText("No Results :(");
    }
}