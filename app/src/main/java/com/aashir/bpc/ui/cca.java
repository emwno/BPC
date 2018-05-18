package com.aashir.bpc.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;
import com.aashir.bpc.ui.model.CommentCard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class cca extends RecyclerView.Adapter<cca.ViewHolder> {

    private ArrayList<CommentCard> mComments = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public cca(Context context, ArrayList<CommentCard> data) {
        super();
        this.mComments = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.card_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder mHolder, int position) {
        CommentCard mCard = mComments.get(position);
        mHolder.mAuthor.setText(mCard.getAuthor());
        mHolder.mContent.setText(mCard.getContent());
        mHolder.mTime.setText(mCard.getTime());
        Glide.with(mContext).load(mCard.getProfilePicture()).into(mHolder.mPP);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mAuthor;
        TextView mContent;
        TextView mTime;
        ImageView mPP;

        public ViewHolder(View v) {
            super(v);
            mAuthor = (TextView) v.findViewById(R.id.comment_author);
            mContent = (TextView) v.findViewById(R.id.comment_content);
            mTime = (TextView) v.findViewById(R.id.comment_time);
            mPP = (ImageView) v.findViewById(R.id.comment_profilepicture);
        }
    }

}