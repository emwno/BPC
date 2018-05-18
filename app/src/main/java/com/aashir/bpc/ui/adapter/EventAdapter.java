package com.aashir.bpc.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.viewholder.EventCardViewHolder;
import com.aashir.bpc.ui.model.EventCard;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<ViewHolder> {

    private RequestManager mRequestManager;
    private ArrayList<EventCard> mCards;
    private EventInterface mInterface;

    public EventAdapter(RequestManager req, ArrayList<EventCard> c, EventInterface interf) {
        mRequestManager = req;
        mCards = c;
        mInterface = interf;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View vImage = mInflater.inflate(R.layout.card_event, viewGroup, false);

        return new EventCardViewHolder(vImage);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        EventCard card = mCards.get(position);
        EventCardViewHolder holder = (EventCardViewHolder) viewHolder;
        holder.mTitle.setText(card.getTitle());
        holder.mTime.setText(card.getTime());
        mRequestManager.load(card.getImage()).centerCrop().into(holder.mImage);

        holder.mCard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mInterface.onEventSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void redoAll(ArrayList<EventCard> cards) {
        this.mCards = cards;
        this.notifyDataSetChanged();
    }

    public EventCard getEvent(int position) {
        return mCards.get(position);
    }

    public interface EventInterface {
        void onEventSelected(int position);
    }
}