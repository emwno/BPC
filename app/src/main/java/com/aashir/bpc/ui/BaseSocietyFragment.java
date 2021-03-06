package com.aashir.bpc.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BasePostFragment;
import com.aashir.bpc.ui.adapter.PostAdapter;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.PostCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BaseSocietyFragment extends BasePostFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView mListView;
    private PostAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mClass;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        SharedPreferences pref = getActivity().getSharedPreferences(Key.Preferences.ID, Context.MODE_PRIVATE);
        mClass = pref.getString(Key.Preferences.SOCIETY, Key.Class.SOCIETY[0]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSocietyPosts(mClass, true);
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_post, container, false);
        mAdapter = new PostAdapter(Glide.with(this), getActivity(), new ArrayList<PostCard>());

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (RecyclerView) rootView.findViewById(R.id.listview);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected void onDataReceived(ArrayList<PostCard> cards) {
        if (isAdded()) {
            if (cards.size() > 0) {
                mAdapter.redoAll(cards);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getSocietyPosts(mClass, false);
    }

}