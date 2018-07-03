package com.aashir.bpc.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.viewholder.TimeTableCardViewHolder;
import com.aashir.bpc.ui.model.TimeTableCard;

import java.util.ArrayList;

public class TimeTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TimeTableCard> mCards;

    public TimeTableAdapter(ArrayList<TimeTableCard> c) {
        mCards = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View vImage = mInflater.inflate(R.layout.card_timetable, viewGroup, false);
        return new TimeTableCardViewHolder(vImage);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TimeTableCard card = mCards.get(position);
        TimeTableCardViewHolder holder = (TimeTableCardViewHolder) viewHolder;
        holder.mSubject.setText(card.getSubject());
        holder.mTime.setText(card.getTime());
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void redoAll(ArrayList<TimeTableCard> cards) {
        this.mCards = cards;
        this.notifyDataSetChanged();
    }

}