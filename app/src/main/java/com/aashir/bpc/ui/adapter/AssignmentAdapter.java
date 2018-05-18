package com.aashir.bpc.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.adapter.viewholder.AssignmentCardViewHolder;
import com.aashir.bpc.ui.model.AssignmentCard;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AssignmentInterface mInterface;
    private ArrayList<AssignmentCard> mCards;
    private int mTabNo;

    public AssignmentAdapter(AssignmentInterface i, ArrayList<AssignmentCard> c, int tabno) {
        mInterface = i;
        mCards = c;
        mTabNo = tabno;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View vImage = mInflater.inflate(R.layout.card_assignment, viewGroup, false);

        return new AssignmentCardViewHolder(vImage);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        AssignmentCard card = mCards.get(position);
        AssignmentCardViewHolder holder = (AssignmentCardViewHolder) viewHolder;
        holder.mSubject.setText(card.getSubject());
        holder.mTitle.setText(card.getTitle());
        holder.mTime.setText(card.getTime());

        if (mTabNo == 2) {
            holder.mToggleDone.setImageResource(R.drawable.ic_close_white_18dp);
        }

        holder.mToggleDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mInterface.onToggle(position);
            }

        });

        holder.mCard.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mInterface.onAssignmentSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public AssignmentCard getAssignment(int position) {
        return mCards.get(position);
    }

    public void redoAll(ArrayList<AssignmentCard> cards) {
        this.mCards = cards;
        this.notifyDataSetChanged();
    }

    public void removeAssignment(int position) {
        mCards.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCards.size());
    }

    public interface AssignmentInterface {
        void onToggle(int position);

        void onAssignmentSelected(int position);
    }

}