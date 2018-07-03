package com.aashir.bpc.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.DetailImageActivity;
import com.aashir.bpc.ui.DetailPostActivity;
import com.aashir.bpc.ui.adapter.viewholder.AssignmentCardViewHolder;
import com.aashir.bpc.ui.adapter.viewholder.PostCardViewHolder;
import com.aashir.bpc.ui.model.AssignmentCard;
import com.aashir.bpc.ui.model.PostCard;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private int POST = 0;
    private int ASS = 1;

    private RequestManager mRequestManager;
    private ArrayList<Object> mCards;
    private Context mContext;

    public HomeAdapter(RequestManager req, Context context, ArrayList<Object> c) {
        mRequestManager = req;
        mContext = context;
        mCards = c;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;

        if (mCards.get(position) instanceof PostCard) {
            viewType = POST;
        } else if (mCards.get(position) instanceof AssignmentCard) {
            viewType = ASS;
        } else {
            viewType = 2;
        }

        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == POST) {
            return new PostCardViewHolder(mInflater.inflate(R.layout.card_no_image, viewGroup, false));
        } else if (viewType == ASS){
            return new AssignmentCardViewHolder(mInflater.inflate(R.layout.card_assignment, viewGroup, false));
        } else {
            return new SectionViewHolder(mInflater.inflate(R.layout.layout, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == POST) {
            final PostCard card = (PostCard) mCards.get(position);
            final PostCardViewHolder holder = (PostCardViewHolder) viewHolder;
            holder.mAuthor.setText(card.getAuthor());
            holder.mContent.setText(card.getContent());
            holder.mTime.setText(card.getTime());
            mRequestManager.load(card.getProfilePicture()).placeholder(R.drawable.placeholder_user).into(holder.mProfilePicture);
        } else if (getItemViewType(position) == ASS){
            AssignmentCard card = (AssignmentCard) mCards.get(position);
            AssignmentCardViewHolder holder = (AssignmentCardViewHolder) viewHolder;
            holder.mSubject.setText(card.getSubject());
            holder.mTitle.setText(card.getTitle());
            holder.mTime.setText(card.getTime());
        } else {
            SectionViewHolder holder = (SectionViewHolder) viewHolder;
            holder.title.setText((String) mCards.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void redoAll(ArrayList<Object> cards, int pos, int size) {
        mCards.addAll(cards);
        // for adding cards to bottom of list
        this.notifyItemRangeInserted(pos, size);
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public SectionViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.ssss);
        }
    }
}