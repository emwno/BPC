package com.aashir.bpc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BasePostFragment;
import com.aashir.bpc.ui.adapter.PostAdapter;
import com.aashir.bpc.ui.model.Key;
import com.aashir.bpc.ui.model.PostCard;
import com.aashir.bpc.util.RecyclerViewListener;
import com.aashir.bpc.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BulletinFragment extends BasePostFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mListView;
    private PostAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBulletinPosts(true);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (isAdmin()) {
            final View fab = ((ViewStub) view.findViewById(R.id.fab_stub)).inflate();
            final int fabMargin = getResources().getDimensionPixelSize(R.dimen.big_padding);

            mListView.addOnScrollListener(new RecyclerViewListener() {

                public void show() {
                    fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }

                @Override
                public void hide() {
                    fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
                }
            });

            fab.setOnClickListener(this);
        }
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
        getBulletinPosts(!Utils.gotInternet(getActivity()));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), NewPost.class);
        i.putExtra("class", Key.Class.BULLETIN);
        startActivity(i);
    }
}