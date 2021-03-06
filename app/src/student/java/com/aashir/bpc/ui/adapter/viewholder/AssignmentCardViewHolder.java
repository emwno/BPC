package com.aashir.bpc.ui.adapter.viewholder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aashir.bpc.R;

public class AssignmentCardViewHolder extends RecyclerView.ViewHolder {

    public TextView mSubject;
    public TextView mTitle;
    public TextView mTime;
    public CardView mCard;
    public FloatingActionButton mToggleDone;

    public AssignmentCardViewHolder(View itemView) {
        super(itemView);
        mSubject = (TextView) itemView.findViewById(R.id.assignment_subject);
        mTitle = (TextView) itemView.findViewById(R.id.assignment_title);
        mTime = (TextView) itemView.findViewById(R.id.assignment_time);
        mToggleDone = (FloatingActionButton) itemView.findViewById(R.id.assignment_toggle);
        mCard = (CardView) itemView.findViewById(R.id.cardView);
    }

}