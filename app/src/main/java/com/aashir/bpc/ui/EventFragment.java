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

import com.aashir.bpc.R;
import com.aashir.bpc.generic.BaseEventFragment;
import com.aashir.bpc.ui.adapter.EventAdapter;
import com.aashir.bpc.ui.model.EventCard;
import com.aashir.bpc.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class EventFragment extends BaseEventFragment implements SwipeRefreshLayout.OnRefreshListener, EventAdapter.EventInterface {

    private RecyclerView mListView;
    private EventAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getEvents(true);
            }
        }, 100);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_refresh, container, false);
        mAdapter = new EventAdapter(Glide.with(this), new ArrayList<EventCard>(), this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (RecyclerView) rootView.findViewById(R.id.listview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    protected void onDataReceived(ArrayList<EventCard> cards) {
        if (isAdded()) {
            if (cards.size() > 0) {
                mAdapter.redoAll(cards);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getEvents(!Utils.gotInternet(getActivity()));
    }

    @Override
    public void onEventSelected(int position) {
        Intent i = new Intent(getActivity(), DetailEventActivity.class);
        EventCard mCard = mAdapter.getEvent(position);

        i.putExtra("object_id", mCard.getObjectID());
        i.putExtra("image", mCard.getImage());
        i.putExtra("title", mCard.getTitle());
        i.putExtra("content", mCard.getContent());
        i.putExtra("time", mCard.getTime());
        i.putExtra("venue", mCard.getVenue());
        i.putExtra("phone", mCard.getPhone());
        i.putExtra("address", mCard.getAddress());
        i.putExtra("latitude", mCard.getLatitude());
        i.putExtra("longitude", mCard.getLongitude());

        getActivity().startActivity(i);
    }
}