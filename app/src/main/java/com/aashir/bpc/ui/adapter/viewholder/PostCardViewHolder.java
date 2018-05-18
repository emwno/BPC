package com.aashir.bpc.ui.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashir.bpc.R;

public class PostCardViewHolder extends RecyclerView.ViewHolder {

    public CardView mCard;
    public TextView mAuthor;
    public TextView mContent;
    public TextView mTime;
    public ImageView mImage;
    public ImageView mProfilePicture;

    public ImageButton mLike;
    public ImageButton mComment;
    public ImageButton mShare;

    public PostCardViewHolder(View itemView) {
        super(itemView);
        mCard = (CardView) itemView.findViewById(R.id.cardView);
        mAuthor = (TextView) itemView.findViewById(R.id.bulletin_author);
        mContent = (TextView) itemView.findViewById(R.id.bulletin_content);
        mTime = (TextView) itemView.findViewById(R.id.bulletin_time);
        mImage = (ImageView) itemView.findViewById(R.id.bulletin_image);
        mProfilePicture = (ImageView) itemView.findViewById(R.id.bulletin_profilepicture);

        mLike = (ImageButton) itemView.findViewById(R.id.bulletin_like);
        mComment = (ImageButton) itemView.findViewById(R.id.bulletin_comment);
        mShare = (ImageButton) itemView.findViewById(R.id.bulletin_share);
    }

}