package com.aashir.bpc.ui.adapter.viewholder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aashir.bpc.R;

public class AssignmentCardViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public TextView mPostedOn;
    public TextView mDueOn;
    public CardView mCard;
    public FloatingActionButton mEdit;

    public AssignmentCardViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.assignment_admin_title);
        mPostedOn = (TextView) itemView.findViewById(R.id.assignment_admin_posted_on);
        mDueOn = (TextView) itemView.findViewById(R.id.assignment_admin_due_on);
        mEdit = (FloatingActionButton) itemView.findViewById(R.id.assignment_toggle);
        mCard = (CardView) itemView.findViewById(R.id.cardView);
    }

}