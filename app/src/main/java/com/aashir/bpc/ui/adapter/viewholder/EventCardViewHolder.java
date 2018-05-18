package com.aashir.bpc.ui.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;

public class EventCardViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImage;
    public TextView mTitle;
    public TextView mTime;
    public CardView mCard;

    public EventCardViewHolder(View itemView) {
        super(itemView);
        mImage = (ImageView) itemView.findViewById(R.id.event_image);
        mTitle = (TextView) itemView.findViewById(R.id.event_title);
        mTime = (TextView) itemView.findViewById(R.id.event_time);
        mCard = (CardView) itemView.findViewById(R.id.cardView);
    }

}